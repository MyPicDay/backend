package mypicday.store.diary.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.dto.DiaryDto;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.service.DiaryService;
import mypicday.store.global.config.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ApiDiaryController {

    private final DiaryService diaryService;

    @PostMapping("/diary")
    public ResponseEntity<String> Diary(@ModelAttribute DiaryDto diaryDto,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        String userId = customUserDetails.getId();
        List<String> images = new ArrayList<>();

        // uploads 폴더 존재 확인 및 생성
        Path uploadDir = Paths.get("uploads");
        if (Files.notExists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        if (diaryDto.getImages() != null) {
            for (MultipartFile file : diaryDto.getImages()) {
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = uploadDir.resolve(fileName);

                    Files.copy(file.getInputStream(), filePath);  // 서버 로컬에 저장
                    images.add("/uploads/" + fileName);
                }
            }
        }

        diaryDto.setAllImages(images);
        log.info("diaryDto: {}", diaryDto.getAllImages());

        diaryService.save(userId, diaryDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/diary")
    public ResponseEntity<Long> Diary(@ModelAttribute DiaryDto diaryDto ,@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        String userId= customUserDetails.getId();
        List<String> images = new ArrayList<>();

        for(MultipartFile file : diaryDto.getImages()){
            if (!file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIRECTORY + "/" + fileName);
                Files.copy(file.getInputStream(), path);  // 현재는 서버 로컬에 저장
                images.add(fileName);
            }
        }
        diaryDto.setAllImages(images);

        Diary diary = diaryService.save(userId , diaryDto);

        return ResponseEntity.ok(diary.getId());
    }
}
