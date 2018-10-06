package oil.service;

import oil.model.Tag;
import oil.repository.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    public List<Tag> findAll(){
        ArrayList<Tag> cases = tagDao.findAllByIsExist(true, Sort.by(Sort.Direction.DESC, "cases"));
        cases.sort(new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o1.getCases().size()>o2.getCases().size()?-1:0;
            }
        });
        if (cases.size()<11){
            return cases;
        }
        return cases.subList(0,10);
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

    public void saveAll(List<Tag> tags) {
        tagDao.saveAll(tags);
    }
}
