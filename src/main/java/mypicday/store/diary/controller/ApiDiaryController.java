package mypicday.store.diary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.dto.DiaryDto;
import mypicday.store.diary.dto.response.UserDiaryDto;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.service.DiaryService;
import mypicday.store.global.config.CustomUserDetails;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ApiDiaryController {

    private final DiaryService diaryService;

    @PostMapping("/diary")
    public ResponseEntity<Map<String ,String>> Diary(@ModelAttribute DiaryDto diaryDto,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        String userId = customUserDetails.getId();
        Optional<Diary> diary = diaryService.updateDiary(userId, diaryDto);
        if (diary.isPresent()) {
            return ResponseEntity.ok(Map.of("id", userId));
        }

        
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


        diaryService.save(userId, diaryDto);
        return ResponseEntity.ok(Map.of("id", userId));
    }


    @GetMapping("/diary")
    public ResponseEntity<UserDiaryDto> editDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date ,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("editDiary: date: {}", date);
        String userId = customUserDetails.getId();
        UserDiaryDto userDiary = diaryService.findUserDiary(userId, date);
        log.info("userDiary: {}", userDiary);
        return ResponseEntity.ok(userDiary);

    }




}

