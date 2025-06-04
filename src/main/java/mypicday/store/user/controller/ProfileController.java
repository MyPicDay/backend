// package mypicday.store.user.controller;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import mypicday.store.user.dto.response.ProfileUserInfoDTO;
// import mypicday.store.user.service.ProfileService;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// @Slf4j
// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/api/profiles")
// public class ProfileController {
//
//     private final ProfileService profileService;
//
//     @GetMapping("{userId}")
//     public ResponseEntity<ProfileUserInfoDTO> getProfile(@PathVariable String userId) {
//         ProfileUserInfoDTO profileUserInfoDTO = profileService.getProfileCounts(userId);
//         return ResponseEntity.ok(profileUserInfoDTO);
//     }
// }
