package oil.repository;

import oil.model.Doc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

/**
 * Created by  waiter on 18-9-19  下午12:20.
 *
 * @author waiter
 */
public interface DocDao extends JpaRepository<Doc,Long> {
}
