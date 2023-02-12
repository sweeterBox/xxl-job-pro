package com.xxl.job.admin.service;


import com.xxl.job.admin.entity.Task;
import com.xxl.job.model.R;

import java.util.Date;
import java.util.Map;

/**
 * core job action for xxl-job
 *
 * @author xuxueli 2016-5-28 15:30:33
 */
public interface JobService {

	/**
	 * add job
	 *
	 * @param jobInfo
	 * @return
	 */
	R<String> add(Task jobInfo);

	/**
	 * update job
	 *
	 * @param jobInfo
	 * @return
	 */
	R<String> update(Task jobInfo);

	/**
	 * remove job
	 * 	 *
	 * @param id
	 * @return
	 */
	R<String> remove(Long id);

	/**
	 * start job
	 *
	 * @param id
	 * @return
	 */
	R<String> start(Long id);

	/**
	 * stop job
	 *
	 * @param id
	 * @return
	 */
	R<String> stop(Long id);

}
