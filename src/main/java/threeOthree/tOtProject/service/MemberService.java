package threeOthree.tOtProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import threeOthree.tOtProject.domain.Member;
import threeOthree.tOtProject.repository.MemberRepository;
import threeOthree.tOtProject.security.jwt.JwtTokenProvider;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

/**
 * 회원가입 Service
 * */
@Service("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public Member join(Member member){

        validateAuthorizedMember(member); //허가된 아이디 validation
        validateDuplicateMember(member);  //중복 회원 validation
        validateDuplicateId(member);      //중복 아이디 validation

        log.info("3.주민번호 인코딩 전 = " + member.getRegNo());
        member.setRegNo(passwordEncoder.encode(member.getRegNo()));
        log.info("4.주민번호 인코딩 후 = " + member.getRegNo());

        memberRepository.save(member);
        return member;
    }

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    /**
     * VALIDATIONS
     */
    //로그인 validation
    public String login(String userId, String password){
        List<Member> loginMember = memberRepository.login(userId);
        log.info("loginMember = " + loginMember.get(0).getPassword());
        log.info("password = " + password);

        boolean loginChk = checkEncoding(password, loginMember.get(0).getPassword());
        log.info("loginChk = " + loginChk);
        if(!loginChk){
            throw new IllegalStateException(" 아이디(로그인 전용 아이디) 또는 비밀번호를 잘못 입력했습니다.\n" +
                    "입력하신 내용을 다시 확인해주세요.");
        }

        return jwtTokenProvider.createToken(loginMember.get(0).getUserId());
    }

    //회원가입 validations
    public void validateDuplicateMember(Member member){
        List<Member> memberList = memberRepository.findMember(member);
        Boolean checkRegNo = null;
        for(Member mem : memberList){
            checkRegNo = checkEncoding(member.getRegNo(), mem.getRegNo());
        }

        if(!memberList.isEmpty() || Boolean.TRUE.equals(checkRegNo)){
            throw new IllegalStateException("이미 존재하는 맴버입니다.");
        }
    }

    //아이디 중복 체크
    public void validateDuplicateId(Member member){
        List<Member> memberList = memberRepository.findId(member);

        if(!memberList.isEmpty()){
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    //회원가입 validations
    public void validateAuthorizedMember(Member member){
        List<String> authListName = new ArrayList<>();
        List<String> authListRegNo = new ArrayList<>();

        boolean check = false;

        authListName.add(0, "홍길동"); authListRegNo.add(0, "860824-1655068");
        authListName.add(1, "김둘리"); authListRegNo.add(1, "921108-1582816");
        authListName.add(2, "마징가"); authListRegNo.add(2, "880601-2455116");
        authListName.add(3, "베지터"); authListRegNo.add(3, "910411-1656116");
        authListName.add(4, "손오공"); authListRegNo.add(4, "820326-2715702");

        log.info("Member.getName ::"+member.getName());
        log.info("Member.getRegNo ::"+member.getRegNo());

        //회원 가능한 유저 check
        for(int i=0;i<authListName.size();i++){
            if ((authListName.get(i).equals(member.getName()) && authListRegNo.get(i).equals(member.getRegNo()))) {
                check = true;
                break;
            }
        }

        log.info("check = "+check);
        if(!check){
            throw new IllegalStateException("허가 명단에 없는 이름과 주민번호입니다.");
        }
    }

    public boolean checkEncoding(String decode, String encode){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(decode, encode);
    }

}
