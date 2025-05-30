package mypicday.store.user.service;

import lombok.RequiredArgsConstructor;
import mypicday.store.diary.service.DiaryService;
import mypicday.store.user.dto.response.ProfileUserInfoDTO;
import mypicday.store.user.entity.User;
import mypicday.store.user.exception.UserNotFoundException;
import mypicday.store.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final DiaryService diaryService;

    public ProfileUserInfoDTO getProfileCounts(String userId) {
        int diaryCount = diaryService.countUserDiaries(userId);
        // TODO: 팔로워 및 팔로잉 수 추후 구현
        int followerCount = 0;
        int followingCount = 0;

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new ProfileUserInfoDTO(userId, diaryCount, followerCount, followingCount, user.getNickname(), user.getAvatar(), user.getEmail());
        }

        throw new UserNotFoundException(userId);
    }

}
