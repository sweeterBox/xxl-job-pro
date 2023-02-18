<#macro commonStyle>

	<#-- favicon -->
	<link rel="icon" href="${request.contextPath}/static/favicon.ico" />
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<script src="${request.contextPath}/static/adminlte/plugins/jquery/jquery.min.js"></script>
	<!-- jQuery UI 1.11.4 -->
	<script src="${request.contextPath}/static/adminlte/plugins/jquery-ui/jquery-ui.min.js"></script>

	<!-- Google Font: Source Sans Pro -->
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
	<!-- Font Awesome -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/fontawesome-free/css/all.min.css">
	<!-- Ionicons -->
	<link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
	<!-- Tempusdominus Bootstrap 4 -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css">
	<!-- iCheck -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
	<!-- JQVMap -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/jqvmap/jqvmap.min.css">
	<!-- Theme style -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/dist/css/adminlte.min.css">
	<!-- overlayScrollbars -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/overlayScrollbars/css/OverlayScrollbars.min.css">
	<!-- Daterange picker -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/daterangepicker/daterangepicker.css">
	<!-- summernote -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/summernote/summernote-bs4.min.css">

	<link rel="stylesheet" href="${request.contextPath}/static/css/theme-v1.0.css">


	<#-- i18n -->
	<#global I18n = I18nUtil.getMultString()?eval />
</#macro>

<#macro commonScript>
    <script>
		var base_url = '${request.contextPath}';
        var I18n = ${I18nUtil.getMultString()};
	</script>

	<!-- Bootstrap 4 -->
	<script src="${request.contextPath}/static/adminlte/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<!-- ChartJS -->
	<script src="${request.contextPath}/static/adminlte/plugins/chart.js/Chart.min.js"></script>
	<!-- Sparkline -->
	<script src="${request.contextPath}/static/adminlte/plugins/sparklines/sparkline.js"></script>
	<!-- JQVMap -->
	<script src="${request.contextPath}/static/adminlte/plugins/jqvmap/jquery.vmap.min.js"></script>
	<script src="${request.contextPath}/static/adminlte/plugins/jqvmap/maps/jquery.vmap.usa.js"></script>
	<!-- jQuery Knob Chart -->
	<script src="${request.contextPath}/static/adminlte/plugins/jquery-knob/jquery.knob.min.js"></script>
	<script src="${request.contextPath}/static/adminlte/plugins/jquery-validation/jquery.validate.min.js"></script>
	<script src="${request.contextPath}/static/adminlte/plugins/jquery-validation/additional-methods.min.js"></script>
	<!-- daterangepicker -->
	<script src="${request.contextPath}/static/adminlte/plugins/moment/moment.min.js"></script>
	<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/daterangepicker.js"></script>
	<!-- Tempusdominus Bootstrap 4 -->
	<script src="${request.contextPath}/static/adminlte/plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js"></script>
	<!-- Summernote -->
	<script src="${request.contextPath}/static/adminlte/plugins/summernote/summernote-bs4.min.js"></script>
	<!-- overlayScrollbars -->
	<script src="${request.contextPath}/static/adminlte/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"></script>
	<!-- AdminLTE App -->
	<script src="${request.contextPath}/static/adminlte/dist/js/adminlte.js"></script>

	<script type="text/javascript" src="${request.contextPath}/static/js/pxmu.min.js"></script>
	<script src="${request.contextPath}/static/js/toasts.js"></script>
	<script src="${request.contextPath}/static/js/common.js"></script>

</#macro>

<#macro commonHeader>
		<nav class="main-header navbar navbar-expand navbar-white navbar-light">
			<!-- Left navbar links -->
			<ul class="navbar-nav" id="collapse">
				<li class="nav-item">
					<a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
				</li>
				<li class="nav-item d-none d-sm-inline-block">
					<a href="${request.contextPath}/" class="nav-link">Home</a>
				</li>
<#--				<li class="nav-item d-none d-sm-inline-block">
					<a href="#" class="nav-link">Contact</a>
				</li>-->
			</ul>

			<!-- Right navbar links -->
			<ul class="navbar-nav ml-auto">
				<li class="nav-item">
					<a class="nav-link" data-widget="fullscreen" href="#" role="button">
						<i class="fas fa-expand-arrows-alt"></i>
					</a>
				</li>
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown2" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						${I18n.system_welcome} ${Request["XXL_JOB_LOGIN_IDENTITY"].username}
					</a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown2">
						<a id="updatePwd" class="dropdown-item" href="javascript:">${I18n.change_pwd}</a>
						<div class="dropdown-divider"></div>
						<a id="logoutBtn" class="dropdown-item" href="javascript:">${I18n.logout_btn}</a>
					</div>
				</li>
			</ul>
		</nav>

	<!-- 修改密码.模态框 -->
	<div class="modal fade" id="updatePwdModal" tabindex="-1" role="dialog"  aria-hidden="true">
		<div class="modal-dialog ">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" >${I18n.change_pwd}</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal form" role="form" >
						<div class="form-group">
							<label for="lastname" class="col-sm-2 control-label">${I18n.change_pwd_field_newpwd}<font color="red">*</font></label>
							<div class="col-sm-10">
								<input type="text" class="form-control" name="password" placeholder="${I18n.system_please_input} ${I18n.change_pwd_field_newpwd}" maxlength="18" >
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

</#macro>

<#macro commonLeft menuCode >
	<!-- Left side column. contains the logo and sidebar -->
<#--
	<aside class="main-sidebar sidebar-dark-primary elevation-4">
-->
	<aside class="main-sidebar elevation-4 sidebar-light-teal">
		<!-- Brand Logo -->
		<a href="${request.contextPath}/" class="brand-link" style="text-align: center;min-height: 50px">
			<img src="static/images/xxl-logo.jpg" alt="Logo" class="brand-image img-circle elevation-3" style="opacity: .8">
			<span class="brand-text" style="font-weight: bolder !important;font-size: 28px  !important">XXL-JOB Pro</span>
		</a>

		<!-- Sidebar -->
		<div class="sidebar os-host os-host-resize-disabled os-host-transition os-host-overflow os-host-overflow-y os-host-scrollbar-horizontal-hidden os-theme-light">
			<nav class="mt-2">
			<!-- sidebar menu: : style can be found in sidebar.less -->
			<ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false" id="menus" data-menucode="${menuCode}">
<#--                <li class="nav-item">
					<a href="${request.contextPath}/" class="nav-link <#if pageName == "index">active</#if>" >
						<i class="nav-icon fas fa-tachometer-alt"></i>
						<p>${I18n.job_dashboard_name}</p>
					</a>
				    </li>
-->
			</ul>
			</nav>
		</div>
	</aside>
</#macro>


<#macro commonFooter >
	<footer class="main-footer">
		<strong>Copyright &copy; 2023-${.now?string('yyyy')} <a href="">XXL-JOB Pro</a>.</strong>
		All rights reserved.
		<div class="float-right d-none d-sm-inline-block">
			<b>version</b> ${I18n.admin_version}
		</div>
	</footer>
</#macro>
