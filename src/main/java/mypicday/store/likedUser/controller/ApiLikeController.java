package mypicday.store.likedUser.controller;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.service.DiaryService;
import mypicday.store.global.config.CustomUserDetails;
import mypicday.store.likedUser.Dto.LikedDto;
import mypicday.store.likedUser.Dto.ResponseLikeCountDto;
import mypicday.store.likedUser.service.LikedUserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiLikeController {

    private final LikedUserService likedUserService;
    private final DiaryService diaryService;

    @PostMapping("/diary/like")
    public void UsersLike(@RequestBody LikedDto likedDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String userId = customUserDetails.getId();
        likedUserService.LikedUser(userId , likedDto);
    }

   @GetMapping("/diary/{diaryId}")
    public ResponseEntity<ResponseLikeCountDto> DiaryLikesCount(@PathVariable Long diaryId) {
        log.info("Diary Id = {} " , diaryId);
        return new ResponseEntity<>(new ResponseLikeCountDto(diaryService.findLikeCount(diaryId)) , HttpStatusCode.valueOf(200));
    }
}
