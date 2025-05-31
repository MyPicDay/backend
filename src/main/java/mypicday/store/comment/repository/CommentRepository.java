package mypicday.store.comment.repository;

import mypicday.store.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    int countByDiary_Id(Long diaryId);
}
