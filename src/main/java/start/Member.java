package start;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Entity@Table(name="member")
@Getter@Setter
public class Member {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String username;

    private Integer age;

}
