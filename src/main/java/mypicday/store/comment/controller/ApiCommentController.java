package mypicday.store.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.comment.dto.reponse.UserCommentsDto;
import mypicday.store.comment.dto.request.CommentDto;
import mypicday.store.comment.dto.reponse.ResponseCommentUser;
import mypicday.store.comment.dto.request.ReplyDto;
import mypicday.store.comment.entity.Comment;
import mypicday.store.comment.service.CommentService;
import mypicday.store.global.config.CustomUserDetails;
import mypicday.store.user.entity.User;
import mypicday.store.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ApiCommentController {

    private final CommentService commentService;

    private final UserService userService;

    @PostMapping("/diary/comment") // 일반피드 댓글 등록
    public ResponseEntity<ResponseCommentUser> saveComment(@RequestBody CommentDto commentDto  , @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("commentDto: {}", commentDto);
        String userId = customUserDetails.getId();
        User findUser = userService.getUserById(userId);
        Comment comment = commentService.saveComment(commentDto, userId);
        Long commentId = comment.getId();
        LocalDate localDate = comment.getCreatedAt().toLocalDate();
        String avator = "/images/logo.png" ; // 나중에 프로필 이미지 수정

        ResponseCommentUser responseCommentUser = new ResponseCommentUser(commentId, findUser.getNickname(), avator, localDate);
        log.info("responseCommentUser: {}", responseCommentUser);

        return new ResponseEntity<>(responseCommentUser ,HttpStatus.OK);
    }


    @PostMapping("/diary/reply") // 답글
    public ResponseEntity<ResponseCommentUser> updateComment(@RequestBody ReplyDto replyDto ,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        String userId = customUserDetails.getId();
        Comment reply = commentService.saveReply(userId, replyDto);
        User findUser = userService.getUserById(userId);
        Long replyId = reply.getId();
        LocalDate localDate = reply.getCreatedAt().toLocalDate();
        String avator = "/images/logo.png" ; // 나중에 프로필 이미지 수정

        ResponseCommentUser responseCommentUser = new ResponseCommentUser(replyId, findUser.getNickname(), avator, localDate);
        log.info("responseCommentUser: {}", responseCommentUser);

        return new ResponseEntity<>(responseCommentUser, HttpStatus.OK);
    }
}
