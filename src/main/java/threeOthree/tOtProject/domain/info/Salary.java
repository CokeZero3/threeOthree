package threeOthree.tOtProject.domain.info;

import lombok.Getter;
import lombok.Setter;
import threeOthree.tOtProject.domain.Member;

import javax.persistence.*;

@Entity
@Getter@Setter
@Table(name = "salary")
public class Salary {

    @Id @GeneratedValue
    @Column(name="salary_id")
    private Long Id;

    @JoinColumn(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String incomeDetails;//소득내역
    private int totalIncome;//총지급액
    private String workStartDate;//업무시작일
    private String company;//기업명
    private String name;//이름
    private String payDate;//지급일
    private String workEndDate;//업무종료일
    private String regNo;//주민번호
    private String incomeDiv;//소득구분
    private String businessRegNo;//사업자번호
}
