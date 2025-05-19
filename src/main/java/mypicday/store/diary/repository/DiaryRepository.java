package mypicday.store.diary.repository;

import mypicday.store.diary.entity.Diary;
import org.springframework.data.repository.CrudRepository;

public interface DiaryRepository extends CrudRepository<Diary, Long> {

}
