package oil.controller;

import oil.model.Lib;
import oil.service.LibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by  waiter on 18-9-23  下午2:12.
 *
 * @author waiter
 */
@Controller
@RequestMapping(value = "/lib")
public class LibController {
    @Autowired
    private LibService libService;

    @GetMapping(value = "/")
    public String getLib(Model model){
        Lib lib = libService.getLib();
        model.addAttribute("lib",lib);
        return "";
    }

    @PostMapping(value = "change")
    public String change(String introduction,
                         String subtitle,
                         String mainTitle){
        Lib lib = libService.getLib();
        lib.setIntroduction(introduction);
        lib.setSubtitle(subtitle);
        lib.setMainTitle(mainTitle);
        libService.save(lib);
        return "";
    }
}
