package mypicday.store.follow.service;

import lombok.RequiredArgsConstructor;
import mypicday.store.follow.entity.Follow;
import mypicday.store.follow.repository.FollowRepository;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.stereotype.Service;

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
        //데이터 베이스에서 myId에 해당하는 팔로우 대상 유저를 조회
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
}

