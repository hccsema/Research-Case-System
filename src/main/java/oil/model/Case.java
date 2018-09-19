package oil.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by  waiter on 18-9-18  上午10:55.
 *
 * @author waiter
 */
@Entity
@Data
@Table(name = "oil_case")
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(length = 500)
    private String introduction;
    @ManyToOne
    private Type type;
    private Boolean isExist;
    private Date date;
    private Long times;
    private String libId;
    private String field;
    private String direction;
    private String course;
    private String author;
    private String unit;
    @ManyToMany
    private List<Tag> tags=new ArrayList<>();
    @Column(length = 500)
    private String summary;
    @OneToMany
    private List<Doc> contents=new ArrayList<>();
    @OneToMany
    private List<Doc> solves=new ArrayList<>();
}
