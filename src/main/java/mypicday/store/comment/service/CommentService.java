package mypicday.store.comment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.comment.dto.request.CommentDto;
import mypicday.store.comment.dto.request.ReplyDto;
import mypicday.store.comment.entity.Comment;
import mypicday.store.comment.repository.CommentRepository;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.notification.entity.Notification;
import mypicday.store.notification.repository.NotificationRepository;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final DiaryRepository diaryRepository;

    private final NotificationRepository notificationRepository;

   // 댓글 등록
    public Comment saveComment(CommentDto commentDto ,String userId) {
        log.info("commentDto = {}", commentDto);
        Long findDiaryId = commentDto.getDiaryId();
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("유저를 찾을수 없습니다."));
        Diary diary = diaryRepository.findById(findDiaryId).orElseThrow(()->new RuntimeException("일기를 찾을수 없습니다."));
        Comment.createComment(user, diary, commentDto.getComment());

        // 알림 로직
        if (!user.getId().equals(diary.getUser().getId())) {
            Notification notification = new Notification();
            notification.setReceiver(diary.getUser()); // 알림 받을 사람
            notification.setType("COMMENT");
            notification.setSenderId(user.getId());
            notification.setMessage(user.getNickname() + "님이 당신의 일기에 댓글을 남겼습니다.");
            notification.setReferenceId(diary.getId().toString());
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
        }

        return commentRepository.save(Comment.createComment(user, diary, commentDto.getComment()));
    }

    // 대댓글 등록
    public Comment saveReply(String userId, ReplyDto replyDto) {
        Long findDiaryId = replyDto.getDiaryId();
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("유저를 찾을수 없습니다."));
        Diary diary = diaryRepository.findById(findDiaryId).orElseThrow(()->new RuntimeException("일기를 찾을수 없습니다."));
        Comment parentComment = commentRepository.findById(replyDto.getParentCommentId()).get();
        //log.info("parentComment = {}", parentComment);

        // 대댓글 작성자가 다이어리 주인과 다르면 일기 주인에게 알림
        if (!user.getId().equals(diary.getUser().getId())) {
            Notification notification = new Notification();
            notification.setReceiver(diary.getUser());
            notification.setType("REPLY");
            notification.setSenderId(user.getId());
            notification.setMessage(user.getNickname() + "님이 당신의 일기에 대댓글을 남겼습니다.");
            notification.setReferenceId(diary.getId().toString());
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
        }

        // 대댓글 작성자가 원댓글 작성자와 다르고, 원댓글 작성자가 일기 주인과 다르면 원댓글 작성자에게 알림
        if (!user.getId().equals(parentComment.getUser().getId()) &&
                !parentComment.getUser().getId().equals(diary.getUser().getId())) {

            Notification notification = new Notification();
            notification.setReceiver(parentComment.getUser());
            notification.setType("REPLY");
            notification.setSenderId(user.getId());
            notification.setMessage(user.getNickname() + "님이 당신의 댓글에 대댓글을 남겼습니다.");
            notification.setReferenceId(diary.getId().toString());
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
        }

        return commentRepository.save(Comment.createReply(user , diary ,replyDto.getComment() , parentComment));
    }

    public int commentCountByDiaryId(Long diaryId){
        return commentRepository.countByDiary_Id(diaryId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findAllByDiaryId(Long diaryId) {
        return commentRepository.findAllByDiaryId(diaryId);
    }

}
