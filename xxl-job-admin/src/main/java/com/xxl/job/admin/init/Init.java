package com.xxl.job.admin.init;
import com.xxl.job.utils.SpringContextUtils;
import com.xxl.job.admin.config.SystemProperties;
import com.xxl.job.admin.entity.User;
import com.xxl.job.admin.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import java.util.Optional;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Component
@Slf4j
public class Init implements CommandLineRunner {

    @Autowired
    private SystemProperties systemProperties;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void run(String... args) throws Exception {
        //TODO 环境检查
        //初始化admin账号
        String username = systemProperties.getInitAdminUsername();
        String password = systemProperties.getInitAdminPassword();
        User user = new User();
        user.setUsername(username);
        UserRepository userRepository = SpringContextUtils.getBean(UserRepository.class);
        Optional<User> userOpt = userRepository.findOne(Example.of(user));
        if (userOpt.isPresent()) {

        }else {
            String slat = DigestUtils.md5DigestAsHex(username.getBytes());
            String passwordMd5 = DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex(password.getBytes()) + slat).getBytes());
            user.setUsername(username);
            user.setPassword(passwordMd5);
            user.setRole(1);
            userRepository.save(user);
            System.out.println("初始化管理员用户信息，用户名：" + username + " 密码：" + password);
        }

        System.out.println("JAVA_HOME:" + System.getProperty("java.home"));
        System.out.println("JAVA_VERSION:" + System.getProperty("java.version"));
        System.out.println("USER_HOME:" + System.getProperty("user.home"));
        System.out.println("USER_NAME:" + System.getProperty("user.name"));

    }
}
