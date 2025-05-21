package mypicday.store.diary.exception;

import mypicday.store.global.error.ErrorCode;
import mypicday.store.global.exception.BusinessException;

/**
 * @author : User
 * @packageName : mypicday.diary.exception
 * @fileName : DiaryNotFoundException
 * @description : 일기 조회 실패 커스텀 예외
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 5. 19.        User       최초 생성
 */
public class DiaryNotFoundException extends BusinessException {
    public DiaryNotFoundException() {
        super(ErrorCode.DIARY_NOT_FOUND);
    }
}
