package mypicday.store.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DiaryDto {

    private String title;
    private String content ;
    private String visibility;
    private List<String> allImages ;

}
