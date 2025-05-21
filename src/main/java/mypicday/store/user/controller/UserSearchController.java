package mypicday.store.user.controller;

import lombok.RequiredArgsConstructor;
import mypicday.store.user.dto.response.UserSearchResponse;
import mypicday.store.user.service.UserSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : jieun
 * @packageName : mypicday.store.user.controller
 * @fileName : UserSearchController
 * @description :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 5. 20.        jieun      유저 검색 컨트롤러 최초 생성 (채연님 작업 중으로 혼선 방지를 위해 UserController -> UserSearchController로 명명)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserSearchController {
    private final UserSearchService userSearchService;

    @GetMapping
    public ResponseEntity<Page<UserSearchResponse>> searchUsersByNickname(@RequestParam String nickname, Pageable pageable) {
        Page<UserSearchResponse> response = userSearchService.findByNicknameContaining(nickname, pageable);
        return ResponseEntity.ok(response);
    }
}
