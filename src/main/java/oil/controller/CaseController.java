package oil.controller;

import oil.model.Case;
import oil.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by  waiter on 18-9-22  下午6:01.
 *
 * @author waiter
 */
@Controller
@RequestMapping(value = "/case")
public class CaseController {
    @Autowired
    private CaseService caseService;

    @GetMapping(value = "/{id}/case_info")
    public String getCase(@PathVariable(value = "id")Case c, Model model){
        model.addAttribute("case",c);
        return "";
    }
}
