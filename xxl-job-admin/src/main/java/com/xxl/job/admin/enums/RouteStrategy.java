package com.xxl.job.admin.enums;

import com.xxl.job.admin.core.route.ExecutorRouter;
import com.xxl.job.admin.core.route.strategy.*;

/**
 * @author sweeter
 * @date 2022/12/3
 */
public enum RouteStrategy {
    /**
     * 任务分发路由策略
     */

    FIRST(){
        @Override
        public ExecutorRouter getRouter() {
            return new ExecutorRouteFirst();
        }
    },
    LAST(){
        @Override
        public ExecutorRouter getRouter() {
            return new ExecutorRouteLast();
        }
    },
    ROUND(){
        @Override
        public ExecutorRouter getRouter() {
            return new ExecutorRouteRound();
        }
    },
    RANDOM(){
        @Override
        public ExecutorRouter getRouter() {
            return new ExecutorRouteRandom();
        }
    },
    CONSISTENT_HASH(){
        @Override
        public ExecutorRouter getRouter() {
            return new ExecutorRouteConsistentHash();
        }
    },
    LEAST_FREQUENTLY_USED(){
        @Override
        public ExecutorRouter getRouter() {
            return new ExecutorRouteLFU();
        }
    },
    LEAST_RECENTLY_USED(){
        @Override
        public ExecutorRouter getRouter() {
            return new ExecutorRouteLRU();
        }
    },
    /**
     * 故障转移(FAILOVER)”时，当调度中心每次发起调度请求时，会按照顺序对执行器发出心跳检测请求，第一个检测为存活状态的执行器将会被选定并发送调度请求
     */
    FAILOVER(){
        @Override
        public ExecutorRouter getRouter() {
            return new ExecutorRouteFailover();
        }
    },
    BUSYOVER(){
        @Override
        public ExecutorRouter getRouter() {
            return new ExecutorRouteBusyover();
        }
    },
    SHARDING_BROADCAST() {
        @Override
        public ExecutorRouter getRouter() {
            return null;
        }
    },

    /**
     * 完全依赖于spring cloud注册中心路由
     */
    SPRING_CLOUD() {
        /**
         * 获取路由分发策略
         *
         * @return
         */
        @Override
        public ExecutorRouter getRouter() {
            return null;
        }
    },


    ;


    /**
     * 获取路由分发策略
     * @return
     */
    public abstract ExecutorRouter getRouter();
}
