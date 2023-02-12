$(function(){
	var loginFormValid = $("#loginForm").validate({
		errorElement : 'span',
        focusInvalid : true,
        rules : {
        	userName : {
        		required : true ,
                minlength: 4,
                maxlength: 18
            },
            password : {
            	required : true ,
                minlength: 4,
                maxlength: 18
            }
        },
        messages : {
        	userName : {
                required  : I18n.login_username_empty,
                minlength : I18n.login_username_lt_4
            },
            password : {
            	required  : I18n.login_password_empty  ,
                minlength : I18n.login_password_lt_4
                /*,maxlength:"登录密码不应超过18位"*/
            }
        },
        errorElement: 'span',
        errorPlacement: function (error, element) {
            error.addClass('invalid-feedback');
            element.closest('.form-group').append(error);
        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass('is-invalid');
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass('is-invalid');
        },
        success : function(label) {
            //label.closest('.form-group').removeClass('has-error');
           // label.remove();
        },
        submitHandler : function(form) {
			$.post(base_url + "/v1.0/auth/login", $("#loginForm").serialize(), function(data, status) {
                if (data && data.success) {
                    Toasts.success(I18n.login_success);
                    setTimeout(function () {
                        window.location.href = base_url + "/index";
                    }, 500);
                } else {
                    Toasts.error({title: I18n.system_tips, content: I18n.login_fail});
                }
			});
		}
	});

});





