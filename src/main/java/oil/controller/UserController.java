package oil.controller;

import oil.model.Role;
import oil.model.User;
import oil.service.RoleService;
import oil.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;

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

    /**
     * 添加用户
     * @param user
     * @param model
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/add")
    public String addUser(User user, Model model){
        User byUserName = userDetailsService.findByUserName(user.getUsername());
        if (byUserName!=null){
            model.addAttribute("msg","用户已存在");
        }else {
            user.setPassWord(bCryptPasswordEncoder.encode(user.getUsername()));
            Role role_user = roleService.getRole("ROLE_USER");
            LinkedList<Role> objects = new LinkedList<>();
            objects.add(role_user);
            user.setAuthorities(objects);
            user.setNonLocked(true);
            user.setNonExpired(true);
            userDetailsService.save(user);
        }
        return "";
    }

    /**
     *修改密码
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/change_pwd")
    public String changePassWd(String pwd, HttpServletRequest request,Model model){
        Assert.notNull(pwd,"密码不为空");
        Assert.hasLength(pwd,"密码不为空");
        if (pwd.length()<7){
            model.addAttribute("msg","密码长度大于6");
            return "";
        }
        String remoteUser = request.getRemoteUser();
        User byUserName = userDetailsService.findByUserName(remoteUser);
        byUserName.setPassWord(bCryptPasswordEncoder.encode(pwd));
        return "";
    }

}
