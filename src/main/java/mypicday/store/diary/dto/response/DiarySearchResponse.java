package mypicday.store.diary.dto.response;

import org.springframework.data.domain.Page;

/**
 * @packageName : mypicday.diary.dto.response
 * @author : User
 * @fileName : DiarySearchResponse
 * @description : 일기 Base Response DTO 
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 5. 19.        User       최초 생성 (MVP 개발 용도로 Page<T> 반환 추후 커스텀 페이징 객체로 진행 예정)
 */
public record DiarySearchResponse(
    Page<DiaryResponse> diaries
) {

}
