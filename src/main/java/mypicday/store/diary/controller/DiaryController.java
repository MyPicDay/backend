package mypicday.store.diary.controller;

import lombok.RequiredArgsConstructor;
import mypicday.store.diary.service.DiaryService;
import mypicday.store.diary.dto.response.DiaryResponse;
import mypicday.store.global.config.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryController {
    private final DiaryService diaryService;

    @GetMapping
    public ResponseEntity<Page<DiaryResponse>> getDiaries(
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Page<DiaryResponse> diaries = diaryService.findDiaries(user.getId(), pageable);
        return ResponseEntity.ok(diaries);
    }
}
