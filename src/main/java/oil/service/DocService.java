package oil.service;

import oil.model.Doc;
import oil.repository.DocDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by  waiter on 18-9-20  下午2:40.
 *
 * @author waiter
 */
@Service
public class DocService {
    @Autowired
    private DocDao docDao;

    public void save(Doc doc){
        docDao.save(doc);
    }

    public Doc findById(Long id){
        return docDao.getOne(id);
    }
}
