package oil.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
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
public class Case implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(length = 500)
    private String introduction;
    @JsonIgnore
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
    @Column(length = 1000)
    private String summary;
    @JsonIgnore
    @OneToMany
    private List<Doc> contents=new ArrayList<>();
    @JsonIgnore
    @OneToMany
    private List<Doc> solves=new ArrayList<>();

    @Override
    public String toString() {
        return "Case{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", introduction='" + introduction + '\'' +
                ", type=" + type.getName() +
                ", isExist=" + isExist +
                ", date=" + date +
                ", times=" + times +
                ", libId='" + libId + '\'' +
                ", field='" + field + '\'' +
                ", direction='" + direction + '\'' +
                ", course='" + course + '\'' +
                ", author='" + author + '\'' +
                ", unit='" + unit + '\'' +
                ", summary='" + summary + '\'' +
                ", contents=" + contents +
                ", solves=" + solves +
                '}';
    }
}
