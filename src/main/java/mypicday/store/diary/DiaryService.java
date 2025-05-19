package mypicday.store.diary;

import lombok.RequiredArgsConstructor;
import mypicday.store.diary.dto.response.DiaryResponse;
import mypicday.store.diary.repository.DiaryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author : User
 * @packageName : mypicday.diary.service
 * @fileName : DiaryService
 * @description :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 5. 19.        User       최초 생성
 */

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

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
}
