package mypicday.store.auth.controller;

import mypicday.store.global.config.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public ResponseEntity<CustomUserDetails> test(@AuthenticationPrincipal CustomUserDetails userDetail)  {

        System.out.println(userDetail);
        return ResponseEntity.ok().body(userDetail);
    }

    @GetMapping("/test2")
    public ResponseEntity<Void> test2(){
        return ResponseEntity.ok().build();
    }
}
