package com.xxl.job.admin;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sweeter
 * @date 2022/11/20
 */
@AutoConfigureBefore(FreeMarkerProperties.class)
@Configuration
public class WebjarsConfiguration {

    @Configuration
    public static class WebjarsResourceHandler implements WebMvcConfigurer{

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/static/")
                    .resourceChain(false);
            registry.addResourceHandler("/templates/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/templates/")
                    .resourceChain(false);
        }


    }

    @Primary
    @Bean
    public FreeMarkerProperties webjarsFreeMarkerProperties(FreeMarkerProperties freeMarkerProperties) {
        freeMarkerProperties.setCharset(Charset.forName("UTF-8"));
        Map<String, String> settings = new HashMap<>();
        settings.put("number_format", "0.##########");
        freeMarkerProperties.setSettings(settings);
        freeMarkerProperties.setRequestContextAttribute("request");
        freeMarkerProperties.setTemplateLoaderPath("classpath:/META-INF/resources/webjars/templates/");
        freeMarkerProperties.setSuffix(".ftl");
         return freeMarkerProperties;
    }

}
