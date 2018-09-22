package oil.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  waiter on 18-9-18  上午10:47.
 *
 * @author waiter
 */
@Entity
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private Boolean isExist;
    @OrderBy("date")
    @ManyToMany
    private List<Case> cases = new ArrayList<>();
}
