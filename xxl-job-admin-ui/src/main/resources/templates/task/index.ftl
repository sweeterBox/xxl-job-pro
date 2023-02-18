<!DOCTYPE html>
<html>
<head>
  	<#import "../common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/fontawesome-free/css/all.min.css">
    <!-- DataTables -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/dist/css/adminlte.min.css">
    <title>${I18n.admin_name}</title>
</head>
<body class="sidebar-mini layout-footer-fixed control-sidebar-slide-open layout-fixed layout-navbar-fixed">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "task" />
	<div class="content-wrapper">
        <section class="content">
            <div class="container-fluid">
	    	<div class="row">
	    		<div class="col-2">
	              	<div class="input-group ">
                        <span class="input-group-text" id="inputGroup-sizing-lg">应用</span>
                		<select class="form-control" name="applicationName" id="allApplication">

	                  	</select>
	              	</div>
	            </div>
                <div class="col-2">
                    <div class="input-group ">
                        <span class="input-group-text" id="inputGroup-sizing-lg">状态</span>
                        <select class="form-control" id="triggerStatus" >
                            <option value="" >${I18n.system_all}</option>
                            <option value="0" >${I18n.jobinfo_opt_stop}</option>
                            <option value="1" >${I18n.jobinfo_opt_start}</option>
                        </select>
                    </div>
                </div>
                <div class="col-2">
                    <div class="input-group ">
                        <span class="input-group-text" id="inputGroup-sizing-lg">${I18n.jobinfo_field_jobdesc}</span>
                        <input type="text" class="form-control" id="description" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_jobdesc}" aria-label="description">
                    </div>
                </div>
                <div class="col-2">
                    <div class="input-group ">
                        <input type="text" class="form-control" id="executorHandler" placeholder="${I18n.system_please_input}JobHandler" aria-label="executorHandler">
                    </div>
                </div>
                <div class="col-2">
                    <div class="input-group ">
                        <input type="text" class="form-control" id="author" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_author}" >
                    </div>
                </div>
	            <div class="col-1">
                    <div class="input-group ">
                        <button class="btn btn-block  btn-search" type="button" id="searchBtn">${I18n.system_search}</button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-1">
                    <div class="input-group">
                        <button class="btn btn-block btn-primary add" type="button">新增</button>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body" >
                            <table id="job_list" class="table table-bordered table-hover" width="100%" >
				                <thead>
					            	<tr>
                                        <th name="id">任务ID</th>
                                        <th name="applicationName">应用名称</th>
                                        <th name="description">任务描述</th>
                                        <th name="scheduleType">调度类型</th>
                                        <th name="glueType">运行模式</th>
                                        <th name="executorParam">${I18n.jobinfo_field_executorparam}</th>
					                  	<th name="updateTime" >updateTime</th>
					                  	<th name="author" >${I18n.jobinfo_field_author}</th>
					                  	<th name="alarmEmail" >${I18n.jobinfo_field_alarmemail}</th>
					                  	<th name="executorTimeout">超时时间</th>
					                  	<th name="executorBlockStrategy">阻塞策略</th>
					                  	<th name="triggerStatus">${I18n.system_status}</th>
					                  	<th name="lastTriggerTime">最后一次调度时间</th>
					                  	<th>${I18n.system_opt}</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
				                <tfoot></tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
			</div>
	    </section>
	</div>

	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<!-- 新增 -->
<div class="modal fade" id="addModal"  role="dialog"  aria-hidden="true" >
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >新增</h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
                    <p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_base}</p>
                    <div class="col-12">
                        <div class="form-group input-group">
                            <label for="application" class="col-sm-2 control-label">应用<font color="red">*</font></label>
                            <div class="col-sm-4">
                                <select class="form-control" name="applicationName" id="addAllApplication" ></select>
                            </div>
                            <label for="description" class="col-sm-2 control-label">描述<font color="red">*</font></label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="description" placeholder="描述" maxlength="50" >
                            </div>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="form-group input-group">
                            <label for="author" class="col-sm-2 control-label">负责人<font color="red">*</font></label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="author" placeholder="负责人" maxlength="50" >
                            </div>
                            <label for="alarmEmail" class="col-sm-2 control-label">邮箱</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="alarmEmail" placeholder="${I18n.jobinfo_field_alarmemail_placeholder}" maxlength="100" >
                            </div>
                        </div>
                    </div>
                    <br>
                    <p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_job}</p>    <#-- 任务配置 -->

                    <div class="form-group input-group">
                        <label for="glueType" class="col-sm-2 control-label">${I18n.jobinfo_field_gluetype}<font color="red">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control glueType" name="glueType" >
                                <option value="BEAN">BEAN</option>
                                <#--                                    <option value="GLUE_SHELL">GLUE_SHELL</option>
                                                                    <option value="GLUE_PYTHON">GLUE_PYTHON</option>
                                                                    <option value="GLUE_PHP">GLUE_PHP</option>
                                                                    <option value="GLUE_NODEJS">GLUE_NODEJS</option>
                                                                    <option value="GLUE_POWERSHELL">GLUE_POWERSHELL</option>-->
                            </select>
                        </div>

                        <label for="tasks" class="col-sm-2 control-label">JobHandler<font color="red">*</font></label>
                        <div class="col-sm-4 input-group">
                            <input type="text" class="form-control" name="executorHandler" list="tasks" id="executorHandler" placeholder="${I18n.system_please_input}JobHandler" maxlength="100" >
                            <div class="input-group-append">
                                <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false">请选择</button>
                                <div class="dropdown-menu" id="tasks">

                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="form-group input-group">
                        <label for="executorParam" class="col-sm-2 control-label">${I18n.jobinfo_field_executorparam}<font color="black">*</font></label>
                        <div class="col-sm-10">
                            <textarea class="textarea form-control"  name="executorParam" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_executorparam}" maxlength="512" style="height: 80px !important; line-height: 1.2;"></textarea>
                        </div>
                    </div>
                    <br>
                    <p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_schedule}</p>
                    <div class="col-12">
                        <div class="form-group input-group">
                            <label for="scheduleType" class="col-sm-2 control-label">${I18n.schedule_type}<font color="red">*</font></label>
                            <div class="col-sm-4">
                                <select class="form-control scheduleType" name="scheduleType">
                                    <option value="CRON">CRON</option>
                                    <option value="FIX_RATE">FIX_RATE</option>
                                    <option value="FIX_DELAY">FIX_DELAY</option>
                                    <option value="NONE">NONE</option>
                                </select>
                            </div>

                        </div>
                    </div>
                    <div class="col-12">

                            <div class="schedule_conf schedule_conf_CRON form-group input-group" >
                                <label for="schedule_conf_CRON" class="col-sm-2 control-label">CRON<font color="red">*</font></label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="schedule_conf_CRON" name="schedule_conf_CRON" placeholder="${I18n.system_please_input}Cron" maxlength="128" >
                                </div>
                                <input type="hidden" name="scheduleConf" />
                            </div>

                            <div class="schedule_conf schedule_conf_NONE" style="display: none" ></div>

                            <div class="schedule_conf schedule_conf_FIX_RATE form-group input-group" style="display: none">
                                <label for="schedule_conf_FIX_RATE" class="col-sm-2 control-label">${I18n.schedule_type_fix_rate}<font color="red">*</font></label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" name="schedule_conf_FIX_RATE" placeholder="${I18n.system_please_input} （ Second ）" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" >
                                </div>
                            </div>
                            <div class="schedule_conf schedule_conf_FIX_DELAY form-group input-group" style="display: none" >
                                <label for="schedule_conf_FIX_DELAY" class="col-sm-2 control-label">${I18n.schedule_type_fix_delay}<font color="red">*</font></label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" name="schedule_conf_FIX_DELAY" placeholder="${I18n.system_please_input} （ Second ）" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" >
                                </div>
                            </div>
                    </div>
                    <br>
                    <p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_advanced}</p>    <#-- 高级配置 -->

                    <div class="form-group input-group">
                        <label for="executorRouteStrategy" class="col-sm-2 control-label">${I18n.jobinfo_field_executorRouteStrategy}<font color="black">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control" name="executorRouteStrategy" >
                                <option value="FIRST">FIRST</option>
                                <option value="LAST">LAST</option>
                                <option value="ROUND">ROUND</option>
                                <option value="RANDOM">RANDOM</option>
                                <option value="CONSISTENT_HASH">CONSISTENT_HASH</option>
                                <option value="LEAST_FREQUENTLY_USED">LEAST_FREQUENTLY_USED</option>
                                <option value="LEAST_RECENTLY_USED">LEAST_RECENTLY_USED</option>
                                <option value="FAILOVER">FAILOVER</option>
                                <option value="BUSYOVER">BUSYOVER</option>
                                <option value="SHARDING_BROADCAST">SHARDING_BROADCAST</option>
                            </select>
                        </div>

                        <label for="childJobId" class="col-sm-2 control-label">${I18n.jobinfo_field_childJobId}<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="childJobId" placeholder="${I18n.jobinfo_field_childJobId_placeholder}" maxlength="100" ></div>
                    </div>

                    <div class="form-group input-group">
                        <label for="misfireStrategy" class="col-sm-2 control-label">${I18n.misfire_strategy}<font color="black">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control" name="misfireStrategy" >
                                <option value="DO_NOTHING">DO_NOTHING</option>
                                <option value="FIRE_ONCE_NOW">FIRE_ONCE_NOW</option>
                            </select>
                        </div>

                        <label for="executorBlockStrategy" class="col-sm-2 control-label">${I18n.jobinfo_field_executorBlockStrategy}<font color="black">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control" name="executorBlockStrategy" >
                                <option value="SERIAL_EXECUTION">SERIAL_EXECUTION</option>
                                <option value="DISCARD_LATER">DISCARD_LATER</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group input-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_timeout}<font color="black">*</font></label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="executorTimeout" placeholder="${I18n.jobinfo_field_executorTimeout_placeholder}" maxlength="6" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" ></div>
                        <label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorFailRetryCount}<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="executorFailRetryCount" placeholder="${I18n.jobinfo_field_executorFailRetryCount_placeholder}" maxlength="4" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" ></div>
                    </div>

                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
                    </div>
				</form>
         	</div>
		</div>
	</div>
</div>

<!-- 更新 -->
<div class="modal fade" id="updateModal" role="dialog"  aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
                <h4 class="modal-title">编辑</h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
                    <input name="id" value="" hidden>
                    <p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_base}</p>
                    <div class="col-12">
                        <div class="form-group input-group">
                            <label for="application" class="col-sm-2 control-label">应用<font color="red">*</font></label>
                            <div class="col-sm-4">
                                <select class="form-control" name="applicationName" id="editAllApplication" ></select>
                            </div>
                            <label for="description" class="col-sm-2 control-label">描述<font color="red">*</font></label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="description" placeholder="描述" maxlength="50" >
                            </div>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="form-group input-group">
                            <label for="author" class="col-sm-2 control-label">负责人<font color="red">*</font></label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="author" placeholder="负责人" maxlength="50" >
                            </div>
                            <label for="alarmEmail" class="col-sm-2 control-label">邮箱</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="alarmEmail" placeholder="${I18n.jobinfo_field_alarmemail_placeholder}" maxlength="100" >
                            </div>
                        </div>
                    </div>
                    <br>
                    <p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_job}</p>    <#-- 任务配置 -->

                    <div class="form-group input-group">
                        <label for="glueType" class="col-sm-2 control-label">${I18n.jobinfo_field_gluetype}<font color="red">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control glueType" name="glueType" >
                                <option value="BEAN">BEAN</option>
                                <#--                                    <option value="GLUE_SHELL">GLUE_SHELL</option>
                                                                    <option value="GLUE_PYTHON">GLUE_PYTHON</option>
                                                                    <option value="GLUE_PHP">GLUE_PHP</option>
                                                                    <option value="GLUE_NODEJS">GLUE_NODEJS</option>
                                                                    <option value="GLUE_POWERSHELL">GLUE_POWERSHELL</option>-->
                            </select>
                        </div>

                        <label for="tasks" class="col-sm-2 control-label">JobHandler<font color="red">*</font></label>
                        <div class="col-sm-4 input-group">
                            <input type="text" class="form-control" name="executorHandler" list="tasks" id="executorHandler" placeholder="${I18n.system_please_input}JobHandler" maxlength="100" >
                            <div class="input-group-append">
                                <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false">请选择</button>
                                <div class="dropdown-menu" id="tasks">

                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="form-group input-group">
                        <label for="executorParam" class="col-sm-2 control-label">${I18n.jobinfo_field_executorparam}<font color="black">*</font></label>
                        <div class="col-sm-10">
                            <textarea class="textarea form-control"  name="executorParam" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_executorparam}" maxlength="512" style="height: 80px !important; line-height: 1.2;"></textarea>
                        </div>
                    </div>
                    <br>
                    <p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_schedule}</p>
                    <div class="col-12">
                        <div class="form-group input-group">
                            <label for="scheduleType" class="col-sm-2 control-label">${I18n.schedule_type}<font color="red">*</font></label>
                            <div class="col-sm-4">
                                <select class="form-control scheduleType" name="scheduleType">
                                    <option value="CRON">CRON</option>
                                    <option value="FIX_RATE">FIX_RATE</option>
                                    <option value="FIX_DELAY">FIX_DELAY</option>
                                    <option value="NONE">NONE</option>
                                </select>
                            </div>

                        </div>
                    </div>
                    <div class="col-12">

                        <div class="schedule_conf schedule_conf_CRON form-group input-group" >
                            <label for="schedule_conf_CRON" class="col-sm-2 control-label">CRON<font color="red">*</font></label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="schedule_conf_CRON" name="schedule_conf_CRON" placeholder="${I18n.system_please_input}Cron" maxlength="128" >
                            </div>
                            <input type="hidden" name="scheduleConf" />
                        </div>

                        <div class="schedule_conf schedule_conf_NONE" style="display: none" ></div>

                        <div class="schedule_conf schedule_conf_FIX_RATE form-group input-group" style="display: none">
                            <label for="schedule_conf_FIX_RATE" class="col-sm-2 control-label">${I18n.schedule_type_fix_rate}<font color="red">*</font></label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="schedule_conf_FIX_RATE" placeholder="${I18n.system_please_input} （ Second ）" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" >
                            </div>
                        </div>
                        <div class="schedule_conf schedule_conf_FIX_DELAY form-group input-group" style="display: none" >
                            <label for="schedule_conf_FIX_DELAY" class="col-sm-2 control-label">${I18n.schedule_type_fix_delay}<font color="red">*</font></label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="schedule_conf_FIX_DELAY" placeholder="${I18n.system_please_input} （ Second ）" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" >
                            </div>
                        </div>
                    </div>
                    <br>
                    <p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_advanced}</p>    <#-- 高级配置 -->

                    <div class="form-group input-group">
                        <label for="executorRouteStrategy" class="col-sm-2 control-label">${I18n.jobinfo_field_executorRouteStrategy}<font color="black">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control" name="executorRouteStrategy" >
                                <option value="FIRST">FIRST</option>
                                <option value="LAST">LAST</option>
                                <option value="ROUND">ROUND</option>
                                <option value="RANDOM">RANDOM</option>
                                <option value="CONSISTENT_HASH">CONSISTENT_HASH</option>
                                <option value="LEAST_FREQUENTLY_USED">LEAST_FREQUENTLY_USED</option>
                                <option value="LEAST_RECENTLY_USED">LEAST_RECENTLY_USED</option>
                                <option value="FAILOVER">FAILOVER</option>
                                <option value="BUSYOVER">BUSYOVER</option>
                                <option value="SHARDING_BROADCAST">SHARDING_BROADCAST</option>
                            </select>
                        </div>

                        <label for="childJobId" class="col-sm-2 control-label">${I18n.jobinfo_field_childJobId}<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="childJobId" placeholder="${I18n.jobinfo_field_childJobId_placeholder}" maxlength="100" ></div>
                    </div>

                    <div class="form-group input-group">
                        <label for="misfireStrategy" class="col-sm-2 control-label">${I18n.misfire_strategy}<font color="black">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control" name="misfireStrategy" >
                                <option value="DO_NOTHING">DO_NOTHING</option>
                                <option value="FIRE_ONCE_NOW">FIRE_ONCE_NOW</option>
                            </select>
                        </div>

                        <label for="executorBlockStrategy" class="col-sm-2 control-label">${I18n.jobinfo_field_executorBlockStrategy}<font color="black">*</font></label>
                        <div class="col-sm-4">
                            <select class="form-control" name="executorBlockStrategy" >
                                <option value="SERIAL_EXECUTION">SERIAL_EXECUTION</option>
                                <option value="DISCARD_LATER">DISCARD_LATER</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group input-group">
                        <label for="executorTimeout" class="col-sm-2 control-label">${I18n.jobinfo_field_timeout}<font color="black">*</font></label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="executorTimeout" placeholder="${I18n.jobinfo_field_executorTimeout_placeholder}" maxlength="6" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" >
                        </div>
                        <label for="executorFailRetryCount" class="col-sm-2 control-label">${I18n.jobinfo_field_executorFailRetryCount}<font color="black">*</font></label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="executorFailRetryCount" placeholder="${I18n.jobinfo_field_executorFailRetryCount_placeholder}" maxlength="4" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" >
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
                    </div>
				</form>
         	</div>
		</div>
	</div>
</div>


<#-- trigger -->
<div class="modal fade" id="jobTriggerModal" role="dialog"  aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >${I18n.jobinfo_opt_run}</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <input hidden name="id">
                    <div class="form-group">
                        <label for="executorParam" class="col-sm-2 control-label">${I18n.jobinfo_field_executorparam}<font color="black">*</font></label>
                        <div class="col-sm-10">
                            <textarea class="textarea form-control" name="executorParam" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_executorparam}" maxlength="512" style="height: 63px; line-height: 1.2;"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="clientUrl" class="col-sm-2 control-label">${I18n.jobgroup_field_registryList}<font color="black">*</font></label>
                        <div class="col-sm-10">
                            <textarea class="textarea form-control" name="clientUrl" placeholder="${I18n.jobinfo_opt_run_tips}" maxlength="512" style="height: 63px; line-height: 1.2;"></textarea>
                        </div>
                    </div>
                    <hr>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary" id="executeOnce"  >${I18n.system_save}</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/jszip/jszip.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-buttons/js/buttons.print.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-buttons/js/buttons.colVis.min.js"></script>

<#-- cronGen -->
<script src="${request.contextPath}/static/plugins/cronGen/cronGen.js"></script>
<script src="${request.contextPath}/static/js/task.js"></script>
</body>
</html>
