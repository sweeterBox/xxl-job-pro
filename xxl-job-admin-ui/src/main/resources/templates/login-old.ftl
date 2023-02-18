<!DOCTYPE html>
<html>
<head>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition login-page">
<div class="login-box">
    <div class="login-logo">
        <a><b>XXL-JOB</b> Pro</a>
    </div>
    <form id="loginForm" method="post" >
        <div class="card" style="border-radius: 6px">
            <div class="card-body login-card-body" style="border-radius: 6px">
                <p class="login-box-msg" style="font-weight: bolder;font-size:14px;padding: 0 0 10px;">分布式任务调度平台</p>
                <p class="login-box-msg" style="padding: 0 0 10px;">支持Spring Cloud微服务分布式架构</p>
                <div class="input-group input-group-lg mb-3">
                    <input type="text" name="userName" class="form-control " placeholder="用户名"  maxlength="18" />
                    <div class="input-group-append">
                        <div class="input-group-text">
                            <span class="fas fa-user"></span>
                        </div>
                    </div>
                </div>
                <div class=" input-group input-group-lg mb-3" style="margin-top: 20px" >
                    <input type="password" name="password" class="form-control" placeholder="密码"  maxlength="18" />
                    <div class="input-group-append">
                        <div class="input-group-text">
                            <span class="fas fa-lock"></span>
                        </div>
                    </div>
                </div>

                <div class="row" style="margin-top: 10px">
                    <div class="col-8">
                        <div class="checkbox icheck input-group input-group-lg">
                            <label>
                                <input type="checkbox" name="ifRemember" style="margin-right: 5px">记住密码
                            </label>
                        </div>
                    </div>
                </div>
                <div class="row text-center" style="margin-top: 10px">
                    <div class="input-group  mb-3">
                        <button type="submit" class="btn btn-primary btn-block btn-flat" style="padding: 5px;border-radius: 4px">登录</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/static/js/login.js"></script>
</body>
</html>
