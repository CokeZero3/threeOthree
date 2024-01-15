package threeOthree.tOtProject.security.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import threeOthree.tOtProject.domain.Member;
import threeOthree.tOtProject.repository.MemberRepository;
import threeOthree.tOtProject.security.jwt.SecurityUser;

import java.util.List;

@Service
public class SecurityUserService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = new Member();
        member.setName(username);
        List<Member> optional = memberRepository.findById(member.getUserId());
        if(!optional.isEmpty()) {
            throw new UsernameNotFoundException(username + " 사용자 없음");
        } else {
            return new SecurityUser(member);
        }

    }

}
