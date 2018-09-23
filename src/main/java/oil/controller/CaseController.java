package oil.controller;

import oil.model.Case;
import oil.model.Tag;
import oil.model.Type;
import oil.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    /**
     * 通过id获取
     * @param c
     * @param model
     * @return
     */
    @GetMapping(value = "/{id}/case_info.html")
    public String getCaseById(@PathVariable(name = "id")Case c, Model model){
        model.addAttribute("case",c);
        return "";
    }

    /**
     * 通过日期查找，精确到月 2018-08
     * @param date
     * @param page
     * @param model
     * @return
     * @throws ParseException
     */
    @GetMapping(value = {"/{date}/{page}/date.html","/{date}/date.html"})
    public String getCasesByDate(@PathVariable(name = "date") String date,
                                @PathVariable(name = "page",required = false) Integer page,
                                Model model) throws ParseException {
        if (page==null){
            page=1;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Page<Case> casesByDate = caseService.getCasesByDate(simpleDateFormat.parse(date), page);
        model.addAttribute("cases",casesByDate);

        return "front/more";
    }


    /**
     * 根据一个标签查找
     * @param tag
     * @param page
     * @param model
     * @return
     */
    @GetMapping(value = {"/{tag}/{page}/tag.html","/{tag}/tag.html"})
    public String getCasesByTag(@PathVariable(name = "tag") Tag tag,
                                @PathVariable(name = "page",required = false) Integer page,
                                Model model){
        if (page==null){
            page=1;
        }

        Page<Case> allByTagsContaining = caseService.findAllByTagsContaining(page, tag);
        model.addAttribute("cases",allByTagsContaining);


        return "front/more";
    }

    /**
     * 根据类别
     * @param type
     * @param page
     * @param model
     * @return
     */
    @GetMapping(value = {"/{type}/{page}/type.html","/{type}/type.html"})
    public String getCasesByType(@PathVariable(name = "type") Type type,
                                @PathVariable(name = "page",required = false) Integer page,
                                Model model){
        if (page==null){
            page=0;
        }

        Page<Case> allByType = caseService.findAllByType(page, type);
        model.addAttribute("cases",allByType);


        return "front/more";
    }

}
