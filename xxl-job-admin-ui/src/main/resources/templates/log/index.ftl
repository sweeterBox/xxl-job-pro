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
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "log" />

	<div class="content-wrapper">
	    <section class="content">
            <div class="container-fluid">

            <div class="row">
	    		<div class="col-2">
 					<div class="input-group ">
                        <span class="input-group-text" id="inputGroup-sizing-lg">应用</span>
                		<select class="form-control" id="allApplicationName">
                            <option disabled selected style="display: none" value="">请选择</option>

	                  	</select>
	              	</div>
	            </div>
	            <div class="col-2">
	              	<div class="input-group ">
                        <span class="input-group-text" id="inputGroup-sizing-lg">任务</span>
                        <input type="text" class="form-control"  id="taskId" placeholder="任务ID" aria-label="name">

<#--                        <select class="form-control" id="taskId">
                            <option value="" >${I18n.system_all}</option>
						</select>-->
	              	</div>
	            </div>

                <div class="col-2">
                    <div class="input-group ">
                        <span class="input-group-text" id="inputGroup-sizing-lg">${I18n.joblog_status}</span>
                        <select class="form-control" id="triggerStatus" >
                            <option value="" >${I18n.joblog_status_all}</option>
                            <option value="1" >${I18n.joblog_status_suc}</option>
                            <option value="2" >${I18n.joblog_status_fail}</option>
                            <option value="3" >${I18n.joblog_status_running}</option>
                        </select>
                    </div>
                </div>

	            <div class="col-3">
              		<div class="input-group">
                        <span class="input-group-text" id="inputGroup-sizing-lg">${I18n.joblog_field_triggerTime}</span>
	                	<input type="text" class="form-control" id="filterTime" readonly >
	              	</div>
	            </div>
                <div class="col-1">
                    <div class="input-group ">
                        <button class="btn btn-block  btn-search" type="button" id="searchBtn">${I18n.system_search}</button>
                    </div>
                </div>
            </div>

<#--            <div class="row">
                <div class="col-1">
                    <div class="input-group ">
                        <button class="btn btn-block  btn-secondary" type="button" id="clearLog">${I18n.joblog_clean}</button>
                    </div>
                </div>
            </div>-->

                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body" >
                              <table id="tab" class="table table-bordered table-hover" width="100%" >
				                <thead>
					            	<tr>
                                        <th name="taskId" >任务ID</th>
                                        <th name="applicationName">应用</th>
                                        <th name="instanceUrl">实例URL</th>
                                        <th name="triggerTime">调度时间</th>
                                        <th name="triggerStatus">调度状态</th>
                                        <th name="triggerContent">调度结果</th>
					                  	<th name="handleStartTime">执行开始时间</th>
					                  	<th name="handleEndTime">执行结束时间</th>
					                  	<th name="handleStatus">执行状态</th>
					                  	<th name="handleContent">执行结果</th>
					                  	<th name="opt">操作</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
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

<!-- 日志清理.模态框 -->
<div class="modal fade" id="clearLogModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >${I18n.joblog_clean_log}</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label class="col-sm-3 control-label">${I18n.jobinfo_field_jobgroup}：</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control jobGroupText" readonly >
							<input type="hidden" name="jobGroup" >
						</div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">${I18n.jobinfo_job}：</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control jobIdText" readonly >
                            <input type="hidden" name="jobId" >
						</div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">${I18n.joblog_clean_type}：</label>
                        <div class="col-sm-9">
                            <select class="form-control" name="type" >
                                <option value="1" >${I18n.joblog_clean_type_1}</option>
                                <option value="2" >${I18n.joblog_clean_type_2}</option>
                                <option value="3" >${I18n.joblog_clean_type_3}</option>
                                <option value="4" >${I18n.joblog_clean_type_4}</option>
                                <option value="5" >${I18n.joblog_clean_type_5}</option>
                                <option value="6" >${I18n.joblog_clean_type_6}</option>
                                <option value="7" >${I18n.joblog_clean_type_7}</option>
                                <option value="8" >${I18n.joblog_clean_type_8}</option>
                                <option value="9" >${I18n.joblog_clean_type_9}</option>
                            </select>
                        </div>
                    </div>

                    <hr>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button type="button" class="btn btn-primary ok" >${I18n.system_ok}</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<@netCommon.commonScript />
<!-- DataTables -->
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
<!-- daterangepicker -->
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<script src="${request.contextPath}/static/js/log.js"></script>
</body>
</html>
