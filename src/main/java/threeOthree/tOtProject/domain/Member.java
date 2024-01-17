package threeOthree.tOtProject.domain;

import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "member")
public class  Member{
    @Id @GeneratedValue
    @Column(name = "member_id")
    @ApiModelProperty(hidden = true)
    private long id;
    @Column(name="user_id")
    private String userId;
    private String name;
    private String regNo;
    private String password;
}
