package mypicday.store.diary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.dto.response.DiaryResponse;
import mypicday.store.diary.service.DiaryService;
import mypicday.store.file.FileUtil;
import mypicday.store.global.config.CustomUserDetails;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CalendarController {

    private final DiaryService diaryService;
    private final FileUtil fileUtil;

    @GetMapping("/diaries/user/{userId}/monthly")
    public ResponseEntity<List<DiaryResponse>> getMonthlyDiaries(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable String userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        // 해당하는 연과 월에 맞는 다이어리 정보들을 반환합니다
        List<DiaryResponse> diaries = diaryService.findMonthlyDiaries(userId, year, month);
        return ResponseEntity.ok(diaries);
    }

    @GetMapping("/diaries/images/{userId}/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String userId, @PathVariable String filename) {
        try {
            Resource resource = fileUtil.loadFileAsResource(userId + "/" + filename);
            String contentType = fileUtil.getContentType(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.warn("파일 조회 실패 - 경로: {}, 오류: {}", filename, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
