package mypicday.store.follow.service;

import lombok.RequiredArgsConstructor;
import mypicday.store.follow.dto.UserProfileDTO;
import mypicday.store.follow.entity.Follow;
import mypicday.store.follow.repository.FollowRepository;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public void follow(String myId, String targetId) {

        User me = userRepository.findById(myId)
                .orElseThrow(() -> new RuntimeException("내 계정 없음"));
        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("대상 계정 없음"));

        boolean alreadyFollowing = followRepository.existsByFollowerAndFollowing(me, target);
        if(alreadyFollowing) {
            throw new IllegalStateException("이미 팔로우중입니다.");
        }
        if(targetId.equals(myId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우 할 수 없습니다.");
        }
//        데이터 베이스에서 myId에 해당하는 팔로우 대상 유저를 조회
        if (!followRepository.existsByFollowerAndFollowing(me, target)) {
            followRepository.save(new Follow(me, target));
        }
    }

    public void unfollow(String myId, String targetId) {
        User me = userRepository.findById(myId)
                .orElseThrow(() -> new RuntimeException("내 계정 없음"));
        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("대상 계정 없음"));

        followRepository.deleteByFollowerAndFollowing(me, target);
    }

    public List<UserProfileDTO> getFollowers(@PathVariable String userId) {
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음")) ;

        List<Follow> followers = followRepository.findByFollower_Id(userId);

        return followers.stream()
                .map(f -> {
                    User followed = f.getFollower();
                    return new UserProfileDTO(
                            followed.getId(),
                            followed.getNickname(),
                            followed.getEmail(),
                            followed.getAvatar(),
                            true  // 내가 팔로우한 사람이므로 항상 true
                    );
                })
                .collect(Collectors.toList());
    }

    public List<UserProfileDTO> getFollowings(String userId, String loginUserId) {
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음")) ;

        // userId가 팔로우한 유저들의 ID 목록 조회
        List<Follow> followingUserIds = followRepository.findByFollowing_Id(userId);

        return followingUserIds.stream()
                .map(f -> {
                    User followed = f.getFollowing();
                    return new UserProfileDTO(
                            followed.getId(),
                            followed.getNickname(),
                            followed.getEmail(),
                            followed.getAvatar(),
                            true  // 내가 팔로우한 사람이므로 항상 true
                    );
                })
                .collect(Collectors.toList());
    }
}

