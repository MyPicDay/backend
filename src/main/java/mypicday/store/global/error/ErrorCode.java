package mypicday.store.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : jieun
 * @packageName : mypicday.store.global.error
 * @fileName : Error
 * @description :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 5. 19.        jieun       최초 생성 (전역 예외 Enum 메시지)
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /*Diary*/
    DIARY_NOT_FOUND(404, "해당 일기 목록이 존재하지 않습니다.")
    /**/
    ;
    private final int code;
    private final String message;
}
