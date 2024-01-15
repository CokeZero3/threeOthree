package threeOthree.tOtProject.domain.info;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import threeOthree.tOtProject.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//소득공제
@Entity
@Getter @Setter
@Table(name="income_deduction")
public class IncomeDeduction {
    @Id
    @GeneratedValue
    @Column(name = "inc_ded_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member  member;

    int amount;//금액
    String incomeDiv;//소득구분
    @ColumnDefault("0")
    double totalAmount;//총납입금액


    public static List<IncomeDeduction> dummyData(){

        List<IncomeDeduction> incomeDeductionList = new ArrayList<>();
        IncomeDeduction incomeDeduction = new IncomeDeduction();

        incomeDeduction.setIncomeDiv("보험료");
        incomeDeduction.setAmount(100000);
        incomeDeductionList.add(0,incomeDeduction);
        incomeDeduction.setIncomeDiv("교육비");
        incomeDeduction.setAmount(200000);
        incomeDeductionList.add(1,incomeDeduction);
        incomeDeduction.setIncomeDiv("기부금");
        incomeDeduction.setAmount(150000);
        incomeDeductionList.add(2,incomeDeduction);
        incomeDeduction.setIncomeDiv("의료비");
        incomeDeduction.setAmount(700000);
        incomeDeductionList.add(3,incomeDeduction);
        incomeDeduction.setIncomeDiv("퇴직연금");
        incomeDeduction.setTotalAmount(1333333.333);
        incomeDeductionList.add(4,incomeDeduction);

        return incomeDeductionList;

    }
}
