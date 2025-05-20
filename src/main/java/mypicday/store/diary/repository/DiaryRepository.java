package mypicday.store.diary.repository;

import mypicday.store.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DiaryRepository extends JpaRepository<Diary, Long> {

}
