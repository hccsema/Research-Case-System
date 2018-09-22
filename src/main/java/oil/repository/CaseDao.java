package oil.repository;

import oil.model.Case;
import oil.model.Tag;
import oil.model.Type;
import oil.repository.result.DayAndCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

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

    /**
     * 根据日期归档统计
     * @return
     */
    @Query(value = "SELECT DATE_FORMAT(`date`,'%Y-%m') days,COUNT(*) as count FROM oil.oil_case GROUP BY days;" ,nativeQuery = true)
    Collection<DayAndCount> getCountByDate();

    @Query(value = "SELECT * FROM oil.oil_case WHERE DATE_FORMAT(`date`,'%Y-%m')=DATE_FORMAT(?1,'%Y-%m')",nativeQuery = true)
    ArrayList<Case> findByDate(Date date);
}
