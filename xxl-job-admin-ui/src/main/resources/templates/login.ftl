<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="UTF-8">
	<link rel="icon" href="static/favicon.ico" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>XXL-JOB Pro Admin</title>
	<#--copy from 作者：山羊の前端小窝 https://www.bilibili.com/read/cv21632661 出处：bilibili-->
	<style>
		* {
			margin: 0;
			padding: 0;
			box-sizing: border-box;
			/* 字体无法选中 */
			user-select: none;
		}

		body {
			width: 100%;
			height: 100vh;
			display: flex;
			justify-content: center;
			align-items: center;
			font-size: 12px;
			background-color: #ecf0f3;
			color: #a0a5a8;
		}

		.shell {
			position: relative;
			width: 800px;
			min-width: 800px;
			min-height: 500px;
			height: 500px;
			padding: 25px;
			background-color: #ecf0f3;
			box-shadow: 10px 10px 10px #d1d9e6, -10px -10px 10px #f9f9f9;
			border-radius: 12px;
			overflow: hidden;
		}

		/* 设置响应式 */
		@media (max-width: 1200px) {
			.shell {
				transform: scale(0.7);
			}
		}

		@media (max-width: 1000px) {
			.shell {
				transform: scale(0.6);
			}
		}

		@media (max-width: 800px) {
			.shell {
				transform: scale(0.5);
			}
		}

		@media (max-width: 600px) {
			.shell {
				transform: scale(0.4);
			}
		}

		.container {
			display: flex;
			justify-content: center;
			align-items: center;
			position: absolute;
			top: 0;
			width: 600px;
			height: 100%;
			padding: 10px;
			background-color: #ecf0f3;
			transition: 1.25s;
		}

		.form {
			display: flex;
			justify-content: center;
			align-items: center;
			flex-direction: column;
			width: 100%;
			height: 100%;
		}


		.form_input {
			width: 300px;
			height: 40px;
			margin: 4px 0;
			padding-left: 25px;
			font-size: 13px;
			letter-spacing: 0.15px;
			border: none;
			outline: none;
			background-color: #ecf0f3;
			transition: 0.25s ease;
			border-radius: 8px;
			box-shadow: inset 2px 2px 4px #d1d9e6, inset -2px -2px 4px #f9f9f9;
		}

		.form_input:focus {
			box-shadow: inset 4px 4px 4px #d1d9e6, inset -4px -4px 4px #f9f9f9;
		}

		.form_span {
			margin-top: 30px;
			margin-bottom: 12px;
		}

		.form_link {
			color: #181818;
			font-size: 15px;
			margin-top: 25px;
			border-bottom: 1px solid #a0a5a8;
			line-height: 2;
		}

		.title {
			font-size: 34px;
			font-weight: 700;
			line-height: 3;
			color: #181818;
			letter-spacing: 10px;
		}

		.description {
			font-size: 14px;
			letter-spacing: 0.25px;
			text-align: center;
			line-height: 2;
			font-weight: bolder;
		}

		.button {
			width: 180px;
			height: 50px;
			border-radius: 25px;
			margin-top: 50px;
			font-weight: 700;
			font-size: 14px;
			letter-spacing: 1.15px;
			background-color: #4B70E2;
			color: #f9f9f9;
			box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #f9f9f9;
			border: none;
			outline: none;
		}

		.a-container {
			z-index: 100;
			left: calc(100% - 500px);
		}


		.switch {
			display: flex;
			justify-content: center;
			align-items: center;
			position: absolute;
			top: 0;
			left: 0;
			height: 100%;
			width: 350px;
			padding: 50px;
			z-index: 200;
			transition: 1.25s;
			background-color: #ecf0f3;
			overflow: hidden;
			box-shadow: 4px 4px 10px #d1d9e6, -4px -4px 10px #d1d9e6;
		}

		.switch_circle {
			position: absolute;
			width: 250px;
			height: 250px;
			border-radius: 50%;
			background-color: #ecf0f3;
			box-shadow: inset 8px 8px 12px #b8bec7, inset -8px -8px 12px #fff;
			bottom: -30%;
			left: -40%;
			transition: 1.25s;
		}

		.switch_circle-t {
			top: -30%;
			left: 70%;
			width: 250px;
			height: 250px;
		}

		.switch_container {
			display: flex;
			justify-content: center;
			align-items: center;
			flex-direction: column;
			position: absolute;
			width: 400px;
			padding: 50px 55px;
			transition: 1.25s;
		}

		.submit:hover {
			box-shadow: 6px 6px 10px #d1d9e6, -6px -6px 10px #f9f9f9;
			transform: scale(0.985);
			transition: 0.25s;
			cursor:pointer;
		}

		@keyframes is-gx {
			0%,
			10%,
			100% {
				width: 400px;
			}
			30%,
			50% {
				width: 500px;
			}
		}
	</style>
</head>

<body>
<div class="shell">
	<div class="container a-container" id="a-container">
		<form action="" id="loginForm" method="post" class="form" id="a-form">
			<h2 class="form_title title">任务调度管理平台</h2>
			<input type="text" name="userName" class="form_input" placeholder="用户名">
			<input type="password" name="password" class="form_input"  placeholder="密码">
<#--
			<a class="form_link">忘记密码？</a>
-->
			<button class="form_button button submit" id="submit">登录</button>
		</form>
	</div>


	<div class="switch" id="switch-cnt">
		<div class="switch_circle"></div>
		<div class="switch_circle switch_circle-t"></div>
		<div class="switch_container" id="switch-c1">
			<h2 class="switch_title title" style="letter-spacing: 0;">XXL-JOB Pro </h2>
			<p class="switch_description description">分布式任务调度平台</p>
			<p class="switch_description description">支持Spring Cloud微服务分布式架构</p>
			<p class="switch_description description" style="color: red">内部系统，切勿暴露到公网</p>
		</div>
	</div>
</div>

<script src="${request.contextPath}/static/adminlte/plugins/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/pxmu.min.js"></script>

<script>
	let switchCtn = document.querySelector("#switch-cnt");
	let switchC1 = document.querySelector("#switch-c1");
	let switchC2 = document.querySelector("#switch-c2");
	let switchCircle = document.querySelectorAll(".switch_circle");
	let switchBtn = document.querySelectorAll(".switch-btn");
	let aContainer = document.querySelector("#a-container");
	let bContainer = document.querySelector("#b-container");
	let allButtons = document.querySelectorAll(".submit");

	let getButtons = (e) => e.preventDefault()
	let changeForm = (e) => {
		// 修改类名
		switchCtn.classList.add("is-gx");
		setTimeout(function () {
			switchCtn.classList.remove("is-gx");
		}, 1500)
		switchCtn.classList.toggle("is-txr");
		switchCircle[0].classList.toggle("is-txr");
		switchCircle[1].classList.toggle("is-txr");

		switchC1.classList.toggle("is-hidden");
		switchC2.classList.toggle("is-hidden");
		aContainer.classList.toggle("is-txl");
		bContainer.classList.toggle("is-txl");
		bContainer.classList.toggle("is-z");
	}
	// 点击切换
	let shell = (e) => {
		for (var i = 0; i < allButtons.length; i++)
			allButtons[i].addEventListener("click", getButtons);
		for (var i = 0; i < switchBtn.length; i++)
			switchBtn[i].addEventListener("click", changeForm)
	}
	window.addEventListener("load", shell);

	$('#submit').on('click', function(){
		let userName = $("#loginForm input[name='userName']").val();
		let password = $("#loginForm input[name='password']").val();
		if (userName && userName != '') {

		}else {
			pxmu.toast({
				msg: '请输入用户名',
				time: 2500,
				bg: 'rgba(0, 0, 0, 0.86)',
				color: '#fff',
				location: 'top',
				animation: 'fade',
				type: 'pc',
				status: 'error',
			});
			return;
		}
		if (password && password != '') {

		}else {
			pxmu.toast({
				msg: '请输入密码',
				time: 2500,
				bg: 'rgba(0, 0, 0, 0.86)',
				color: '#fff',
				location: 'top',
				animation: 'fade',
				type: 'pc',
				status: 'error',
			});
			return;
		}
		$.ajax({
			type : 'POST',
			data: {
				userName: userName,
				password: password
			},
			url : 'v1.0/auth/login',
			success : function(data){
				if (data && data.success) {
					localStorage.setItem("userInfo", JSON.stringify(data.user));

					pxmu.toast({
						msg: '登录成功',
						time: 2500,
						bg: 'rgba(0, 0, 0, 0.86)',
						color: '#fff',
						location: 'top',
						animation: 'fade',
						type: 'pc',
						status: 'success',
					});
					setTimeout(function () {
						window.location.href = "index";
					}, 50);
				} else {
					pxmu.toast({
						msg: '用户名或密码错误',
						time: 2500,
						bg: 'rgba(0, 0, 0, 0.86)',
						color: '#fff',
						location: 'top',
						animation: 'fade',
						type: 'pc',
						status: 'error',
					});
				}
			}
		});
	});
</script>

</body>

</html>
