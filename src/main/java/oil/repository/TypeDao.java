package oil.repository;

import oil.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

/**
 * Created by  waiter on 18-9-19  下午12:22.
 *
 * @author waiter
 */
public interface TypeDao extends JpaRepository<Type,Integer> {
    ArrayList<Type> findAllByIsExistOrderByGradeAsc(boolean b);
}
