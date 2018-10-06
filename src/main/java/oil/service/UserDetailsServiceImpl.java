package oil.service;


import oil.model.User;
import oil.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    public User findByUserName(String username){
        return userDao.findByUserName(username);
    }




    public User save(User user){return userDao.save(user);}

    /**
     * 用户登录认证
     * @param s
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByUserName(s);
        if(user == null){

            throw new UsernameNotFoundException("user not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),user.isEnabled(),user.isAccountNonExpired(),true,user.getNonLocked(),user.getAuthorities());
    }


    public User findById(int id){
        return userDao.findById(id);
    }

    public void saveAll(Iterable<User> list ){
        userDao.saveAll(list);
    }

    public Iterable<User> findAll(){
        return userDao.findAll();
    }

    public void delete(User user){
        userDao.delete(user);
    }
}
