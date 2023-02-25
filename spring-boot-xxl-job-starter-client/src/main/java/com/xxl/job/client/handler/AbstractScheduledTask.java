package com.xxl.job.client.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * spring bean class 继承该抽象类可实现任务调度
 * @author sweeter
 * @date 2023/2/19
 */
@Slf4j
public abstract class AbstractScheduledTask {

    /**
     * 任务名称/标识（英文），不可使用中文
     * @return task name
     */
    public String name(){
        log.warn("You should override the method to define the name of the task");
        return this.toFistLowerCase(this.getClass().getSimpleName());
    }

    /**
     * 任务中文描述信息
     * @return 任务中文描述信息
     */
    public abstract String description();

    /**
     * 作者
     * @return
     */
    public String author() {
        log.warn("You should override this method to mark the author of the task");
        return "anonymous";
    }


    /**
     * 执行
     */
    public abstract void execute();


    public void init(){

    }


    public void destroy() {

    }

    /**
     * 首字母转换为小写
     * @param name 任务名称
     * @return 首字母小写的任务标识
     */
    protected String toFistLowerCase(String name) {
        if (StringUtils.isNotBlank(name)) {
            char[] cs = name.toCharArray();
            cs[0]-=32;
            return String.valueOf(cs);
        }
        return name;
    }
}
