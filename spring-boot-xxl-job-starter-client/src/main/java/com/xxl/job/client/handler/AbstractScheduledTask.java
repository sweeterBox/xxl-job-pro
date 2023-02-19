package com.xxl.job.client.handler;

import org.apache.commons.lang3.StringUtils;

/**
 * @author sweeter
 * @date 2023/2/19
 */
public abstract class AbstractScheduledTask {

    /**
     * 任务名称/标识（英文），不可使用中文
     * @return task name
     */
    public String name(){
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
