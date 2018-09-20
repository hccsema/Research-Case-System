package oil.controller;

import oil.listener.InitListener;
import oil.model.Lib;
import oil.service.LibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by  waiter on 18-9-19  下午7:14.
 *
 * @author waiter
 */
@Controller
public class IndexController {
    @Autowired
    private LibService libService;

    @RequestMapping(value = {"/","/index"})
    public String index(){
        WebApplicationContext applicatonContext = InitListener.getApplicatonContext();
        Lib lib = (Lib) applicatonContext.getServletContext().getAttribute("lib");
        lib.setTimes(lib.getTimes()+1);
        libService.save(lib);
        return "index";
    }
}
