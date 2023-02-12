package com.xxl.job.admin.notify;

/**
 * @author sweeter
 * @description 消息通知
 * @date 2022/10/18
 */
public interface Notifier {

   /**
    * 通知
    * @param event
    */
   void notify(Event event);
}
