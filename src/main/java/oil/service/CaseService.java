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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;


/**
 * Created by  waiter on 18-9-20  下午2:31.
 *
 * @author waiter
 */
@Service
public class CaseService {
    @Autowired
    private CaseDao caseDao;

    @Cacheable(value = "CaseService_findAllByType")
    public Page<Case> findAllByType(int page, Type type){
        PageRequest date = PageRequest.of(page, 20, Sort.by(Sort.Order.desc("date")));
        return caseDao.findAllByTypeAndIsExist(date,type,true);
    }

    @Cacheable(value = "CaseService_findAllByTagsContaining")
    public Page<Case> findAllByTagsContaining(int page , Tag tag){
        PageRequest date = PageRequest.of(page, 20, Sort.by(Sort.Order.desc("date")));
        return caseDao.findAllByTagsContainingAndAndIsExist(date,tag,true);
    }


    @Cacheable(value = "findById")
    public Case findById(Long id){
        return caseDao.findFirstById(id);
    }

    @Cacheable(value = "getCountByDate")
    public Collection<DayAndCount> getCountByDate(){
        return caseDao.getCountByDate();
    }


    @CacheEvict(value = {"findAllByType",
                        "findById",
                        "findAllByTagsContaining",
                        "getCountByDate"})
    public void save(Case cases){
        caseDao.save(cases);
    }

}
