package mypicday.store.diary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.dto.DiaryDto;
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
    public ResponseEntity<String> Diary(@ModelAttribute DiaryDto diaryDto ,@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        String userId= customUserDetails.getId();
        List<String> images = new ArrayList<>();

        for(MultipartFile file : diaryDto.getImages()){
            if (!file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path path = Paths.get("uploads/" + fileName);
                Files.copy(file.getInputStream(), path);  // 현재는 서버 로컬에 저장
                images.add("/uploads/" + fileName);
            }
        }
        diaryDto.setAllImages(images);
        diaryService.save(userId , diaryDto);

        return ResponseEntity.ok().build() ;
    }

}
