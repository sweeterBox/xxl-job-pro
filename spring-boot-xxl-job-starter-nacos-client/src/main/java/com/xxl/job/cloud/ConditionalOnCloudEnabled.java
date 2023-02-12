package com.xxl.job.cloud;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import java.lang.annotation.*;

/**
 * @author sweeter
 * @date 2023/1/14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ConditionalOnProperty(value = {"spring.cloud.nacos.discovery.enabled","spring.cloud.discovery.enabled"}, matchIfMissing = true)
public @interface ConditionalOnCloudEnabled {

}
