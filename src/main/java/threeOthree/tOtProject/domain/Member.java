package threeOthree.tOtProject.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
public class  Member{
    @Id
    private String userId;
    private String name;
    private String regNo;
    private String password;
}
