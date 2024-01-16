package threeOthree.tOtProject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import threeOthree.tOtProject.domain.Member;
import threeOthree.tOtProject.domain.Refund;
import threeOthree.tOtProject.domain.info.IncomeDeduction;
import threeOthree.tOtProject.domain.info.MemberInfo;
import threeOthree.tOtProject.domain.info.Salary;
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
    public void saveDummyData(Member member){
        MemberInfo memberInfo = new MemberInfo();
        memberInfo = MemberInfo.dummyData(member);

        //List<Member> memberData = memberRepository.findById(userId);

        infoRepository.saveInfo(memberInfo);
        infoRepository.saveSalary(memberInfo.getMemberSalaryList().get(0));


        for(IncomeDeduction incomeDeduction : memberInfo.getIncomeDeductionList()){
            //incomeDeduction.setMember(memberData.get(0));
            infoRepository.saveIncomeDeduction(incomeDeduction);
        }
    }

    public List<MemberInfo> findMemberInfo(Member member){
        List<MemberInfo> memberInfos = infoRepository.findMemberInfo(member);
        List<IncomeDeduction> incomeDeductionList = infoRepository.findIncomeDeduction(member);
        List<Salary> salaries = infoRepository.findSalary(member);
        System.out.println("salaries = " + salaries);
        List<IncomeDeduction> incomeDeductions = infoRepository.findIncomeDeduction(member);
        System.out.println("incomeDeductions = " + incomeDeductions);
        memberInfos.get(0).setIncomeDeductionList(incomeDeductions);
        memberInfos.get(0).setMemberSalaryList(salaries);

        return  memberInfos;
    }

    public List<IncomeDeduction> findIncomeDeduction(Member member){
        return  infoRepository.findIncomeDeduction(member);
    }

    public List<Salary> findSalary(Member member){
        return  infoRepository.findSalary(member);
    }

    public Refund calculateTaxAmount(MemberInfo memberInfo){
        Refund refund = new Refund();
        List<IncomeDeduction> incomeDeductionList = memberInfo.getIncomeDeductionList();//소득공제
        List<Salary> salaryList = memberInfo.getMemberSalaryList(); //급여

        int taxAmount = 0; //결정세액

        int calcTaxAmount = memberInfo.getCalcTaxAmount(); //산출세액
        double incomTaxCredit = calcTaxAmount*.55; //근로소득세액공제금액 = 산출세액 * 0.55

        double retCredit = 0; //퇴직연금세액공제금액 = 퇴직연금 납입금액 * 0.15
        double insCredit = 0; //보험료공제금액 = 보험료납입금액 * 12%
        double medCredit = 0; //의료비공제금액 = (의료비납입금액 - 총급여 * 3%) * 15%
        double eduCredit = 0; //교육비공제금액 = 교육비납입금액 * 15%
        double donationCredit = 0; //기부금공제금액 = 기부금납입금액 * 15%

        int totalIncome = 0; //총급여

        if(!salaryList.isEmpty()){
            totalIncome = salaryList.get(0).getTotalIncome();
        }

        for(IncomeDeduction incomeDeduction : incomeDeductionList){
            switch (incomeDeduction.getIncomeDiv()){
                case "보험료" : insCredit = incomeDeduction.getAmount(); break;
                case "의료비" : medCredit = incomeDeduction.getAmount(); break;
                case "교육비" : eduCredit = incomeDeduction.getAmount(); break;
                case "기부금" : donationCredit = incomeDeduction.getAmount(); break;
                case "퇴직연금" : retCredit = incomeDeduction.getTotalAmount(); break;
            }

        }
        if(insCredit!=0){insCredit = insCredit*.12;} //보험료공제금액 = 보험료납입금액 * 12%

        if(medCredit<0){medCredit=0;} //의료비공제금액 < 0 일 경우, 의료비공제금액 = 0 처리 한다.
        else{medCredit = (medCredit - totalIncome * .03) * .15;};//의료비공제금액 = (의료비납입금액 - 총급여 * 3%) * 15%

        if(eduCredit!=0){eduCredit = eduCredit*.15;}//교육비공제금액 = 교육비납입금액 * 15%

        if(donationCredit!=0){donationCredit = donationCredit*.15;}//기부금공제금액 = 기부금납입금액 * 15%

        if(retCredit!=0){retCredit = retCredit*.15;} //퇴직연금세액공제금액 = 퇴직연금 납입금액 * 0.15

        //특별세액공제금액 = 보험료공제금액, 의료비공제금액, 교육비공제금액, 기부금공제금액
        int specialCredit = (int) (insCredit + medCredit + eduCredit + donationCredit);
        System.out.println("specialCredit = " + specialCredit);
        /**
        표준세액공제금액
        특별세액공제금액의 합 < 130,000 이면 표준세액공제금액 = 130,000원
        특별세액공제금액의 합 >= 130,000 이면 표준세액공제금액 = 0
        단, 표준세액공제금액 = 130,000원이면 특별세액공제금액 = 0 처리 한다.
        결정세액 < 0 인 경우, 결정세액 = 0 처리 한다.
        */

        int standardTax = 0;//표준세액공제금액

        if(specialCredit < 130000){
            standardTax = 130000;
        } else if(specialCredit >= 130000){
            standardTax = 0;
        }

        if(standardTax == 130000){
            specialCredit = 0;
        }

        //결정세액 = 산출세액 - 근로소득세액공제금액 - 특별세액공제금액 - 표준세액공제금액 - 퇴직연금세액공제금액;
        taxAmount = (int) (calcTaxAmount - incomTaxCredit - specialCredit - standardTax - retCredit);
        System.out.println("taxAmount = " + taxAmount);
        if(taxAmount<0){
            taxAmount=0;
        }

        refund.setName(salaryList.get(0).getName());
        refund.setTotalTaxAmount(taxAmount);
        refund.setPensionTaxCredit((int) retCredit);
        System.out.println("standardTax = " + standardTax);

        return refund;
    }
}
