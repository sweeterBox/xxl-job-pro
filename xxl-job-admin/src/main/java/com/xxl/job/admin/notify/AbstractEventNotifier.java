package com.xxl.job.admin.notify;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sweeter
 * @date 2022/10/18
 */
@Getter
@Setter
public abstract class AbstractEventNotifier implements Notifier{

        /**
         * Enables the notification.
         */
        protected boolean enabled = true;

        /**
         * 通知
         *
         * @param event
         */
        @Override
        public void notify(Event event) {
                if (this.enabled) {
                        this.doNotify(event);
                }
        }

       public abstract void doNotify(Event event);
}
