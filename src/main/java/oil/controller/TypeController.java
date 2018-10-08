package oil.controller;

import oil.listener.InitListener;
import oil.model.Case;
import oil.model.Doc;
import oil.model.Type;
import oil.service.DocService;
import oil.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.File;
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
    @Autowired
    private DocService docService;

    @Value("${doc.path}")
    private String basePath;

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
        if (type.getId()!=null) {
            Type byId = typeService.findById(type.getId());
            if (!byId.getName().equals(type.getName())){
                List<Case> cases = byId.getCases();
                for (Case c:cases){
                    List<Doc> contents = c.getContents();
                    for (Doc doc:contents){
                        String path = doc.getPath();
                        String replace = path.replace(byId.getName(),type.getName());
                        doc.setPath(replace);
                        File file = new File(basePath+path);
                        File file1 = new File(basePath + replace);
                        creatDir(file1.getParentFile());
                        file.renameTo(file1);
                    }
                    c.setContents(contents);

                    List<Doc> solves = c.getSolves();
                    for (Doc doc:solves){
                        String path = doc.getPath();
                        String replace = path.replace(byId.getName(),type.getName());
                        doc.setPath(replace);
                        File file = new File(basePath+path);
                        File file1 = new File(basePath + replace);
                        creatDir(file1.getParentFile());
                        file.renameTo(file1);
                    }

                    docService.saveAll(contents);
                    docService.saveAll(solves);

                    c.setSolves(solves);
                }
            }
            type.setCases(byId.getCases());
        }
        type.setIsExist(true);

        typeService.save(type);
        ArrayList<Type> all = typeService.findAll();
        for (Type typ:all){
            List<Case> cases = type.getCases();
            List<Case> cases1 = new ArrayList<>();
            for (Case c:cases){
                if (c.getIsExist()){
                    cases1.add(c);
                }
            }
            typ.setCases(cases1);
        }
        InitListener.getApplicatonContext().getServletContext().setAttribute("types",all);
        return findAll(model);
    }

    private void creatDir(File file){
        while (!file.exists()){
            creatDir(file.getParentFile());
            file.mkdir();
        }
    }

    @GetMapping(value = "/remove/{type}")
    public String remove(@PathVariable(name = "type") Type type, Model model){
        type.setIsExist(false);
        typeService.save(type);
        InitListener.getApplicatonContext().getServletContext().setAttribute("type",type);
        return findAll(model);
    }

}
