package oil.repository;

import oil.model.Case;
import oil.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  waiter on 18-9-19  下午12:18.
 *
 * @author waiter
 */
public interface CaseDao extends JpaRepository<Case,Long> {
    Page<Case> findAllByType(Pageable pageable, Type type);
    Case findFirstById(Long id);
}
