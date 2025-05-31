package mypicday.store.diary.mapper;

import lombok.RequiredArgsConstructor;
import mypicday.store.diary.dto.response.DiaryDetailResponseDTO;
import mypicday.store.diary.entity.Diary;
import mypicday.store.global.dto.RequestMetaInfo;
import mypicday.store.global.util.ImagePathToUrlConverter;
import mypicday.store.user.dto.response.UserInfoDTO;
import mypicday.store.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DiaryMapper {

    private final ImagePathToUrlConverter converter;

    public DiaryDetailResponseDTO toDiaryDetailResponseDTO(Diary diary, User user, int commentCount, RequestMetaInfo requestMetaInfo) {
        return DiaryDetailResponseDTO.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .visibility(diary.getStatus())
                .imageUrls(diary.getImageList().stream()
                        .map(imageName -> converter.diaryImageUrl(imageName, requestMetaInfo))
                        .collect(Collectors.toList()))
                .commentCount(commentCount)
                .author(
                        new UserInfoDTO(
                                user.getId(),
                                user.getNickname(),
                                converter.userAvatarImageUrl(user.getAvatar(), requestMetaInfo)
                        )
                ).build();
    }
}

