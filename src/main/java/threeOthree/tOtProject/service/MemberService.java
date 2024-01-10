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

    //exception
    public void validateDuplicateMember(Member member){
        List<Member> memberList = memberRepository.findMember(member);
        if(!memberList.isEmpty()){
            throw new IllegalStateException("이미 존재하는 맴버입니다.");
        }
    }

    public void validateAuthorizedMember(Member member){
        List<String> authListName = new ArrayList<>();
        List<String> authListRegNo = new ArrayList<>();

        authListName.add(0, "홍길동");authListRegNo.add(0, "860824-1655068");
        authListName.add(1, "김둘리");authListRegNo.add(1, "921108-1582816");
        authListName.add(2, "마징가");authListRegNo.add(2, "880601-2455116");
        authListName.add(3, "베지터");authListRegNo.add(3, "910411-1656116");
        authListName.add(4, "손오공");authListRegNo.add(4, "820326-2715702");

        List<Member> memberList = memberRepository.findMember(member);

        //회원 가능한 유저 check
        for(int i=0;i<authListName.size();i++){
            for(Member members : memberList){
                if( (authListName.get(i).equals(members.getName())
                        &&authListRegNo.get(i).equals(members.getRegNo())) ){
                    throw new IllegalStateException("You are not authorized to signup");
                }
            }
        }
    }

}
