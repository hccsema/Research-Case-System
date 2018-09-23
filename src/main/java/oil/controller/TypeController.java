package oil.controller;

import oil.model.Type;
import oil.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

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
        return "";
    }

    /**
     * 添加
     * @param type
     * @return
     */
    @PostMapping(value = "/add")
    public String addType(Type type){
        Assert.notNull(type,"未知参数");
        type.setGrade(4);
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
    public String changeType(Type type){
        Assert.notNull(type,"未知参数");
        typeService.save(type);
        return "";
    }

    @PostMapping(value = "/remove")
    public String remove(@RequestParam(name = "type") Type type){
        type.setIsExist(false);
        typeService.save(type);
        return "";
    }

}
