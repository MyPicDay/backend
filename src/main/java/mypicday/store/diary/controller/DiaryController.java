package mypicday.store.diary.controller;

import lombok.RequiredArgsConstructor;
import mypicday.store.diary.DiaryService;
import mypicday.store.diary.dto.response.DiaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @packageName    : mypicday.diary.controller
 * @fileName       : DiaryController
 * @author         : User
 * @description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 5. 19.        User       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryController {
    private final DiaryService diaryService;

    @GetMapping
    public ResponseEntity<Page<DiaryResponse>> getDiaries(
            Pageable pageable
    ) {
        Page<DiaryResponse> diaries = diaryService.findDiaries(null, pageable);
        return ResponseEntity.ok(diaries);
    }
}
