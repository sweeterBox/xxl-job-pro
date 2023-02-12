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
    <style>
        .application-name {
            font-size: 2.5rem;
            font-weight: 400;
            line-height: 2.2;
            text-align: center;
        }
    </style>
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

                    <div class="col-1">
                        <div class="input-group ">
                            <button class="btn btn-block btn-back" type="button" id="back">返回</button>
                        </div>
                    </div>
                </div>

                <h1 class="application-name" id="name"></h1>

                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body" >
                                <table id="tab" class="table table-bordered table-hover" width="100%" >
                                    <thead>
                                    <tr>
                                        <th name="id">ID</th>
                                        <th name="name">实例名称</th>
                                        <th name="title">实例标题</th>
                                        <th name="healthy">健康</th>
                                        <th name="ephemeral">临时</th>
                                        <th name="weight">权重</th>
                                        <th name="host">host</th>
                                        <th name="port">port</th>
                                        <th name="url">url</th>
                                        <th name="status">状态</th>
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

<script src="${request.contextPath}/static/js/clientList.js"></script>
</body>
</html>
