package mypicday.store.diary.repository;

import mypicday.store.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT d.like.count FROM Diary d WHERE d.id = :diaryId")
    long LikeCountByDiaryId(@Param("diaryId") Long diaryId);

}
