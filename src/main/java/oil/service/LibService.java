package oil.service;

import oil.model.Lib;
import oil.repository.LibDao;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Lib getLib(){
        ArrayList<Lib> all = libDao.findAll();
        return all.size()>0?all.get(0):null;
    }

    public void save(Lib lib){
        libDao.save(lib);
    }

}
