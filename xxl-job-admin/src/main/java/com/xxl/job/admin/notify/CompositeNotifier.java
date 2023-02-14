package com.xxl.job.admin.notify;

import java.util.Iterator;

/**
 * @author sweeter
 * @date 2023/2/13
 */
public class CompositeNotifier implements Notifier {

    private final Iterable<Notifier> delegates;

    public CompositeNotifier(Iterable<Notifier> delegates) {
        this.delegates = delegates;
    }

    /**
     * 通知
     *
     * @param event
     */
    @Override
    public void notify(Event event) {
        Iterator<Notifier> notifierIterator = this.delegates.iterator();
        while (notifierIterator.hasNext()) {
            notifierIterator.next().notify(event);
        }
    }
}
