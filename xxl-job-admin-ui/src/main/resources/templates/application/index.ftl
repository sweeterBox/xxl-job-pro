<!DOCTYPE html>
<html>
<head>
  	<#import "../common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/fontawesome-free/css/all.min.css">
    <!-- DataTables -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "application" />

	<div class="content-wrapper">
        <section class="content">
            <div class="container-fluid">
            <div class="row">
                <div class="col-2">
                    <div class="input-group ">
                        <input type="text" class="form-control"  id="name" placeholder="应用标识" aria-label="name">
                    </div>
                </div>
                <div class="col-2">
                    <div class="input-group ">
                        <input type="text" class="form-control" id="title" placeholder="应用名称" aria-label="title">
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
                    <div class="input-group ">
                        <button class="btn btn-block btn-primary  add" type="button" id="add">${I18n.jobinfo_field_add}</button>
                    </div>
                </div>
            </div>
             <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body" >
                                <table id="tab" class="table table-bordered table-hover" width="100%" >
				                <thead>
					            	<tr>
                                        <th name="id">ID</th>
                                        <th name="name">应用标识</th>
                                        <th name="title">应用名称</th>
                                        <th name="instanceAllSize">实例数</th>
                                        <th name="instanceHealthySize">健康实例数</th>
                                        <th>操作</th>
					                </tr>
				                </thead>
                                <tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			</div>
	    </section>
	</div>

    <!-- 新增.模态框 -->
    <div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">新增</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal form" role="form" >
                        <div class="col-10">
                            <div class=" form-group input-group ">
                                <span class="input-group-text" id="inputGroup-sizing-lg">应用标识<font color="red">*</font></span>
                                <input type="text" class="form-control" name="name" placeholder="${I18n.system_please_input}应用标识" maxlength="64" >
                            </div>
                        </div>
                        <div class="col-10">
                            <div class="form-group input-group ">
                                <span class="input-group-text" id="inputGroup-sizing-lg">应用名称<font color="red">*</font></span>
                                <input type="text" class="form-control" name="title" placeholder="${I18n.system_please_input}应用名称" maxlength="12" >
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

	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script>

<script src="${request.contextPath}/static/js/application.js"></script>
</body>
</html>
