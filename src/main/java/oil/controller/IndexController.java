package oil.controller;

import oil.listener.InitListener;
import oil.model.Case;
import oil.model.Doc;
import oil.model.Lib;
import oil.model.Tag;
import oil.service.CaseService;
import oil.service.DocService;
import oil.service.LibService;
import oil.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by  waiter on 18-9-19  下午7:14.
 *
 * @author waiter
 */
@Controller
public class IndexController {
    @Autowired
    private LibService libService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CaseService caseService;
    @Autowired
    private DocService docService;

    @RequestMapping(value = {"/","/index"})
    public String index(){
        WebApplicationContext applicatonContext = InitListener.getApplicatonContext();
        Lib lib = (Lib) applicatonContext.getServletContext().getAttribute("lib");
        lib.setTimes(lib.getTimes()+1);
        libService.save(lib);
        return "index";
    }

    @GetMapping(value = "/search/{search}")
    public String search(@PathVariable(name = "search",required = false) String search,
                         Model model){
        ArrayList<Tag> search1 = tagService.search(search);
        ArrayList<Case> search2 = caseService.search(search);
        ArrayList<Doc> search3 = docService.search(search);
        HashMap<Long, Case> longCaseHashMap = new HashMap<>();
        for (Case c:search2){
            longCaseHashMap.put(c.getId(),c);
        }

        for (Tag tag:search1){
            List<Case> cases = tag.getCases();
            for (Case c:cases){
                longCaseHashMap.put(c.getId(),c);
            }
        }

        for (Doc doc:search3){
            Case aCase = doc.getACase();
            longCaseHashMap.put(aCase.getId(),aCase);
        }

        model.addAttribute("cases",longCaseHashMap);
        return "";
    }

}
