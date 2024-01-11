package threeOthree.tOtProject.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import threeOthree.tOtProject.domain.Member;
import threeOthree.tOtProject.domain.Role;
import threeOthree.tOtProject.service.MemberService;

/**
 * 회원가입 컨트롤러
 * */
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

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
        System.out.println("1.패스워드 인코딩 전:: "+member.getPassword());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        System.out.println("2.패스워드 인코딩 후:: "+member.getPassword());
        member.setRole(Role.ROLE_MEMBER);//default
        return new ResponseEntity<Member>(memberService.join(member), HttpStatus.OK);
    }

    @ApiOperation(value="로그인", notes="로그인 기능")
    @PostMapping("/szs/login")
    public ResponseEntity<String> login(
            @ApiParam(value="아이디",required = true, example = "userId")
            @RequestParam(value="userId", required = true) String userId,
            @ApiParam(value="비밀번호",required = true, example = "password")
            @RequestParam(value="password", required = true) String password) {



        return new ResponseEntity<String>(memberService.login(userId, password), HttpStatus.OK);
    }


}
