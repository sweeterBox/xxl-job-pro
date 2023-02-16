<!DOCTYPE html>
<html>
<head>
  	<#import "./common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <!-- daterangepicker -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "dashboard" />

	<div class="content-wrapper">
		<section class="content">
            <!-- 任务信息 -->
            <div class="row" style="margin-top: 30px;">
                <#-- 任务信息 -->
                <div class="col-md-4 col-sm-6 col-12">
                    <div class="info-box bg-gradient-info">
                        <span class="info-box-icon">
                            <i class="fas fa-tasks  "></i>
                        </span>
                        <div class="info-box-content">
                            <span class="info-box-text">${I18n.job_dashboard_job_num}</span>
                            <p>
                                <span class="" id="taskRunningNum">0</span>/
                                <span class="" id="taskAllNum">0</span>
                            </p>

                            <div class="progress">
                                <div class="progress-bar" style="width: 100%"></div>
                            </div>
                            <span class="progress-description">${I18n.job_dashboard_job_num_tip}</span>
                        </div>
                    </div>
                </div>
                <#-- 调度信息 -->
                <div class="col-md-4 col-sm-6 col-12">
                    <div class="info-box bg-gradient-success">
                        <span class="info-box-icon"><i class="fas fa-tag"></i></span>
                        <div class="info-box-content">
                            <span class="info-box-text">${I18n.job_dashboard_trigger_num}</span>
                            <p>
                                <span class="" id="triggerSuccessNum">0</span>/
                                <span class="" id="triggerAllNum">0</span>
                            </p>

                            <div class="progress">
                                <div class="progress-bar" style="width: 100%"></div>
                            </div>
                            <span class="progress-description">${I18n.job_dashboard_trigger_num_tip}</span>
                        </div>
                    </div>
                </div>

                <#-- 执行器 -->
                <div class="col-md-4 col-sm-6 col-12">
                    <div class="info-box bg-gradient-warning">
                        <span class="info-box-icon"><i class="fas fa-paperclip"></i></span>
                        <div class="info-box-content">
                            <span class="info-box-text">${I18n.job_dashboard_jobgroup_num}</span>
                            <p>
                                <span class="" id="instanceUpNum">0</span>/
                                <span class="" id="instanceAllNum">0</span>
                            </p>


                            <div class="progress">
                                <div class="progress-bar" style="width: 100%"></div>
                            </div>
                            <span class="progress-description">${I18n.job_dashboard_jobgroup_num_tip}</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" style="margin-top: 30px;">
                <section class="col-lg-8 connectedSortable">
                    <div class="card">
                        <div class="card-header border-0">
                            <h3 class="card-title">
                                <i class="fas fa-th mr-1"></i>
                                ${I18n.job_dashboard_report}
                            </h3>
                            <div class="card-tools">
                                <button type="button" class="btn btn-primary btn-sm daterange pull-right" data-toggle="tooltip" id="filterTime" >
                                    <i class="fa fa-calendar"></i>
                                </button>

                                <button type="button" class="btn bg-info btn-sm" data-card-widget="collapse">
                                    <i class="fas fa-minus"></i>
                                </button>
                                <button type="button" class="btn bg-info btn-sm" data-card-widget="remove">
                                    <i class="fas fa-times"></i>
                                </button>
                            </div>
                        </div>
                        <div class="card-body">
                            <div id="lineChart" style="height: 350px;"></div>
                        </div>

                        <div class="card-footer bg-transparent">
                            这里写一些说明
                        </div>
                    </div>
                </section>
                <section class="col-lg-4 connectedSortable">


                <div class="card">
                        <div class="card-header border-0">
                            <h3 class="card-title">
                                <i class="fas fa-th mr-1"></i>
                                ${I18n.job_dashboard_report}
                            </h3>
                            <div class="card-tools">
<#--                                <button type="button" class="btn btn-primary btn-sm daterange pull-right" data-toggle="tooltip" id="filterTime" >
                                    <i class="fa fa-calendar"></i>
                                </button>-->

                                <button type="button" class="btn bg-info btn-sm" data-card-widget="collapse">
                                    <i class="fas fa-minus"></i>
                                </button>
                                <button type="button" class="btn bg-info btn-sm" data-card-widget="remove">
                                    <i class="fas fa-times"></i>
                                </button>
                            </div>
                        </div>
                        <div class="card-body">
                            <div id="pieChart" style="height: 350px;"></div>
                        </div>

                        <div class="card-footer bg-transparent">
                            这里写一些说明
                        </div>
                    </div>
                </section>
            </div>
		</section>
		<!-- /.content -->
	</div>
	<!-- /.content-wrapper -->

	<!-- footer -->
	<@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
<!-- daterangepicker -->
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<#-- echarts -->
<script src="${request.contextPath}/static/plugins/echarts/echarts.common.min.js"></script>
<script src="${request.contextPath}/static/js/index.js"></script>
</body>
</html>
