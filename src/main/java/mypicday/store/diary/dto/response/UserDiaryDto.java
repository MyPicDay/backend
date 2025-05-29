package mypicday.store.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mypicday.store.diary.entity.Visibility;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDiaryDto {

    private String title ;
    private String content ;
    private Visibility status ;
    private List<String> imagesList ;
}
