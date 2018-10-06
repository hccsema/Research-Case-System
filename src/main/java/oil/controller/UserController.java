package oil.controller;

import oil.model.Role;
import oil.model.User;
import oil.service.RoleService;
import oil.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
        user.setAuthorities((List<Role>) byId.getAuthorities());
        user.setPassWord(byId.getPassword());
        user.setNonLocked(true);
        user.setNonExpired(true);
        userDetailsService.save(user);
        model.addAttribute("msg","更新成功");
        return "user/user_info";
    }


    /**
     * 修改用户(admin)
     * @param user
     * @param model
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/change2")
    public String changeUserByAdmin(User user, Model model){
        User byId = userDetailsService.findById(user.getId());
        user.setPassWord(byId.getPassword());
        user.setAuthorities((List<Role>) byId.getAuthorities());
        user.setNonLocked(true);
        user.setNonExpired(true);
        userDetailsService.save(user);
        model.addAttribute("msg","更新成功");
        return "admin/user_info";
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

        if (bCryptPasswordEncoder.matches(oldPwd,byUserName.getPassword())) {
            byUserName.setPassWord(bCryptPasswordEncoder.encode(pwd));
        }
        model.addAttribute("msg","修改成功");
        return "user/pwd_change";
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
    @GetMapping(value = {"/get"})
    public String getAll(Model model){
        Iterable<User> all = userDetailsService.findAll();
        model.addAttribute("users",all);
        return "admin/userControl";
    }


    @RolesAllowed("ROLE_ADMIN")
    @GetMapping(value = "/get/{id}/info")
    public String getUser(@PathVariable(name = "id") User user,Model model){
        model.addAttribute("user",user);
        return "admin/user_info";
    }

    /**
     * 锁定
     * @param user
     * @return
     */
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping(value = "/lock/{id}")
    public String lock(@PathVariable(name = "id") User user,Model model) {
        user.setNonExpired(!user.getNonExpired());
        userDetailsService.save(user);
        return getAll(model);
    }



    /**
     * 删除
     * @param user
     * @return
     */
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping(value = "/delete/{id}")
    public String delete(@PathVariable(name = "id") User user,Model model) {
        userDetailsService.delete(user);
        return getAll(model);
    }

    /**
     * 授权
     * @param user
     * @return
     */
    @RolesAllowed("ROLE_ADMIN")
    @PostMapping(value = "/give_role")
    public String giveRole(@RequestParam(name = "id") User user) {
        Role roleUser = roleService.getRole("ROLE_ADMIN");
        List<Role> authorities = (List<Role>) user.getAuthorities();
        authorities.add(roleUser);
        userDetailsService.save(user);
        return "";
    }

    /**
     * 撤销权限
     * @param user
     * @return
     */
    @RolesAllowed("ROLE_ADMIN")
    @PostMapping(value = "/remove_role")
    public String removeRole(@RequestParam(name = "id") User user) {
        Role roleUser = roleService.getRole("ROLE_ADMIN");
        List<Role> authorities = (List<Role>) user.getAuthorities();
        authorities.remove(roleUser);
        userDetailsService.save(user);
        return "";
    }


}
