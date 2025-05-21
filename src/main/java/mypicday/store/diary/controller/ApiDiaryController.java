package mypicday.store.diary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.dto.DiaryDto;
import mypicday.store.diary.service.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ApiDiaryController {

    private final DiaryService diaryService;

    @PostMapping("/diary")
    public ResponseEntity<String> Diary(@RequestBody DiaryDto diaryDto) {
        String userId = "1";
        diaryService.save(userId , diaryDto);
        return ResponseEntity.ok().build() ;
    }

}
