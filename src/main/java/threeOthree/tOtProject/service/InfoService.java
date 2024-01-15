package threeOthree.tOtProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import threeOthree.tOtProject.domain.Member;
import threeOthree.tOtProject.domain.info.IncomeDeduction;
import threeOthree.tOtProject.domain.info.MemberInfo;
import threeOthree.tOtProject.repository.InfoRepository;
import threeOthree.tOtProject.repository.MemberRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InfoService {

    @Autowired
    private final InfoRepository infoRepository;
    private final MemberRepository memberRepository;
    public void saveDummyData(String userId){
        MemberInfo memberInfo = new MemberInfo();
        memberInfo = MemberInfo.dummyData(userId);

        //List<Member> memberData = memberRepository.findById(userId);

        infoRepository.saveInfo(memberInfo);
        infoRepository.saveSalary(memberInfo.getMemberSalaryList().get(0));


        for(IncomeDeduction incomeDeduction : memberInfo.getIncomeDeductionList()){
            //incomeDeduction.setMember(memberData.get(0));
            infoRepository.saveIncomeDeduction(incomeDeduction);
        }

    }
}
