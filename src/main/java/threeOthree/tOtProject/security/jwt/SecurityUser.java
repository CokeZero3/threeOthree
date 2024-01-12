package threeOthree.tOtProject.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import threeOthree.tOtProject.domain.Member;

import javax.persistence.Entity;

public class SecurityUser extends User {
    private Member member;

    public SecurityUser(Member member) {
        super(member.getUserId(), member.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_MEMBER"));
        this.member = member;
    }

}
