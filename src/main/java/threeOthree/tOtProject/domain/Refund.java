package threeOthree.tOtProject.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Refund {
    private String name;
    private int totalTaxAmount; //결정세액
    private int pensionTaxCredit; //퇴직연금세액공제
}
