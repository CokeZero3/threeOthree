package threeOthree.tOtProject.controller;


import antlr.Token;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
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
import threeOthree.tOtProject.service.MemberService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * 회원가입 컨트롤러
 * */
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
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
        String jwt = "Bearer " + memberService.login(userId, password);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<String>(jwt,httpHeaders, HttpStatus.OK);
    }

    @ApiOperation(value="토큰 발급", notes="토큰 기능")
    @GetMapping("/szs/me")
    public ResponseEntity<String> token(
            @ApiParam(value="토큰",required = true, example = "token")
            @RequestParam(value="token", required = true) String token){

        //String tokenAuth = String.valueOf(jwtTokenProvider.getAuthentication(token));
        String tokenAuth = jwtTokenProvider.getUserPK(token);
        log.info("tokenAuth = " + tokenAuth);

        boolean validateToken = jwtTokenProvider.validateToken(token);
        log.info("validateToken = " + validateToken);
        return new ResponseEntity<String> (tokenAuth, HttpStatus.OK);
    }

    @ApiOperation(value="토큰 발급", notes="토큰 기능")
    @GetMapping("/szs/me2")
    public String token(
            @ApiParam(value="토큰",required = true, example = "token")
            @RequestParam(value="token", required = true) String token,
            Model model, HttpServletResponse response,
            HttpServletRequest req){

        String tokenAuth = jwtTokenProvider.getUserPK(token);
        log.info("tokenAuth = " + tokenAuth);

        boolean validateToken = jwtTokenProvider.validateToken(token);
        log.info("validateToken = " + validateToken);

        Cookie cookie = new Cookie("jwtToken", token);
        log.info("cookie = " + cookie);
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
        log.info("response = " + response);

        String authorization = req.getHeader("Authorization");
        log.info("authorization"+authorization);
//        String accessToken = authorization.split("Bearer ")[1];
//        log.info("accessToken: "+accessToken);
        String resolveToken = jwtTokenProvider.resolveToken(req);
        log.info("resolveToken = " + resolveToken);



        return "";
    }



}
