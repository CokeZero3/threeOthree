package threeOthree.tOtProject.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

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
