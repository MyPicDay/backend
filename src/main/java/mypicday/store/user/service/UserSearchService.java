package mypicday.store.user.service;

import lombok.RequiredArgsConstructor;
import mypicday.store.user.dto.response.UserSearchResponse;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author : jieun
 * @packageName : mypicday.store.user.service
 * @fileName : UserSearchService
 * @description :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 5. 20.        jieun       최초 생성
 */

//TODO 현재 유저 검색 한정으로 user 패키지 내부 검색 기능 구현,검색 범위 확장 시, search 전용 패키지 분리 후 검색 구조 리팩터링 예정
@Service
@RequiredArgsConstructor
public class UserSearchService {
    private final UserRepository userRepository;

    public Page<UserSearchResponse> findByNicknameContaining(String nickname, Pageable pageable) {
        Page<User> users = userRepository.findByNicknameContaining(nickname, pageable);
        return users.map(UserSearchResponse::from);
    }
}
