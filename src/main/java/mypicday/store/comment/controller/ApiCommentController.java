package mypicday.store.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.comment.dto.CommentDto;
import mypicday.store.comment.service.CommentService;
import mypicday.store.global.config.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ApiCommentController {

    private final CommentService commentService;

    @PostMapping("/diary/comment")
    public ResponseEntity<String> saveComment(@RequestBody CommentDto commentDto  , @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("commentDto: {}", commentDto);
        String userId = customUserDetails.getId();
        commentService.saveComment(commentDto  , userId );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/diary/comment/{commentId}") // 답글
    public ResponseEntity<String> updateComment(String userId , @RequestBody CommentDto commentDto, @PathVariable Long commentId) {

        commentService.saveReply(userId, commentDto , commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
