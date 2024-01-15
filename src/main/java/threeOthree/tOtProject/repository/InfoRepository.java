package threeOthree.tOtProject.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import threeOthree.tOtProject.domain.info.IncomeDeduction;
import threeOthree.tOtProject.domain.info.MemberInfo;
import threeOthree.tOtProject.domain.info.Salary;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InfoRepository {

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public void saveInfo(MemberInfo memberInfo){em.persist(memberInfo);}
    @Transactional
    public void saveIncomeDeduction(IncomeDeduction incomeDeduction){em.persist(incomeDeduction);}
    @Transactional
    public void saveSalary(Salary salary){em.persist(salary);}


}
