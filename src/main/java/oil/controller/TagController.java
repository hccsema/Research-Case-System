package oil.controller;

import oil.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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



}
