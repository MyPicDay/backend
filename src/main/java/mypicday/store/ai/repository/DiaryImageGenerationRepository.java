package mypicday.store.ai.repository;

import mypicday.store.ai.entity.DiaryImageGeneration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryImageGenerationRepository extends JpaRepository<DiaryImageGeneration, Long> {

    List<DiaryImageGeneration> findByDiaryIdIsNull();

    Optional<DiaryImageGeneration> findByUserIdAndFilePath(String userId, String filePath);
}
