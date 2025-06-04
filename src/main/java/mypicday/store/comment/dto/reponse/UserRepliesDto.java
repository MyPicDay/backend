package mypicday.store.comment.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class UserRepliesDto {
    private ResponseUserDto responseUserDto ;
    private List<ResponseReplyDto> comments ;


}
