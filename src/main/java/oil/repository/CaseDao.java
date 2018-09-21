package oil.repository;

import oil.model.Case;
import oil.model.Tag;
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
    /**
     * 通过类别分页查找
     * @param pageable
     * @param type
     * @return
     */
    Page<Case> findAllByType(Pageable pageable, Type type);

    /**
     * 通过类别查找前8个
     * @param type
     * @return
     */
    ArrayList<Case> findTop8ByType(Type type);

    /**
     * 通过标签查找
     * @param pageable
     * @param tag
     * @return
     */
    Page<Case> findAllByTagsContaining(Pageable pageable, Tag tag);

    /**
     * 通过id查找
     * @param id
     * @return
     */
    Case findFirstById(Long id);
}
