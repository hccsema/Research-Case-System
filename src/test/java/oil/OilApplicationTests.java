package oil;

import oil.model.Doc;
import oil.model.Role;
import oil.model.User;
import oil.service.CaseService;
import oil.service.DocService;
import oil.service.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Transient;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(rollbackFor = Exception.class)
public class OilApplicationTests {


    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private DocService docService;
    @Autowired
    private CaseService caseService;

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void contextLoads() {
        User user = new User();
        user.setUserName("123456");
        user.setPassWord("123456");
        user.setNonExpired(true);
        user.setNonLocked(true);
        user.setNickName("测试");
        ArrayList<Role> objects = new ArrayList<>();
        objects.add(new Role("ROLE_USER"));
        user.setAuthorities(objects);
        userDetailsServiceImpl.save(user);
    }

    @Test
    public void T1(){
        Doc doc = new Doc();
        doc.setName("001");
        doc.setPath("ssasss");
        docService.save(doc);
    }

}
