package mypicday.store.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.comment.dto.CommentDto;
import mypicday.store.comment.entity.Comment;
import mypicday.store.comment.repository.CommentRepository;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final DiaryRepository diaryRepository;

   // 댓글 등록
    public Comment saveComment(CommentDto commentDto ,String userId) {
        log.info("commentDto = {}", commentDto);
        Long findDiaryId = commentDto.getDiaryId();
        User user = userRepository.findById(userId).get();
        Diary diary = diaryRepository.findById(findDiaryId).get();
        Comment.createComment(user, diary, commentDto.getComment());
        return commentRepository.save(Comment.createComment(user, diary, commentDto.getComment()));
    }

    // 대댓글 등록
    public Comment saveReply(String userId, CommentDto commentDto ,  Long commentId) {
        Long findDiaryId = commentDto.getDiaryId();
        User user = userRepository.findById(userId).get();
        Diary diary = diaryRepository.findById(findDiaryId).get();
        Comment Parent = commentRepository.findById(commentId).get();
        return commentRepository.save( Comment.createReply(user , diary , commentDto.getComment() , Parent));
    }

}
