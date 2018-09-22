package oil.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by  waiter on 18-9-18  上午11:05.
 *
 * @author waiter
 */
@Entity
@Data
public class Doc implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String path;
    private Date uploadDate;
    private Long downCount;
    @ManyToOne
    private Case aCase;
}
