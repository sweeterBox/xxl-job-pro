<!DOCTYPE html>
<html>
<head>
  	<#import "./common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <!-- daterangepicker -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition sidebar-mini layout-footer-fixed control-sidebar-slide-open layout-fixed layout-navbar-fixed">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "dashboard" />

	<div class="content-wrapper">
		<section class="content">
            <div class="row" style="margin-top: 30px;">
                <div class="col-12">
                    <div class="info-box mb-3 info-box-card">
                        <div class="info-box-content">
                            <span class="info-box-text">OS</span>
                            <span class="info-box-number" id="osName">-</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">CPU核数</span>
                            <span class="info-box-number" id="cpuCoreSize">-</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">线程数量</span>
                            <span class="info-box-number" id="totalThread">-</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">PID</span>
                            <span class="info-box-number" id="pid">-</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">启动时间</span>
                            <span class="info-box-number" id="startTime">-</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">运行时长</span>
                            <span class="info-box-number" id="runTimeLength">-</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" style="margin-top: 30px">
                <div class="col-md-4 col-sm-6 col-12">
                    <div class="info-box info-box-card">
                        <span class="info-box-icon" style="width: 100px">堆内存</span>
                        <div class="info-box-content">
                            <span class="info-box-text">初始</span>
                            <span class="info-box-number" id="initHeapMemorySize">-</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">已使用</span>
                            <span class="info-box-number" id="usedHeapMemorySize">-</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">最大可用</span>
                            <span class="info-box-number" id="maxHeapMemorySize">-</span>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 col-sm-6 col-12">
                    <div class="info-box info-box-card">
                        <span class="info-box-icon" style="width: 100px">非堆内存</span>
                        <div class="info-box-content">
                            <span class="info-box-text">初始</span>
                            <span class="info-box-number" id="initNonHeapMemorySize">-</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">已使用</span>
                            <span class="info-box-number" id="usedNonHeapMemorySize">-</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">最大可用</span>
                            <span class="info-box-number" id="maxNonHeapMemorySize">-</span>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 col-sm-6 col-12">
                    <div class="info-box info-box-card">
                        <span class="info-box-icon" style="width: 100px">OS内存</span>
                        <div class="info-box-content">
                            <span class="info-box-text">总共</span>
                            <span class="info-box-number" id="totalPhysicalMemorySize">-</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">可用</span>
                            <span class="info-box-number" id="freePhysicalMemorySize">-</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">已使用</span>
                            <span class="info-box-number" id="usedPhysicalMemorySize">-</span>
                        </div>
                    </div>
                </div>

            </div>

            <div class="row" style="margin-top: 30px;">

                <div class="col-md-4 col-sm-6 col-12">
                    <div class="info-box  bg-gradient-info info-box-card">
                        <span class="info-box-icon" style="width: 100px">
                            <i class="fas fa-tasks"></i>任务数量
                        </span>
                        <div class="info-box-content">
                            <span class="info-box-text">全部</span>
                            <span class="info-box-number" id="taskAllNum">0</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">运行中</span>
                            <span class="info-box-number" id="taskRunningNum">0</span>
                        </div>
                    </div>
                </div>

                <div class="col-md-4 col-sm-6 col-12">
                    <div class="info-box bg-gradient-success info-box-card">
                        <span class="info-box-icon" style="width: 100px"><i class="fas fa-tag"></i>调度次数</span>
                        <div class="info-box-content">
                            <span class="info-box-text">成功</span>
                            <span class="info-box-number" id="triggerSuccessNum">0</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">全部</span>
                            <span class="info-box-number" id="triggerAllNum">0</span>
                        </div>
                    </div>
                </div>

                <div class="col-md-4 col-sm-6 col-12">
                    <div class="info-box bg-gradient-warning info-box-card">
                        <span class="info-box-icon"><i class="fas fa-paperclip"></i>实例数量</span>
                        <div class="info-box-content">
                            <span class="info-box-text">在线</span>
                            <span class="info-box-number" id="instanceUpNum">0</span>
                        </div>
                        <div class="info-box-content">
                            <span class="info-box-text">全部</span>
                            <span class="info-box-number" id="instanceAllNum">0</span>
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

<#--                        <div class="card-footer bg-transparent">
                            这里写一些说明
                        </div>-->
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

<#--                        <div class="card-footer bg-transparent">
                            这里写一些说明
                        </div>-->
                    </div>
                </section>
            </div>

		</section>
	</div>
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
