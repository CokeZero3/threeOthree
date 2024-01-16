package threeOthree.tOtProject.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import threeOthree.tOtProject.domain.Member;
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

    public List<MemberInfo> findMemberInfo(Member member){
        return em.createQuery("select m from MemberInfo m where m.member.id = :member", MemberInfo.class)
                .setParameter("member", member.getId())
                .getResultList();
    }

    public List<IncomeDeduction> findIncomeDeduction(Member member){
        return em.createQuery("select i from IncomeDeduction i where i.member.id = :member", IncomeDeduction.class)
                .setParameter("member", member.getId())
                .getResultList();
    }

    public List<Salary> findSalary(Member member){
        return em.createQuery("select s from Salary s where s.member.id = :member", Salary.class)
                .setParameter("member", member.getId())
                .getResultList();
    }


}
