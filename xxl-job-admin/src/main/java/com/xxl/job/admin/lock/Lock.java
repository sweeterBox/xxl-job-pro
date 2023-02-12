package com.xxl.job.admin.lock;

/**
 * @author sweeter
 * @date 2023/1/15
 */
public interface Lock {
    String SEPARATOR = ":";
    /**
     * 锁前缀
     */
    String LOCK_KEY_REFIX = "XXL-JOB:LOCK";
    /**
     * 默认redis 锁过期时间 单位/毫秒
     */
    Long DEFAULT_EXPIRE_TIME = 10 * 1000L;
    /**
     * 默认的取锁超时时间 单位/毫秒
     */
    Long DEFAULT_TIME_OUT =  1000L;

    /**
     * 获取锁
     * @param key lock key
     * @return boolean
     */
    boolean lock(String key);
    /**
     * 获取锁
     * @param key lock key
     * @param expire lock 超时时间 单位/毫秒 注：该时间应该大于业务处理的时间
     * @return boolean
     */
    boolean lock(String key, Long expire);
    /**
     *  获取锁
     * @param key lock key
     * @param timeOut 取lock 超时时间 单位/毫秒
     * @param expire lock 超时时间 单位/毫秒 注：该时间应该大于业务处理的时间
     * @return boolean
     */
    boolean lock(String key, Long timeOut, Long expire);
    /**
     * 释放锁
     * @param key lock key
     * @return
     */
    boolean unlock(String key);
    /**
     * 根据方法参数生成 key
     * @param params
     * @return
     */
    default String createKeyByParams(Object... params) {
        StringBuilder key = new StringBuilder(LOCK_KEY_REFIX);
        if (params != null && params.length > 0) {
            for (Object param : params) {
                key.append(SEPARATOR).append(param);
            }
        }
        return key.toString();
    }
}
