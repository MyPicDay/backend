package mypicday.store.diary.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.dto.DiaryDto;
import mypicday.store.diary.dto.response.DiaryResponse;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.entity.Visibility;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.like.entity.LikeEntity;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final UserRepository userRepository;

    public Diary save(String userID , DiaryDto diaryDto) {
        Optional<User> user = userRepository.findById(userID);

        if (user.isEmpty()) {
            return null;
        }
        log.info("Visibility.valueOf(diaryDto.getVisibility().toUpperCase()) ={}", Visibility.valueOf(diaryDto.getVisibility().toUpperCase()));

        return diaryRepository.save(new Diary(diaryDto.getContent() , Visibility.valueOf(diaryDto.getVisibility().toUpperCase()) ,LikeEntity.create() ,
                       diaryDto.getAllImages() ,diaryDto.getTitle(), user.get()));
    }

    public Page<DiaryResponse> findDiaries(
            final String userId,
            final Pageable pageable
    ) {
        //TODO 전체 목록 조회 정렬 조건 (1. 본인, 2. 친구, 3. 제3자)
        Page<DiaryResponse> response = diaryRepository.findAllByAuthorId(
                userId,
                pageable
        ).map(DiaryResponse::from);
        return response;
    }

    public long findLikeCount(Long diaryId) {
        return diaryRepository.LikeCountByDiaryId(diaryId);
    }

}
