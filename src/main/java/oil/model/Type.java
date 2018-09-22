package oil.model;

import lombok.Data;

import javax.persistence.*;
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
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private Boolean isExist;
    private Integer grade;
    @OrderBy("date")
    @OneToMany
    private List<Case> cases=new ArrayList<>();
}
