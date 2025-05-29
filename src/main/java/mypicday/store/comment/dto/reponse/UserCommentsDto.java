package mypicday.store.comment.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mypicday.store.comment.dto.request.CommentDto;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommentsDto {
    private String title ;
    private String content ;
    private String name ;
    private String avatar ;
    private List<ResponseCommentDto> comments ;
}
