package oil.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Created by  waiter on 18-6-18.
 * @author waiter
 *
 * MVC配置
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/front/more").setViewName("front/more");
        registry.addViewController("/front/tag").setViewName("front/tag");
        registry.addViewController("/front/case").setViewName("front/case");
        registry.addViewController("/front/date").setViewName("front/date");
        registry.addViewController("/front/search").setViewName("front/search");

        registry.addViewController("/admin/index").setViewName("admin/index");
        registry.addViewController("/admin/projectCon").setViewName("admin/projectCon");
        registry.addViewController("/admin/recycleBin").setViewName("admin/recycleBin");
        registry.addViewController("/admin/userAdd").setViewName("admin/userAdd");
        registry.addViewController("/admin/userControl").setViewName("admin/userControl");

        registry.addViewController("/user/user_info").setViewName("user/user_info");
        registry.addViewController("/admin/user_info").setViewName("admin/user_info");

        registry.addViewController("/user/pwd_change").setViewName("user/pwd_change");
        registry.addViewController("/admin/pwd_change").setViewName("admin/pwd_change");

        registry.addViewController("/admin/frontdesk").setViewName("admin/frontdesk");
    }
}
