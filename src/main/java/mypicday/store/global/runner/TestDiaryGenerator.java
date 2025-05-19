package mypicday.store.global.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.entity.Status;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
 @Component
 @Profile("dev")
 @RequiredArgsConstructor
 public class TestDiaryGenerator implements ApplicationRunner { // 클래스 이름 변경
     private final DiaryRepository diaryRepository;
     private final UserRepository userRepository;

     @Override
     public void run(ApplicationArguments args) {
         List<User> users = IntStream.range(0, 3).mapToObj(i -> userRepository.save(new User(null, "test"))).toList();
         List<Diary> diaries = IntStream.range(0, 3)
                 .mapToObj(i -> new Diary(users.get(i), "제목", "내용", Status.PUBLIC, null, null, null)).toList();
         diaryRepository.saveAll(diaries);
     }
 }

