package mypicday.store.diary.service;

import mypicday.store.comment.entity.Comment;
import mypicday.store.comment.service.CommentService;
import mypicday.store.diary.dto.response.CommentDto;
import mypicday.store.diary.dto.response.DiaryDetailResponseDTO;
import mypicday.store.diary.dto.response.DiaryResponse;
import mypicday.store.diary.mapper.DiaryMapper;
import mypicday.store.global.dto.RequestMetaInfo;
import mypicday.store.likedUser.service.LikedUserService;
import org.springframework.http.ResponseEntity;
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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final LikedUserService likedUserService;
    private final UserRepository userRepository;
    private final CommentService commentService;
    private final DiaryMapper diaryMapper;

    public int countUserDiaries(String userId) {
        return diaryRepository.countByUser_Id(userId);
    }

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



    public Long findLikeCount(Long diaryId){
        Optional<Diary> findLike = diaryRepository.findById(diaryId);
        return findLike.map(diary -> diary.getLike().getCount()).orElse(0L);
    }

    @Transactional(readOnly = true)
    public UserDiaryDto findUserDiary(String userId , LocalDate date ) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        Optional<Diary> diary = diaryRepository.findUserIdAndCreatedAt(userId, startOfDay, endOfDay);

        if (diary.isPresent()) {
            return  new UserDiaryDto(diary.get().getTitle() , diary.get().getContent() , diary.get().getStatus() , diary.get().getImageList());
        }
        return new UserDiaryDto();

    }

    public boolean updateDiary(String userId , DiaryDto diaryDto) {
        LocalDateTime startOfDay = diaryDto.getDate().atStartOfDay();

        LocalDateTime endOfDay = diaryDto.getDate().atTime(23, 59, 59);
        Optional<Diary> diary = diaryRepository.findUserIdAndCreatedAt(userId, startOfDay, endOfDay);
        diary.ifPresent(value -> value.update(diaryDto.getTitle() , diaryDto.getContent() , Visibility.valueOf(diaryDto.getVisibility().toUpperCase()) , diaryDto.getAllImages()));
        if (diary.isEmpty()) {
            return true ;
        }
        return false;
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


    public List<DiaryResponse> findMonthlyDiaries(String userId, int year, int month) {
        // 해당 연월의 시작과 끝 날짜 계산
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        // 해당 월의 다이어리들을 조회하고 DiaryResponse로 변환하여 반환
            List<Diary> diaries = diaryRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, startOfMonth, endOfMonth);

        return diaries.stream()
                .map(diary -> new DiaryResponse(
                        diary.getTitle(),
                        diary.getId(),
                        diary.getStatus(),
                        diary.getContent(),
                        diary.getUser().getNickname(),
                        diary.getImageList(),
                        diary.getComments().size(),
                        diary.getCreatedAt().toLocalDate()))
                .collect(Collectors.toList());
    }

    public DiaryDetailResponseDTO getDiaryDetail(String userId ,Long diaryId, RequestMetaInfo metaInfo) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new IllegalArgumentException("Diary not found"));
        User user = diary.getUser();
        log.info("user.getName {}", user.getNickname());
        LikeEntity like = diary.getLike();
        Long likeId = like.getId();
        boolean liked = likedUserService.findLike(userId , likeId);
        int commentCount = commentService.commentCountByDiaryId(diaryId);
        List<Comment> comments = commentService.findAllByDiaryId(diaryId);
        DiaryDetailResponseDTO diaryDetailResponseDTO = diaryMapper.toDiaryDetailResponseDTO(diary, user, commentCount, metaInfo , liked ,comments);
        return diaryDetailResponseDTO;
    }


}