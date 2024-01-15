package threeOthree.tOtProject.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
@Table(name = "member")
public class  Member{
    @Id @GeneratedValue
    @Column(name = "member_id")
    private long id;
    @Column(name="user_id")
    private String userId;
    private String name;
    private String regNo;
    private String password;
}
