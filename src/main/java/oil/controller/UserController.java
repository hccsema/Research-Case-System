package oil.controller;

import oil.model.Role;
import oil.model.User;
import oil.service.RoleService;
import oil.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

/**
 * Created by  waiter on 18-9-17  下午7:40.
 *
 * @author waiter
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RoleService roleService;

    @PostMapping(value = "/add/{userName}")
    public String addUser(){
        return "";
    }

}
