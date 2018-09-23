package oil.controller;

import oil.model.Case;
import oil.model.Tag;
import oil.model.Type;
import oil.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        c.setTimes(c.getTimes()+1);
        caseService.changTimes(c);
        Page<Case> top10ByTypeAndIsExistOrderByTimes = caseService.findTop10ByTypeAndIsExistOrderByTimes(c.getType());
        model.addAttribute("case",c);
        model.addAttribute("top",top10ByTypeAndIsExistOrderByTimes);
        return "front/case";
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
            page=0;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Page<Case> casesByDate = caseService.getCasesByDate(simpleDateFormat.parse(date), page);
        model.addAttribute("cases",casesByDate);
        model.addAttribute("type",date);

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
            page=0;
        }

        Page<Case> allByTagsContaining = caseService.findAllByTagsContaining(page, tag);
        model.addAttribute("cases",allByTagsContaining);
        model.addAttribute("type",tag);

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
        model.addAttribute("type",type);

        return "front/more";
    }

    /**
     * 彻底删除案例
     * @param c
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/delete.html")
    public String deleteCase(@RequestParam(name = "case",required = false) Case c){
        if (c!=null) {
            caseService.delete(c);
        }
        return "";
    }

    /**
     * 删除案例
     * @param c
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/remove.html")
    public String removeCase(@RequestParam(name = "case",required = false) Case c){
        if (c!=null) {
            c.setIsExist(false);
            caseService.save(c);
        }
        return "";
    }

    /**
     * 添加案例
     * @param c
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/add.html")
    public String addCase( Case c){
        Assert.notNull(c,"没有参数");

        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyyMMddHHmmss");

        c.setIsExist(true);
        c.setDate(new Date());
        c.setTimes(0L);
        c.setLibId(simpleDateFormat.format(new Date()));
        System.out.println(c);
        caseService.save(c);
        return "";
    }

}
