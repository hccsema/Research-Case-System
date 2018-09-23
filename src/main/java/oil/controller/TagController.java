package oil.controller;

import oil.model.Tag;
import oil.service.TagService;
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
 * Created by  waiter on 18-9-21  上午11:13.
 *
 * @author waiter
 */
@Controller
@RequestMapping(value = "/tag")
public class TagController {
    @Autowired
    private TagService tagService;


    @PostMapping(value = "/add")
    public String add(Tag tag){
        Assert.notNull(tag,"未知参数");
        tag.setIsExist(true);
        tagService.save(tag);
        return "";
    }

    @PostMapping(value = "/remove")
    public String remove(@RequestParam(name = "tag") Tag tag){
        Assert.notNull(tag,"未知参数");
        tag.setIsExist(false);
        tagService.save(tag);
        return "";
    }


    @GetMapping(value = "/")
    public String fandAll(Model model){
        ArrayList<Tag> all = tagService.findAll();
        model.addAttribute("tags",all);
        return "";
    }

}
