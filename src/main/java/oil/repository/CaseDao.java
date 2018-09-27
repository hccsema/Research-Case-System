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
    Page<Case> findAllByTypeAndIsExist(Pageable pageable, Type type,Boolean b);

    List<Case> findTop10ByTypeAndIsExistOrderByTimesDesc(Type type,Boolean b);

    /**
     * 通过类别查找前8个
     * @param type
     * @return
     */
    ArrayList<Case> findTop8ByTypeAndIsExist(Type type,Boolean b);

    /**
     * 通过标签查找
     * @param pageable
     * @param tag
     * @return
     */
    Page<Case> findAllByTagsContainingAndIsExist(Pageable pageable, Tag tag,Boolean b );

    ArrayList<Case> findAllByTagsContainingAndIsExist( Tag tag,Boolean b );

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
    @Query(value = "SELECT DATE_FORMAT(`date`,'%Y%m') days,COUNT(*) as count FROM oil.oil_case where `is_exist`=true GROUP BY days" ,nativeQuery = true)
    Collection<DayAndCount> getCountByDate();

    /**
     * 根据月分页查询
     * @param date
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM oil.oil_case WHERE DATE_FORMAT(`date`,'%Y-%m')=DATE_FORMAT(?1,'%Y-%m') AND `is_exist`=true ",
            countQuery = "SELECT count(*) FROM oil.oil_case WHERE DATE_FORMAT(`date`,'%Y-%m')=DATE_FORMAT(?1,'%Y-%m') AND `is_exist`=true",
            nativeQuery = true)
    Page<Case> findByDate(Date date,Pageable pageable);

    @Query(value = "SELECT c FROM Case c where " +
            "c.name like %?1% or " +
            "c.introduction like %?1% or "+
            "c.author like %?1% or " +
            "c.direction like %?1% or " +
            "c.summary like %?1%")
    ArrayList<Case> search(String search);
}
