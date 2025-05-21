package mypicday.store.likedUser.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import mypicday.store.diary.repository.DiaryRepository;

import mypicday.store.like.entity.LikeEntity;
import mypicday.store.likedUser.Dto.LikedDto;
import mypicday.store.likedUser.entity.LikeStatus;
import mypicday.store.likedUser.entity.LikedUser;
import mypicday.store.likedUser.repository.LikedUserRepository;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LikedUserService {

    private final DiaryRepository diaryRepository ;

    private final UserRepository userRepository;

    private final LikedUserRepository likedUserRepository;

    public void LikedUser(String userId, LikedDto likedDto) {

        User findUser = userRepository.findById(userId).get();
        LikeEntity findLike = diaryRepository.findById(likedDto.getDiaryId()).get().getLike();

        Optional<LikedUser> findLikedUser = likedUserRepository.findByUserAndLike(findUser, findLike);
        System.out.println(findLikedUser.isPresent());
        if (findLikedUser.isPresent()) {
            if (likedDto.isLiked()) {
                findLike.decreaseCount();
                findLikedUser.get().changeLiked();
            }
            else{
                findLike.increaseCount();
                findLikedUser.get().changeLiked();
            }

        }
        else {
            findLike.increaseCount();
            LikedUser likedUser = LikedUser.of(false, findUser, findLike);
            likedUserRepository.save(likedUser);
        }

    }
    
}
