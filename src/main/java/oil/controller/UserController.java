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
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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
            Role roleUser = roleService.getRole("ROLE_USER");
            LinkedList<Role> objects = new LinkedList<>();
            objects.add(roleUser);
            user.setAuthorities(objects);
            user.setNonLocked(true);
            user.setNonExpired(true);

            userDetailsService.save(user);
            model.addAttribute("msg","添加成功");
        }
        return "admin/userAdd";
    }


    /**
     * 修改用户
     * @param user
     * @param model
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/change")
    public String changeUser(User user, Model model){
        User byId = userDetailsService.findById(user.getId());
        user.setPassWord(byId.getPassword());
        user.setNonLocked(true);
        user.setNonExpired(true);
        userDetailsService.save(user);
        model.addAttribute("msg","更新成功");
        return "user/user_info";
    }

    /**
     *修改密码
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = {"/change_pwd"})
    public String changePassWd(String oldPwd,
                               String pwd,
                               HttpServletRequest request,
                               Model model){
        Assert.notNull(pwd,"密码不为空");
        Assert.hasLength(pwd,"密码不为空");
        if (pwd.length()<7){
            model.addAttribute("msg","密码长度大于6");
            return "user/pwd_change";
        }

        String remoteUser = request.getRemoteUser();
        User byUserName = userDetailsService.findByUserName(remoteUser);
        byUserName.setPassWord(bCryptPasswordEncoder.encode(pwd));
        model.addAttribute("msg","修改成功");
        return "user/pwd_change";
        if (bCryptPasswordEncoder.matches(oldPwd,byUserName.getPassword())) {
            byUserName.setPassWord(bCryptPasswordEncoder.encode(pwd));
        }
        return "";
    }


    /**
     *修改密码,后台用
     */
    @RolesAllowed("ROLE_ADMIN")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = {"/change_pwd/{id}"})
    public String changePassWdAdmin(@PathVariable(name = "id") User user,
                               String pwd,
                               Model model){
        Assert.notNull(pwd,"密码不为空");
        Assert.hasLength(pwd,"密码不为空");
        if (pwd.length()<7){
            model.addAttribute("msg","密码长度大于6");
            return "";
        }

        user.setPassWord(bCryptPasswordEncoder.encode(pwd));
        return "";
    }

    /**
     * 获取用户列表
     * @param model
     * @return
     */
    @GetMapping(value = "/get")
    public String getAll(Model model){
        model.addAttribute("users",userDetailsService.findAll());
        return "admin/userControl";
    }

    /**
     * 锁定
     * @param user
     * @return
     */
    @RolesAllowed("ROLE_ADMIN")
    @PostMapping(value = "/lock")
    public String lock(@RequestParam(name = "id") User user) {
        user.setNonLocked(false);
        userDetailsService.save(user);
        return "";
    }

    /**
     * 解锁
     * @param user
     * @return
     */
    @RolesAllowed("ROLE_ADMIN")
    @PostMapping(value = "/unLock")
    public String unLock(@RequestParam(name = "id") User user) {
        user.setNonLocked(true);
        userDetailsService.save(user);
        return "";
    }


    /**
     * 删除
     * @param user
     * @return
     */
    @RolesAllowed("ROLE_ADMIN")
    @PostMapping(value = "/delete")
    public String delete(@RequestParam(name = "id") User user) {
        userDetailsService.delete(user);
        return "";
    }


}
