package oil.service;

import oil.model.Lib;
import oil.repository.LibDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by  waiter on 18-9-19  下午7:34.
 *
 * @author waiter
 */
@Service
public class LibService {
    @Autowired
    private LibDao libDao;

    @Cacheable(value = "LibService_getLib")
    public Lib getLib(){
        ArrayList<Lib> all = libDao.findAll();
        return all.size()>0?all.get(0):null;
    }

    @CacheEvict(value = "LibService_getLib",allEntries=true)
    public void save(Lib lib){
        libDao.save(lib);
    }

}
