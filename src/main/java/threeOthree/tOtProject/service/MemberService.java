package threeOthree.tOtProject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import threeOthree.tOtProject.domain.Member;
import threeOthree.tOtProject.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원가입 Serivce
 * */

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;

    //회원가입
    @Transactional
    public Member join(Member member){
        validateAuthorizedMember(member);
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member;
    }

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    //회원가입 validations
    public void validateDuplicateMember(Member member){
        List<Member> memberList = memberRepository.findMember(member);
        if(!memberList.isEmpty()){
            throw new IllegalStateException("이미 존재하는 맴버입니다.");
        }
    }

    //회원가입 validations
    public void validateAuthorizedMember(Member member){
        List<String> authListName = new ArrayList<>();
        List<String> authListRegNo = new ArrayList<>();

        boolean check = false;

        authListName.add(0, "홍길동");authListRegNo.add(0, "860824-1655068");
        authListName.add(1, "김둘리");authListRegNo.add(1, "921108-1582816");
        authListName.add(2, "마징가");authListRegNo.add(2, "880601-2455116");
        authListName.add(3, "베지터");authListRegNo.add(3, "910411-1656116");
        authListName.add(4, "손오공");authListRegNo.add(4, "820326-2715702");

        System.out.println("Member.getName ::"+member.getName());
        System.out.println("Member.getRegNo ::"+member.getRegNo());

        //회원 가능한 유저 check
        for(int i=0;i<authListName.size();i++){
            if ((authListName.get(i).equals(member.getName()) && authListRegNo.get(i).equals(member.getRegNo()))) {
                check = true;
                break;
            }
        }

        System.out.println("check = "+check);
        if(!check){
            throw new IllegalStateException("허가 명단에 없는 이름과 주민번호입니다.");
        }
    }

    //로그인 validation
    public boolean login(String userId, String password){
        List<Member> login = memberRepository.login(userId, password);
        boolean loginCheck = login.size() == 1;
        if(!loginCheck){
            throw new IllegalStateException(" 아이디(로그인 전용 아이디) 또는 비밀번호를 잘못 입력했습니다.\n" +
                    "입력하신 내용을 다시 확인해주세요.");
        }
        return loginCheck;
    }

}
