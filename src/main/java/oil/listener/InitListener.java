package oil.listener;


import oil.model.Lib;
import oil.model.Tag;
import oil.model.Type;
import oil.repository.result.DayAndCount;
import oil.service.CaseService;
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
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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

        service(sce);
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

    private void initCaseByDate(ServletContextEvent sce){
        CaseService bean = applicationContext.getBean(CaseService.class);
        Collection<DayAndCount> countByDate = bean.getCountByDate();
        sce.getServletContext().setAttribute("countByDate",countByDate);
    }

    private void service(ServletContextEvent sce){
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        // 参数：1、任务体 2、首次执行的延时时间
        //      3、任务执行间隔 4、间隔时间单位
        service.scheduleAtFixedRate(()->{
            initLib(sce);
            initTag(sce);
            initType(sce);
            initCaseByDate(sce);
        }, 0, 10, TimeUnit.MINUTES);


       }

}