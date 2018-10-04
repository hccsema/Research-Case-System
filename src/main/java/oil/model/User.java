package oil.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by  waiter on 18-9-17  下午6:25.
 *
 * @author waiter
 */
@Entity
@Data
public class User implements UserDetails , Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nickName;
    private String userName;
    @JsonIgnore
    private String passWord;
    private String email;
    private Date lastLoginDate;
    private Date thisLoginDate;
    private String lastLoginIp;
    private String thisLoginIp;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    /**
     * 是否过期false为已过期
     */
    private Boolean nonExpired;
    /**
     * 是否锁定false为已锁定
     */
    private Boolean nonLocked;


    /**
     * 权限列表
     */
    @ManyToMany(targetEntity = Role.class,cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Role> authorities ;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passWord;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return nonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", email='" + email + '\'' +
                ", lastLoginDate=" + lastLoginDate +
                ", thisLoginDate=" + thisLoginDate +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", thisLoginIp='" + thisLoginIp + '\'' +
                ", nonExpired=" + nonExpired +
                ", nonLocked=" + nonLocked +
                ", authorities=" + authorities +
                '}';
    }
}

enum Sex{
    男,女
}