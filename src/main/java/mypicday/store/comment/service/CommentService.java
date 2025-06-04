package mypicday.store.comment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.comment.dto.request.CommentDto;
import mypicday.store.comment.dto.request.ReplyDto;
import mypicday.store.comment.entity.Comment;
import mypicday.store.comment.repository.CommentRepository;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.notification.dto.NotificationDTO;
import mypicday.store.notification.entity.Notification;
import mypicday.store.notification.repository.NotificationRepository;
import mypicday.store.notification.service.NotificationService;
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
    private final NotificationService notificationService;

   // 댓글 등록
    public Comment saveComment(CommentDto commentDto ,String userId) {
        log.info("commentDto = {}", commentDto);
        Long findDiaryId = commentDto.getDiaryId();
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("유저를 찾을수 없습니다."));
        Diary diary = diaryRepository.findById(findDiaryId).orElseThrow(()->new RuntimeException("일기를 찾을수 없습니다."));
        Comment.createComment(user, diary, commentDto.getComment());

        // 일기 주인에게 알림
        sendNotificationIfNeeded(user, diary.getUser(), diary, "COMMENT",
                user.getNickname() + "님이 당신의 일기에 댓글을 남겼습니다.");

        return commentRepository.save(Comment.createComment(user, diary, commentDto.getComment()));
    }

    // 대댓글 등록
    public Comment saveReply(String userId, ReplyDto replyDto) {
        Long findDiaryId = replyDto.getDiaryId();
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("유저를 찾을수 없습니다."));
        Diary diary = diaryRepository.findById(findDiaryId).orElseThrow(()->new RuntimeException("일기를 찾을수 없습니다."));
        Comment parentComment = commentRepository.findById(replyDto.getParentCommentId()).get();
        //log.info("parentComment = {}", parentComment);

        // 일기 주인에게 알림
        sendNotificationIfNeeded(user, diary.getUser(), diary, "REPLY",
                user.getNickname() + "님이 당신의 일기에 대댓글을 남겼습니다.");

        // 대댓글이면서 원댓글 작성자와도 다르면
        sendNotificationIfNeeded(user, parentComment.getUser(), diary, "REPLY",
                user.getNickname() + "님이 당신의 댓글에 대댓글을 남겼습니다.");

        return commentRepository.save(Comment.createReply(user , diary ,replyDto.getComment() , parentComment));
    }

    private void sendNotificationIfNeeded(User sender, User receiver, Diary diary, String type, String message) {
        if (sender.getId().equals(receiver.getId())) return;

        Notification notification = new Notification();
        notification.setReceiver(receiver);
        notification.setType(type);
        notification.setSenderId(sender.getId());
        notification.setMessage(message);
        notification.setReferenceId(diary.getId().toString());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
        log.info("[알림 저장 완료] type={}, from={}, to={}, diaryId={}", type, sender.getId(), receiver.getId(), diary.getId());

        String profileImage = sender.getAvatar();
        String diaryThumbnail = diary.getImageList().isEmpty() ? null : diary.getImageList().get(0);
        String moveToWhere = "/diary/" + diary.getId();

        NotificationDTO dto = NotificationDTO.from(notification, profileImage, diaryThumbnail, moveToWhere);

        notificationService.sendNotification(receiver.getId(), dto);
        log.info("[SSE 알림 전송 완료] to={}, type={}", receiver.getId(), type);
    }

    public int commentCountByDiaryId(Long diaryId){
        return commentRepository.countByDiary_Id(diaryId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findAllByDiaryId(Long diaryId) {
        return commentRepository.findAllByDiaryId(diaryId);
    }

}
