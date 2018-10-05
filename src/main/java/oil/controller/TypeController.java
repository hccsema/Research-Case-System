package oil.controller;

import oil.listener.InitListener;
import oil.model.Type;
import oil.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  waiter on 18-9-23  下午1:59.
 *
 * @author waiter
 */
@Controller
@RequestMapping(value = "/type")
public class TypeController {
    @Autowired
    private TypeService typeService;

    /**
     * 查询所有
     * @param model
     * @return
     */
    @GetMapping(value = "/")
    public String findAll(Model model){
        ArrayList<Type> all = typeService.findAll();
        model.addAttribute("types",all);
        return "admin/typechange";
    }

    /**
     * 添加
     * @param type
     * @return
     */
    @PostMapping(value = "/add")
    public String addType(Type type){
        Assert.notNull(type,"未知参数");

        type.setIsExist(true);
        typeService.save(type);
        return "";
    }

    /**
     * 修改
     * @param type
     * @return
     */
    @PostMapping(value = "/change")
    public String changeType(Type type,Model model){
        Assert.notNull(type,"未知参数");
        Type byId = typeService.findById(type.getId());
        type.setIsExist(true);
        type.setCases(byId.getCases());
        typeService.save(type);
        ArrayList<Type> all = typeService.findAll();
        InitListener.getApplicatonContext().getServletContext().setAttribute("types",all);
        return findAll(model);
    }


    @GetMapping(value = "/remove/{type}")
    public String remove(@PathVariable(name = "type") Type type, Model model){
        type.setIsExist(false);
        typeService.save(type);
        InitListener.getApplicatonContext().getServletContext().setAttribute("type",type);
        return findAll(model);
    }

}
