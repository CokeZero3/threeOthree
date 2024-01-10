package threeOthree.tOtProject.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    public void save(Member member){em.persist(em);}

    public List<Member> findMember(Member member) {
        return em.createQuery("select m from Member m " +
                        "where m.userId = :userId " +
                        "and m.password = :password" +
                        "and m.name = :name" +
                        "and m.regNo = :regNo")
                .setParameter("userId", member.getUserId())
                .setParameter("password", member.getPassword())
                .setParameter("name", member.getName())
                .setParameter("regNo", member.getRegNo())
                .getResultList();
    }

}
