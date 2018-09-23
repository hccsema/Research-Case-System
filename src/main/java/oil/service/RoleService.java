package oil.service;


import oil.model.Role;
import oil.repository.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    public Role findById(int id){
        return roleDao.findById(id);
    }

    @Cacheable(value = "RoleService_getRole")
    public Role getRole(String role){
        return roleDao.getFirstByRoleContaining(role);
    }

    @CacheEvict(value = "RoleService_getRole")
    public void save(Role role){
        roleDao.save(role);
    }
}
