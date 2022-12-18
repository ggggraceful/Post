package com.hello.post00.service;


import com.hello.post00.dto.TokenDto;
import com.hello.post00.dto.request.LoginRequestDto;
import com.hello.post00.dto.request.MemberRequestDto;
import com.hello.post00.dto.response.MemberResponseDto;
import com.hello.post00.dto.response.ResponseDto;
import com.hello.post00.entity.Member;
import com.hello.post00.entity.RefreshToken;
import com.hello.post00.exception.entityException.memberException.BadPasswordConfirmException;
import com.hello.post00.exception.entityException.memberException.BadPasswordException;
import com.hello.post00.exception.entityException.memberException.DuplicateMemberException;
import com.hello.post00.exception.entityException.memberException.MemberNotFoundException;
import com.hello.post00.jwt.provider.JwtProvider;
import com.hello.post00.repository.MemberRepository;
import com.hello.post00.repository.RefreshTokenRopository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRopository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;


    //가입한 회원인지 아닌지 유효성 검사해주는 method
    public Member isPresentMember(String username){
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        return optionalMember.orElse(null);
    }
    //회원가입
    @Transactional
    public ResponseEntity<?> registerUser(MemberRequestDto memberRequestDto){

        // 중복처리
        if(null != isPresentMember(memberRequestDto.getUsername())){
            throw new DuplicateMemberException();
//            return ResponseDto.fail("DUPLICATED_NICKNAME","중복된 닉네임 입니다.");
        }

        // 비밀번호 확인
        if(!memberRequestDto.getPassword().equals(memberRequestDto.getPasswordConfirm())){
            throw new BadPasswordConfirmException();
//                    return ResponseDto.fail("PASSWORDS_NOT_MATCHED","비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .username(memberRequestDto.getUsername())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .build();
        memberRepository.save(member);

        // 성공시 나타나는 부분
        return ResponseEntity.ok().body(ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        ));
    }

    //로그인
    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        Member member = isPresentMember(loginRequestDto.getUsername());

        // 존재하는 사용자인지
        if(null == member){
            throw new MemberNotFoundException();
//            return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
        }
        // 비밀번호가 맞는지 확인
        if(!member.validatePassword(passwordEncoder, loginRequestDto.getPassword())){
            throw new BadPasswordException();
//            return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을수 없습니다.");
        }


        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = jwtProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        httpServletResponse.addHeader("Access-Token", tokenDto.getGrantType() + " " + tokenDto.getAccessToken());
        httpServletResponse.addHeader("Refresh-Token", tokenDto.getRefreshToken());

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }
}
