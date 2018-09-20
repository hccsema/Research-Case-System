package oil.repository;


import oil.model.Lib;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by  waiter on 18-9-19  下午12:21.
 *
 * @author waiter
 */
public interface LibDao extends CrudRepository<Lib,Integer> {
    @Override
    ArrayList<Lib> findAll();
}
