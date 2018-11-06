package oil.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  waiter on 18-9-18  上午10:43.
 *
 * @author waiter
 */
@Entity
@Data
@Table(name = "oil_type")
public class Type implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private Boolean isExist;
    /**
     * 优先级
     */
    private Integer grade;
    @OrderBy("date desc ")
    @OneToMany(mappedBy = "type",fetch = FetchType.EAGER)
    private List<Case> cases=new ArrayList<>();

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isExist=" + isExist +
                ", grade=" + grade +
                '}';
    }
}
