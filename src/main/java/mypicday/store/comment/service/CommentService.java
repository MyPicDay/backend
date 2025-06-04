package mypicday.store.comment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.comment.dto.request.CommentDto;
import mypicday.store.comment.dto.request.ReplyDto;
import mypicday.store.comment.entity.Comment;
import mypicday.store.comment.repository.CommentRepository;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("유저를 찾을수 없습니다."));
        Diary diary = diaryRepository.findById(findDiaryId).orElseThrow(()->new RuntimeException("일기를 찾을수 없습니다."));
        Comment.createComment(user, diary, commentDto.getComment());
        return commentRepository.save(Comment.createComment(user, diary, commentDto.getComment()));
    }

    // 대댓글 등록
    public Comment saveReply(String userId, ReplyDto replyDto) {
        Long findDiaryId = replyDto.getDiaryId();
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("유저를 찾을수 없습니다."));
        Diary diary = diaryRepository.findById(findDiaryId).orElseThrow(()->new RuntimeException("일기를 찾을수 없습니다."));
        Comment parentComment = commentRepository.findById(replyDto.getParentCommentId()).get();
        //log.info("parentComment = {}", parentComment);

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
