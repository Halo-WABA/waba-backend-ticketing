package com.festimap.tiketing.global.testsupport;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
public class TestController {

    @GetMapping("/api/test/security")
    public ResponseEntity<String> securityTest(){
        return ResponseEntity.ok("테스트");
    }
}
