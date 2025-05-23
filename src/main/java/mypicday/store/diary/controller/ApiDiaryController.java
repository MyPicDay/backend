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

    private static final String UPLOAD_DIRECTORY = "upload";
    private final DiaryService diaryService;

    @PostConstruct
    public void init() {
        Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("업로드 디렉토리 생성됨: " + uploadPath.toAbsolutePath());
            } else {
                System.out.println("업로드 디렉토리 이미 존재함: " + uploadPath.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("업로드 디렉토리 생성 실패: " + uploadPath.toAbsolutePath(), e);
        }
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
