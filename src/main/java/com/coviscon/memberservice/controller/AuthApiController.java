package com.coviscon.memberservice.controller;

import com.coviscon.memberservice.dto.MemberRequestDto;
import com.coviscon.memberservice.service.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthApiController {

    private final AuthService authService;
    private final Environment env;

    @GetMapping("/member-emails/{email}/exist")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.validateEmail(email));
    }

    @GetMapping("/member-nicknames/{nickName}/exist")
    public ResponseEntity<Boolean> checkNickNameDuplicate(@PathVariable String nickName) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.validateNickName(nickName));
    }

}
