package threeOthree.tOtProject.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import threeOthree.tOtProject.Util.PasswordUtility;
import threeOthree.tOtProject.domain.Member;
import threeOthree.tOtProject.domain.Refund;
import threeOthree.tOtProject.domain.info.MemberInfo;
import threeOthree.tOtProject.security.jwt.JwtAuthenticationFilter;
import threeOthree.tOtProject.security.jwt.JwtTokenProvider;
import threeOthree.tOtProject.service.InfoService;
import threeOthree.tOtProject.service.MemberService;

import java.util.List;
import java.util.Map;

/**
 * 회원가입 컨트롤러
 * */
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final InfoService infoService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordUtility utility;

    @ApiOperation(value="1.회원가입", notes="회원가입 기능")
    @PostMapping("/szs/signup")
    public ResponseEntity<Member> save(@ApiParam(example = "member") Member member) {
        return new ResponseEntity<Member>(memberService.join(member), HttpStatus.OK);
    }

    @ApiOperation(value="2.로그인", notes="로그인 기능")
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

    @ApiOperation(value="3.토큰발급확인", notes="토큰 기능")
    @GetMapping("/szs/me")
    public ResponseEntity<Boolean> token(
            @ApiParam(value="토큰",required = true, example = "token")
            @RequestParam(value="token", required = true) String token){

        boolean validateToken = jwtTokenProvider.validateToken(token);
        boolean validateToken2 = jwtTokenProvider.validateToken(token);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer JwtToken " + token);

        return new ResponseEntity<Boolean> (validateToken2, httpHeaders, HttpStatus.OK);
    }

    @ApiOperation(value="4.가입한 유저의 정보 스크랩", notes="가입한 유저의 정보 스크랩")
    @PostMapping("/szs/scrap")
    public ResponseEntity<Map<String, Object>> tokenScrap(
            @ApiParam(value="토큰",required = true, example = "token")
            @RequestParam(value="token", required = true) String token){

        Map<String, Object> tokenInfo = jwtTokenProvider.getUserPKList(token);
        tokenInfo.replace("regNo", utility.decrypt((String) tokenInfo.get("regNo")));

        List<Member> memberList = memberService.findMemberByUserId((String) tokenInfo.get("userId"));

        List<MemberInfo> memberInfos = null;

        if(!memberList.isEmpty()){
            memberInfos = infoService.findMemberInfo(memberList.get(0));
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer JwtToken " + token);

        return new ResponseEntity<Map<String, Object>> (tokenInfo, httpHeaders, HttpStatus.OK);
    }


    @ApiOperation(value="5.결정세액/퇴직연금세액공제금액 계산", notes="결정세액/퇴직연금세액공제금액 계산")
    @GetMapping("/szs/refund")
    public ResponseEntity<Refund> save(
            @ApiParam(value="토큰",required = true, example = "token")
            @RequestParam(value="token", required = true) String token){
        int tax = 0;
        List<MemberInfo> memberInfos = null;
        Map<String, Object> tokenInfo = jwtTokenProvider.getUserPKList(token);
        List<Member> memberList = memberService.findMemberByUserId((String) tokenInfo.get("userId"));
        Refund refund = new Refund();

        if(!memberList.isEmpty()){
            memberInfos = infoService.findMemberInfo(memberList.get(0));
            refund = infoService.calculateTaxAmount(memberInfos.get(0));
        }

        return new ResponseEntity<Refund>(refund, HttpStatus.OK);
    }

}
