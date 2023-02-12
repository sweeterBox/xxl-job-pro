package com.xxl.job.admin.core.route.strategy;

import com.xxl.job.admin.core.route.ExecutorRouter;
import com.xxl.job.model.R;
import com.xxl.job.model.TriggerParam;
import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteFirst extends ExecutorRouter {

    @Override
    public R<String> route(TriggerParam triggerParam, List<String> addressList){
        return new R<>(addressList.get(0));
    }

}
