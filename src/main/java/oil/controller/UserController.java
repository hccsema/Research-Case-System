package oil.controller;

import oil.model.Role;
import oil.model.User;
import oil.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

/**
 * Created by  waiter on 18-9-17  下午7:40.
 *
 * @author waiter
 */
@Controller
public class UserController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @ResponseBody
    @GetMapping(value = "/add/user")
    public User addUser(){
        User user = new User();
        user.setUserName("123456");
        user.setPassWord("123456");
        user.setNonExpired(true);
        user.setNonLocked(true);
        user.setNickName("测试");
        ArrayList<Role> objects = new ArrayList<>();
        objects.add(new Role("ROLE_USER"));
        user.setAuthorities(objects);
        return userDetailsService.save(user);
    }
}
