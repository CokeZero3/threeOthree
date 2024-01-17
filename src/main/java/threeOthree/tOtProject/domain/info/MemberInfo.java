package threeOthree.tOtProject.domain.info;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import threeOthree.tOtProject.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@Table(name="member_info")
public class MemberInfo {
    @Id
    @Column(name="info_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String userId;

    private int calcTaxAmount;//산출세액

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Salary> memberSalaryList;//급여 리스트


    @OneToMany(mappedBy = "member",  cascade = CascadeType.ALL)
    private List<IncomeDeduction> incomeDeductionList;//소득공제 리스트

    private String company;//회사

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "Asia/Seoul")
    private LocalDateTime workerResDt;//workerResDt

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "Asia/Seoul")
    private LocalDateTime workerReqDt;//workerReqDt

    private String status;
    private String appVer;//버전
    private String errMsg;//에러메세지
    private String svcCd;//svcCd
    private String hostNm;//hostNm



    //생성 메서드
    public static MemberInfo dummyData(Member member){
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setUserId(member.getUserId());
        memberInfo.setCalcTaxAmount(600000);
        memberInfo.setAppVer("2021112501");
        memberInfo.setCompany("삼쩜삼");
        memberInfo.setSvcCd("test01");
        memberInfo.setHostNm("jobis-codetest");
        memberInfo.setWorkerResDt(LocalDateTime.parse("2022-08-16T06:27:35.160789"));
        memberInfo.setWorkerReqDt(LocalDateTime.parse("2022-08-16T06:27:35.160789"));
        memberInfo.setMember(member);

        List<IncomeDeduction> incomeDeductionList = new ArrayList<>();
        IncomeDeduction incomeDeduction = new IncomeDeduction();
        incomeDeduction.setIncomeDiv("보험료");
        incomeDeduction.setAmount(100000);
        incomeDeduction.setMember(member);
        incomeDeductionList.add(incomeDeduction);
        IncomeDeduction incomeDeduction1 = new IncomeDeduction();
        incomeDeduction1.setIncomeDiv("교육비");
        incomeDeduction1.setAmount(200000);
        incomeDeduction1.setMember(member);
        incomeDeductionList.add(incomeDeduction1);
        IncomeDeduction incomeDeduction2 = new IncomeDeduction();
        incomeDeduction2.setIncomeDiv("기부금");
        incomeDeduction2.setAmount(150000);
        incomeDeduction2.setMember(member);
        incomeDeductionList.add(incomeDeduction2);
        IncomeDeduction incomeDeduction3 = new IncomeDeduction();
        incomeDeduction3.setIncomeDiv("의료비");
        incomeDeduction3.setAmount(700000);
        incomeDeduction3.setMember(member);
        incomeDeductionList.add(incomeDeduction3);
        IncomeDeduction incomeDeduction4 = new IncomeDeduction();
        incomeDeduction4.setIncomeDiv("퇴직연금");
        incomeDeduction4.setTotalAmount(1333333.333);
        incomeDeduction4.setMember(member);
        incomeDeductionList.add(incomeDeduction4);

        List<Salary> salaryList = new ArrayList<>();
        Salary salary = new Salary();

        salary.setUserId(member.getUserId());
        salary.setIncomeDetails("급여");
        salary.setTotalIncome(30000000);
        salary.setWorkStartDate("20221002");//20221002
        salary.setCompany("(주)활빈당");
        salary.setName(member.getName());
        salary.setPayDate("20201102");
        salary.setWorkEndDate("20211102");
        salary.setRegNo(member.getRegNo());
        salary.setIncomeDiv("근로소득(연간)");
        salary.setBusinessRegNo("012-34-56789");
        salary.setMember(member);

        salaryList.add(salary);

        memberInfo.setMemberSalaryList(salaryList);
        memberInfo.setIncomeDeductionList(incomeDeductionList);

        return memberInfo;
    }

}
