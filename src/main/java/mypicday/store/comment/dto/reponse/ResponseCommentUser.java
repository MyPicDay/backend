package mypicday.store.comment.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCommentUser {

    private Long id ;
    private String name ;
    private String avatar ;

    private LocalDate date ;
}
