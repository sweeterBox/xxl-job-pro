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
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "user" />

	<div class="content-wrapper">
		<!-- Main content -->
	    <section class="content">
			<div class="container-fluid">

			<div class="row">
                <div class="col-2">
                    <div class="input-group ">
                        <input type="text" class="form-control" id="username" autocomplete="on" placeholder="用户名" aria-label="username">
                    </div>
                </div>
				<div class="col-2">
					<div class="input-group ">
						<span class="input-group-text" id="inputGroup-sizing-lg">角色</span>
						<select class="form-control" id="role">
							<option disabled selected style="display: none" value="">请选择角色</option>
							<option value="1">管理员</option>
							<option value="0">普通</option>
						</select>
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
						<button class="btn btn-block btn-primary  add" type="button">${I18n.jobinfo_field_add}</button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-12">
					<div class="card">
			            <div class="card-body" >
			              	<table id="user_list" class="table table-bordered table-hover" width="100%" >
				                <thead>
					            	<tr>
                                        <th name="id" >ID</th>
                                        <th name="username" >${I18n.user_username}</th>
					                  	<th name="password" >${I18n.user_password}</th>
                                        <th name="role" >${I18n.user_role}</th>
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
<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >新增</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_username}<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="username" placeholder="${I18n.system_please_input}${I18n.user_username}" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_password}<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="password" placeholder="${I18n.system_please_input}${I18n.user_password}" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_role}<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="role" value="0" checked />${I18n.user_role_normal}
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="role" value="1" />${I18n.user_role_admin}
                        </div>
                    </div>
<#--                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_permission}<font color="black">*</font></label>
                        <div class="col-sm-10">
							<#if groupList?exists && groupList?size gt 0>
								<#list groupList as item>
                                    <input type="checkbox" name="permission" value="${item.id}" />${item.title}(${item.appname})<br>
								</#list>
							</#if>
                        </div>
                    </div>-->

					<div class="modal-footer">
						<button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>

<!-- 更新.模态框 -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >编辑</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<input name="id" hidden>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_username}<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="username" placeholder="${I18n.system_please_input}${I18n.user_username}" maxlength="20" readonly ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_password}<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="password" placeholder="${I18n.user_password_update_placeholder}" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_role}<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="role" value="0" />${I18n.user_role_normal}
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="role" value="1" />${I18n.user_role_admin}
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
<!-- AdminLTE App -->
<script src="${request.contextPath}/static/js/user.js"></script>
</body>
</html>
