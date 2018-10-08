package oil.service;

import oil.model.Doc;
import oil.repository.DocDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  waiter on 18-9-20  下午2:40.
 *
 * @author waiter
 */
@Service
public class DocService {
    @Autowired
    private DocDao docDao;

    @CacheEvict(value = "DocService_findById",allEntries=true)
    public void save(Doc doc){
        docDao.save(doc);
    }

    public ArrayList<Doc> search(String search){
        return docDao.findAllByNameContainingOrPathContaining(search,search);
    }

    @CacheEvict(value = "DocService_findById",allEntries=true)
    public void remove(Doc doc){
        docDao.delete(doc);
    }

    @Cacheable(value = "DocService_findById")
    public Doc findById(Long id){
        return docDao.getOne(id);
    }

    @CacheEvict(value = "DocService_findById",allEntries=true)
    public void saveAll(List<Doc> docs){
        docDao.saveAll(docs);
    }
}
