package threeOthree.tOtProject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
public class  Member{
    @Id
    private String userId;
    private String password;
    private String name;
    private String regNo;
}
