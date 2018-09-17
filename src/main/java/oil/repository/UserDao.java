package oil.repository;


import oil.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedList;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
public interface UserDao extends CrudRepository<User,Integer> {
    /**
     * 通过用户名查找用户
     * @param userName
     * @return
     */
     User findByUserName(String userName);

    /**
     *
     * @param id
     * @return
     */
    User findById(int id);

    /**
     * 通过邮箱查找
     * @param email
     * @return
     */
    User findUserByEmail(String email);
}
