package oil.service;

import oil.model.Case;
import oil.model.Tag;
import oil.model.Type;
import oil.repository.CaseDao;
import oil.repository.result.DayAndCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * Created by  waiter on 18-9-20  下午2:31.
 *
 * @author waiter
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CaseService {
    @Autowired
    private CaseDao caseDao;

    public Page<Case> recovery(int page){
        PageRequest date = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("date")));

        return caseDao.findAllByIsExist(false,date);
    }

    @Cacheable(value = "CaseService_findAllByType")
    public Page<Case> findAllByType(int page, Type type){
        PageRequest date = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("date")));
        return caseDao.findAllByTypeAndIsExist(date,type,true);
    }

    @Cacheable(value = "CaseService_findAllByTagsContaining")
    public Page<Case> findAllByTagsContaining(int page , Tag tag){
        PageRequest date = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("date")));
        return caseDao.findAllByTagsContainingAndIsExist(date,tag,true);
    }

    @Cacheable(value = "CaseService_findAllByTagsContaining")
    public ArrayList<Case> findAllByTagsContaining( Tag tag){
        return caseDao.findAllByTagsContainingAndIsExist(tag,true);
    }

    public ArrayList<Case> search(String search){
        return caseDao.search(search);
    }

    @Cacheable(value = "CaseService_findById")
    public Case findById(Long id){
        return caseDao.findFirstById(id);
    }

    @Cacheable(value = "CaseService_getCountByDate")
    public Collection<DayAndCount> getCountByDate(){
        return caseDao.getCountByDate();
    }

    @Cacheable(value = "CaseService_getCasesByDate")
    public Page<Case> getCasesByDate(Date date,Integer page){
        PageRequest request = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("date")));
        return caseDao.findByDate(date,request);
    }

    @CacheEvict(value = {"CaseService_findAllByType",
                        "CaseService_findById",
                        "CaseService_findAllByTagsContaining",
                        "CaseService_getCountByDate",
                        "CaseService_getCasesByDate",
                        "CaseService_findTop10ByTypeAndIsExistOrderByTimes"})
    public void save(Case cases){
        caseDao.save(cases);
    }

    @CacheEvict(value = {"CaseService_findAllByType",
                        "CaseService_findById",
                        "CaseService_findAllByTagsContaining",
                        "CaseService_getCountByDate",
                        "CaseService_getCasesByDate",
                        "CaseService_findTop10ByTypeAndIsExistOrderByTimes"},allEntries=true)
    public void delete(Case c){
        caseDao.delete(c);
    }

    @CacheEvict(value = "CaseService_findById",allEntries=true)
    public void changTimes(Case cases){
        caseDao.save(cases);
    }

    @Cacheable(value = "CaseService_findTop10ByTypeAndIsExistOrderByTimes")
    public List<Case> findTop10ByTypeAndIsExistOrderByTimes(Type type){
        return caseDao.findTop10ByTypeAndIsExistOrderByTimesDesc(type,true);
    }

    public List<Case> findAllByIsExist(Boolean b){
        return caseDao.findAllByIsExist(b);
    }

}
