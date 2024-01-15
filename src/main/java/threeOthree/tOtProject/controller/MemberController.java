package threeOthree.tOtProject.controller;


import antlr.Token;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import threeOthree.tOtProject.domain.Member;
import threeOthree.tOtProject.security.Service.SecurityUserService;
import threeOthree.tOtProject.security.jwt.JwtAuthenticationFilter;
import threeOthree.tOtProject.security.jwt.JwtTokenProvider;
import threeOthree.tOtProject.service.InfoService;
import threeOthree.tOtProject.service.MemberService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 회원가입 컨트롤러
 * */
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final InfoService infoService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "test", notes = "test")
    @PostMapping("/szs/test")
    public ResponseEntity<Integer> add (
            @ApiParam(value="1번 값", required = true, example = "1")
            @RequestParam(value="num1", required = true) int num1,
            @ApiParam(value="2번 값", required = true, example = "2")
            @RequestParam(value="num2", required = true)int num2
    ) {
        int sum = num1 + num2;

        return ResponseEntity.ok(sum);
    }

    @ApiOperation(value="회원가입", notes="회원가입 기능")
    @PostMapping("/szs/signup")
    public ResponseEntity<Member> save(@ApiParam(example = "member") Member member) {
        //암호화
        log.info("1.패스워드 인코딩 전:: "+member.getPassword());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        log.info("2.패스워드 인코딩 후:: "+member.getPassword());
        return new ResponseEntity<Member>(memberService.join(member), HttpStatus.OK);
    }

    @ApiOperation(value="로그인", notes="로그인 기능")
    @PostMapping("/szs/login")
    public ResponseEntity<String> login(
            @ApiParam(value="아이디",required = true, example = "userId")
            @RequestParam(value="userId", required = true) String userId,
            @ApiParam(value="비밀번호",required = true, example = "password")
            @RequestParam(value="password", required = true) String password) {

        String jwt = "Bearer JwtToken " + memberService.login(userId, password);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, jwt);

        return new ResponseEntity<String>(jwt,httpHeaders, HttpStatus.OK);
    }

    @ApiOperation(value="토큰발급확인", notes="토큰 기능")
    @GetMapping("/szs/me")
    public ResponseEntity<Boolean> token(
            @ApiParam(value="토큰",required = true, example = "token")
            @RequestParam(value="token", required = true) String token){

        boolean validateToken = jwtTokenProvider.validateToken(token);
        System.out.println("validateToken = " + validateToken);
        boolean validateToken2 = jwtTokenProvider.validateToken(token);
        log.info("validateToken2 = " + validateToken2);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer JwtToken " + token);

        return new ResponseEntity<Boolean> (validateToken2, httpHeaders, HttpStatus.OK);
    }

    @ApiOperation(value="토큰으로 정보 가져오기", notes="토큰 정보 가져오기")
    @GetMapping("/szs/scrap")
    public ResponseEntity<Map<String, Object>> tokenScrap(
            @ApiParam(value="토큰",required = true, example = "token")
            @RequestParam(value="token", required = true) String token){

        //String tokenAuth = String.valueOf(jwtTokenProvider.getAuthentication(token));
        Map<String, Object> tokenInfo = jwtTokenProvider.getUserPKList(token);
        tokenInfo.replace("regNo", memberService.decrypt((String) tokenInfo.get("regNo")));
        log.info("tokenInfo = " + tokenInfo);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer JwtToken " + token);

        return new ResponseEntity<Map<String, Object>> (tokenInfo, httpHeaders, HttpStatus.OK);
    }

    @ApiOperation(value="데이터 저장하기", notes="데이터 저장하기")
    @GetMapping("/szs/save")
    public ResponseEntity<String> save(
            @ApiParam(value="아이디",required = true, example = "userId")
            @RequestParam(value="userId", required = true) String userId){
        infoService.saveDummyData(userId);
        return new ResponseEntity<String>("", HttpStatus.OK);
    }



}
