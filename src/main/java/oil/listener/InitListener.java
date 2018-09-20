package oil.listener;


import oil.model.Lib;
import oil.model.Tag;
import oil.model.Type;
import oil.service.LibService;
import oil.service.TagService;
import oil.service.TypeService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;


/**
 * Created by  waiter on 18-9-19  下午7:22.
 *
 * @author waiter
 */
@WebListener
@Configuration
public class InitListener implements ServletContextListener {
    private  static WebApplicationContext applicationContext = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //System.out.println("ServletContext对象创建");

        //初始化 ApplicationContext  对象
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());


        initLib(sce);
        initType(sce);
        initTag(sce);

    }




    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Lib lib = (Lib) sce.getServletContext().getAttribute("lib");
        LibService libService = applicationContext.getBean(LibService.class);
        libService.save(lib);
    }

    //用于那些非控制层中使用直接获取到的Spring Bean的获取，如接口

    public static WebApplicationContext  getApplicatonContext(){

        return applicationContext ;

    }

    public void initLib(ServletContextEvent sce){
        LibService libService = applicationContext.getBean(LibService.class);

        Lib lib = libService.getLib();
        if (lib==null){
            lib=new Lib();
            lib.setMainTitle("研究生课程教学案例库");
            lib.setSubtitle("石油与天然气工程专业学位课程——采油采气工程设计与应用");
            lib.setTimes(0L);
            lib.setIntroduction("案例库");
            libService.save(lib);
        }

        sce.getServletContext().setAttribute("lib",lib);
    }

    private void initType(ServletContextEvent sce) {
        TypeService bean = applicationContext.getBean(TypeService.class);
        ArrayList<Type> all = bean.findAll();
        sce.getServletContext().setAttribute("types",all);
    }

    private void initTag(ServletContextEvent sce) {
        TagService bean = applicationContext.getBean(TagService.class);
        ArrayList<Tag> all = bean.findAll();
        sce.getServletContext().setAttribute("tags",all);
    }

}