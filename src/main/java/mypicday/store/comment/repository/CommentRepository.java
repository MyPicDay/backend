package mypicday.store.comment.repository;

import mypicday.store.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    int countByDiary_Id(Long diaryId);

    @Query("select c from Comment c where c.diary.id = :diaryId")
    List<Comment> findAllByDiaryId(@Param("diaryId") Long diaryId);
}
