package oil.component;

import oil.model.User;
import oil.service.UserDetailsServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by  waiter on 18-9-23  下午2:54.
 *
 * @author waiter
 */
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    Log logger = LogFactory.getLog(CustomLoginSuccessHandler.class);


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        RequestCache requestCache = new HttpSessionRequestCache();

        this.saveLoginInfo(request, authentication);

        // 登录成功之后将SECURITY放入上下文中
        request.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        String url = null;
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        if(savedRequest != null){
            url = savedRequest.getRedirectUrl();
        }
        if(url == null){
            response.sendRedirect(request.getContextPath()+"/");
        }else {
            response.sendRedirect(url);
        }

    }

    private void saveLoginInfo(HttpServletRequest request, Authentication authentication) {
        UserDetails user = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User byUserName = userDetailsService.findByUserName(user.getUsername());
        String ip = request.getRemoteAddr();
        byUserName.setLastLoginDate(byUserName.getThisLoginDate());
        byUserName.setLastLoginIp(byUserName.getThisLoginIp());
        byUserName.setThisLoginDate(new Date());
        byUserName.setThisLoginIp(ip);
        logger.info("登录用户:" + user);
        this.userDetailsService.save(byUserName);
    }

}

