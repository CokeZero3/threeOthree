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
        return em.createQuery("select m from Member m where m.name = :name")
                .setParameter("name", member.getName())
                .getResultList();
    }

    public List<Member> findId(Member member) {
        return em.createQuery("select m.id from Member m where m.userId = :userId")
                .setParameter("userId", member.getUserId())
                .getResultList();
    }

    public List<Member> login(String userId) {
        return em.createQuery("select m from Member m where m.userId = :userId")
                .setParameter("userId", userId)
                .getResultList();
    }

}
