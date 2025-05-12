// package mypicday.store.global.runner;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.boot.ApplicationArguments;
// import org.springframework.boot.ApplicationRunner;
// import org.springframework.context.annotation.Profile;
// import org.springframework.stereotype.Component;
// import mypicday.store.user.entity.User;
// import mypicday.store.user.repository.UserRepository;
//
// import java.util.Arrays;
// import java.util.List;
//
// @Component
// @Profile("dev") // "dev" 프로필에서만 실행
// @RequiredArgsConstructor
// @Slf4j
// public class TestUserGenerator implements ApplicationRunner { // 클래스 이름 변경
//
//     private final UserRepository userRepository;
//     private static final String TEST_USER_JOB_MARKER = "test_user_job_marker";
//     private static final String DEFAULT_PROFILE_IMAGE_URL = "/images/default_profile.png";
//     private static final String DEFAULT_PSYCHOLOGICAL_RESULT = "{\"MBTI\":\"INTP\"}";
//
//     @Override
//     public void run(ApplicationArguments args) {
//         List<User> existingUserWithMarker = userRepository.findAllByJob(TEST_USER_JOB_MARKER);
//
//         if (existingUserWithMarker.isEmpty() || existingUserWithMarker.size() < 2) {
//             List<User> testUsersToCreate = Arrays.asList(
//                 new User(
//                     DEFAULT_PROFILE_IMAGE_URL,
//                     30,
//                     "Male",
//                     "Seoul",
//                     "None",
//                     5000,
//                     TEST_USER_JOB_MARKER,
//                     DEFAULT_PSYCHOLOGICAL_RESULT,
//                     175.0,
//                     70.0
//                 ),
//                 new User(
//                     DEFAULT_PROFILE_IMAGE_URL,
//                     20, // 나이 수정됨 (기존 코드 반영)
//                     "Female",
//                     "Seoul",
//                     "None",
//                     3000,
//                     TEST_USER_JOB_MARKER, // 동일한 직업 마커 사용
//                     DEFAULT_PSYCHOLOGICAL_RESULT,
//                     175.0, // 키, 몸무게는 첫 번째 사용자와 동일 (기존 코드 반영)
//                     70.0
//                 )
//                     // 필요하다면 여기에 더 많은 테스트 사용자 추가
//             );
//
//         userRepository.saveAll(testUsersToCreate);
//         log.info("DEV Profile: 테스트 사용자 (직업 마커: '{}') {}명이 생성되었습니다.", TEST_USER_JOB_MARKER, testUsersToCreate.size());
//         return;
//         }
//         log.info("DEV Profile: 테스트 사용자 (직업 마커: '{}')가 이미 존재하여 생성하지 않았습니다.", TEST_USER_JOB_MARKER);
//     }
// }