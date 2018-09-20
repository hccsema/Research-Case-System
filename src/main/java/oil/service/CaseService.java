package oil.service;

import oil.model.Case;
import oil.model.Type;
import oil.repository.CaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by  waiter on 18-9-20  下午2:31.
 *
 * @author waiter
 */
@Service
public class CaseService {
    @Autowired
    private CaseDao caseDao;

    public Page<Case> findAllByType(int page, Type type){
        PageRequest date = PageRequest.of(page, 20, Sort.by(Sort.Order.desc("date")));
        return caseDao.findAllByType(date,type);
    }

    public Case findById(Long id){
        return caseDao.findFirstById(id);
    }

    public void save(Case cases){
        caseDao.save(cases);
    }

}
