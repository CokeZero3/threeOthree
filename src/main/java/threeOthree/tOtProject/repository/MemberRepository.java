package threeOthree.tOtProject.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import threeOthree.tOtProject.domain.Member;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    @PersistenceContext
    private final EntityManager em;

    //저장
    public void save(Member member){em.persist(member);}

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findMember(Member member) {
        return em.createQuery("select m from Member m where m.name = :name and m.regNo = :regNo")
                .setParameter("name", member.getName())
                .setParameter("regNo", member.getRegNo())
                .getResultList();
    }

    public List<Member> login(String userId, String password) {
        return em.createQuery("select m from Member m where m.userId = :userId and m.password = :password")
                .setParameter("userId", userId)
                .setParameter("password", password)
                .getResultList();
    }

}
