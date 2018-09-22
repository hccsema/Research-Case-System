package oil.service;

import oil.model.Tag;
import oil.repository.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by  waiter on 18-9-20  下午1:53.
 *
 * @author waiter
 */
@Service
public class TagService {
    @Autowired
    private TagDao tagDao;

    @Cacheable(value = "findAll")
    public ArrayList<Tag> findAll(){
        return tagDao.findAllByIsExistOrderByCases(true);
    }

    @CacheEvict(value = "findAll")
    public void save(Tag tag){
        tagDao.save(tag);
    }
}
