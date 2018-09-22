package oil.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 *
 * 用户权限
 */
@Entity
@Table(name = "role")
@Data
public class Role implements GrantedAuthority , Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id ;
    /**
     * 用户权限
     *
     */
    private String role;
    Role(){}

    public Role(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }


}
