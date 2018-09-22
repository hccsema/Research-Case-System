package oil.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  waiter on 18-9-18  上午10:47.
 *
 * @author waiter
 */
@Entity
@Data
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private Boolean isExist;
    @OrderBy("date asc ")
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Case> cases = new ArrayList<>();
}
