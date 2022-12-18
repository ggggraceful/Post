package com.hello.post00.controller;

import com.hello.post00.dto.request.LoginRequestDto;
import com.hello.post00.dto.request.MemberRequestDto;
import com.hello.post00.dto.response.ResponseDto;
import com.hello.post00.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    //회원가입
    @PostMapping("/api/member/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid MemberRequestDto memberRequestDto){
        return memberService.registerUser(memberRequestDto);
    }

    //로그인
    @PostMapping("/api/member/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse){
        return memberService.login(loginRequestDto, httpServletResponse);

    }
}
