package mypicday.store.likedUser.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.repository.DiaryRepository;

import mypicday.store.like.entity.LikeEntity;
import mypicday.store.likedUser.Dto.LikedDto;
import mypicday.store.likedUser.entity.LikedUser;
import mypicday.store.likedUser.repository.LikedUserRepository;
import mypicday.store.notification.dto.NotificationDTO;
import mypicday.store.notification.entity.Notification;
import mypicday.store.notification.repository.NotificationRepository;
import mypicday.store.notification.service.NotificationService;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LikedUserService {

    private final DiaryRepository diaryRepository ;
    private final UserRepository userRepository;
    private final LikedUserRepository likedUserRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    public void LikedUser(String userId, LikedDto likedDto) {

        User findUser = userRepository.findById(userId).get();
        log.info("likedDto: {}", likedDto);
        LikeEntity findLike = diaryRepository.findById(likedDto.getDiaryId()).get().getLike();
        Diary diary = diaryRepository.findById(likedDto.getDiaryId()).get();

        Optional<LikedUser> findLikedUser = likedUserRepository.findByUserAndLike(findUser, findLike);
        boolean isLiking = likedDto.isLiked();
        if (findLikedUser.isPresent()) {
            if (likedDto.isLiked()) {
                findLike.increaseCount();
                findLikedUser.get().changeLiked();
            }
            else{
                findLike.decreaseCount();
                findLikedUser.get().changeLiked();
            }

        }
        else {
            findLike.increaseCount();
            LikedUser likedUser = LikedUser.of(true, findUser, findLike);
            likedUserRepository.save(likedUser);
        }

        // 알림 로직
        if (isLiking && !findUser.getId().equals(diary.getUser().getId())) {
            log.info("[알림 생성] 사용자 '{}'가 일기 ID={}에 좋아요를 눌렀습니다.", findUser.getId(), diary.getId());

            Notification notification = new Notification();
            notification.setReceiver(diary.getUser()); // 알림 받을 사람
            notification.setType("LIKE");
            notification.setSenderId(findUser.getId());
            notification.setMessage(findUser.getNickname() + "님이 당신의 일기에 좋아요를 눌렀습니다.");
            notification.setReferenceId(diary.getId().toString());
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
            log.info("[알림 저장 완료] 알림이 DB에 저장되었습니다. 수신자={}, 알림ID={}", diary.getUser().getId(), notification.getId());

            String profileImage = findUser.getAvatar();
            String diaryThumbnail = diary.getImageList().isEmpty() ? null : diary.getImageList().get(0);
            String moveToWhere = "/diary/" + diary.getId();

            NotificationDTO dto = NotificationDTO.from(notification, profileImage, diaryThumbnail, moveToWhere);
            log.debug("[알림 DTO 생성] 알림 DTO 생성 완료: moveToWhere={}", moveToWhere);

            notificationService.sendNotification(diary.getUser().getId(), dto);
            log.info("[알림 전송] 사용자 '{}'에게 SSE 알림을 전송했습니다.", diary.getUser().getId());
        }
    }
    public boolean findLike(String userId , long likedId) {
        Optional<Boolean> likedByUserId = likedUserRepository.findLikedByUserId(userId , likedId);
        if(likedByUserId.isPresent()) {
            log.info("likedByUserId {}", likedByUserId.get());
            return likedByUserId.get() ;

        }
        return false ;
    }
}
