package oil.service;

import oil.model.Tag;
import oil.repository.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
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

    @Cacheable(value = "TagService_findAll")
    public ArrayList<Tag> findAll(){
        return tagDao.findTop10ByIsExist(true, Sort.by(Sort.Direction.DESC,"cases"));
    }

    public ArrayList<Tag> search(String search){
        return tagDao.findAllByIsExistAndNameContaining(true,search);
    }

    public Tag findByName(String name){
        return tagDao.findByName(name);
    }

    @CacheEvict(value = "TagService_findAll",allEntries=true)
    public Tag save(Tag tag){
        return tagDao.save(tag);
    }
}
