package mypicday.store.diary.mapper;

import lombok.RequiredArgsConstructor;
import mypicday.store.comment.entity.Comment;
import mypicday.store.diary.dto.response.CommentDto;
import mypicday.store.diary.dto.response.DiaryDetailResponseDTO;
import mypicday.store.diary.entity.Diary;
import mypicday.store.global.dto.RequestMetaInfo;
import mypicday.store.global.util.ImagePathToUrlConverter;
import mypicday.store.user.dto.response.UserInfoDTO;
import mypicday.store.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DiaryMapper {

    private final ImagePathToUrlConverter converter;

    public DiaryDetailResponseDTO toDiaryDetailResponseDTO(
            Diary diary, User user, int commentCount,
            RequestMetaInfo requestMetaInfo, boolean like, List<Comment> comments
    ) {
        return DiaryDetailResponseDTO.builder()
                .imageUrls(diary.getImageList().stream()
                        .map(imageName -> converter.diaryImageUrl(imageName, requestMetaInfo))
                        .collect(Collectors.toList()))
                .commentCount(commentCount)
                .likeCount(diary.getLike().getCount())
                .liked(like)
                .content(diary.getContent())
                .author(new UserInfoDTO(
                        user.getId(),
                        user.getNickname(),
                        converter.userAvatarImageUrl(user.getAvatar(), requestMetaInfo)
                ))
                .comments(
                        comments.stream()
                                .map(comment -> new CommentDto(
                                        comment.getId(),
                                        comment.getParent() != null ? comment.getParent().getId() : null,
                                        comment.getUser().getNickname(),
                                        converter.userAvatarImageUrl(comment.getUser().getAvatar(), requestMetaInfo),
                                        comment.getContext(),
                                        comment.getCreatedAt()
                                ))
                                .collect(Collectors.toList())
                )
                .build();
    }
}
