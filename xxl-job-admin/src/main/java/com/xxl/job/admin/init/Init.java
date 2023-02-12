package com.xxl.job.admin.init;
import com.xxl.job.utils.SpringContextUtils;
import com.xxl.job.admin.config.SystemProperties;
import com.xxl.job.admin.entity.User;
import com.xxl.job.admin.repository.UserRepository;
import com.xxl.job.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
        User user = new User();
        user.setId(1L);
        UserRepository userRepository = SpringContextUtils.getBean(UserRepository.class);
        Optional<User> userOpt = userRepository.findOne(Example.of(user));
        if (userOpt.isPresent()) {

        }else {
            user.setUsername("admin");
            user.setPassword("e10adc3949ba59abbe56e057f20f883e");
            user.setRole(1);
            userRepository.save(user);
            log.info("初始化管理员用户信息，data:{}", user);
        }


    }
}
