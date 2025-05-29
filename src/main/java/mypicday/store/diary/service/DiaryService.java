package mypicday.store.diary.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.dto.DiaryDto;
import mypicday.store.diary.dto.response.UserDiaryDto;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.entity.Visibility;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.like.entity.LikeEntity;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

        Diary diary = new Diary(diaryDto.getContent() , Visibility.valueOf(diaryDto.getVisibility().toUpperCase()) ,LikeEntity.create() ,
                diaryDto.getAllImages() ,diaryDto.getTitle(), user.get());
        return diaryRepository.save(diary);
    }

/*    public Page<DiaryResponse> findDiaries(
            final String userId,
            final Pageable pageable
    ) {
        //TODO 전체 목록 조회 정렬 조건 (1. 본인, 2. 친구, 3. 제3자)
        Page<DiaryResponse> response = diaryRepository.findAllByAuthorId(
                userId,
                pageable
        ).map(DiaryResponse::from);
        return response;
    }*/

    public Long findLikeCount(Long diaryId){
        Optional<Diary> findLike = diaryRepository.findById(diaryId);
        return findLike.map(diary -> diary.getLike().getCount()).orElse(0L);
    }

    @Transactional(readOnly = true)
    public UserDiaryDto findUserDiary(String userId , LocalDate date ) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        Optional<Diary> diary = diaryRepository.findUserIdAndCreatedAt(userId, startOfDay, endOfDay);
        return diary.map(value -> new UserDiaryDto(value.getTitle(), value.getContent(), value.getStatus(), value.getImageList())).orElseGet(UserDiaryDto::new);
    }

    public Optional<Diary> updateDiary(String userId , DiaryDto diaryDto) {
        LocalDateTime startOfDay = diaryDto.getDate().atStartOfDay();
        LocalDateTime endOfDay = diaryDto.getDate().atTime(23, 59, 59);
        Optional<Diary> diary = diaryRepository.findUserIdAndCreatedAt(userId, startOfDay, endOfDay);
        diary.ifPresent(value -> value.update(diaryDto.getTitle() , diaryDto.getContent() , Visibility.valueOf(diaryDto.getVisibility().toUpperCase()) , diaryDto.getAllImages()));
        return diary ;
    }
    @Transactional(readOnly = true)
    public List<Diary> findAllDiaries() {
        return diaryRepository.findAllDiaries();
    }

    @Transactional(readOnly = true)
    public List<Diary> findAllComments(Long diaryId) {
        return diaryRepository.findAllComments(diaryId);
    }

    @Transactional(readOnly = true)
    public List<Diary> findAllReplies(Long diaryId) {
        return diaryRepository.findAllReplies(diaryId);
    }
}
