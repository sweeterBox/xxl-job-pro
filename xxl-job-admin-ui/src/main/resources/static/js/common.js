function formatBytes(a,b=2,k=1024){with(Math){let d=floor(log(a)/log(k));return 0==a?"0 Bytes":parseFloat((a/pow(k,d)).toFixed(max(0,b)))+" "+["Bytes","KB","MB","GB","TB","PB","EB","ZB","YB"][d]}}

$.fn.serializeJson = function () {
    var serializeObj = {};
    var array = this.serializeArray();
    var str = this.serialize();
    $(array).each(function () {
        if (serializeObj[this.name]) {
            if ($.isArray(serializeObj[this.name])) {
                serializeObj[this.name].push(this.value);
            } else {
                serializeObj[this.name] = [serializeObj[this.name], this.value];
            }
        } else {
            serializeObj[this.name] = this.value;
        }
    });
    return serializeObj;
};


function initMenuActive() {
    //let menuCode = $("#menus").data("menucode");
    $('#'+$("#menus").data("menucode")).addClass('active');
}


$(function(){

    //加载菜单
    getMenus();

    function getMenus() {
        $.ajax({
            type : 'GET',
            url : base_url + '/menus',
            dataType : "json",
            success : function(data){
                if (data && Array.isArray(data)) {
                    localStorage.setItem("menus", JSON.stringify(data));

                    let html = "";
                    data.forEach(m => {
                        html += '<li class="nav-item">';
                        html += '<a href="' + m.url + '" class="nav-link" id="' + m.code + '" target="' + m.aTartget + '">';
                        html += '<i class="' + m.icon + '"></i>';
                        html += '<p>' + m.name + '</p>';
                        html += '</a>';
                        html += '</li>';
                    });
                    $('#menus').html(html);
                    initMenuActive();
                }
            }
        });

    }




/*    initMenus();
    function initMenus() {
        let menus = JSON.parse(localStorage.getItem("menus"));
        let html = "";
        menus.forEach(m => {
            html += '<li class="nav-item">';
            html += '<a href="' + m.url + '" class="nav-link" target="' + m.aTartget + '">';
            html += '<i class="' + m.icon + '"></i>';
            html += '<p>' + m.name + '</p>';
            html += '</a>';
            html += '</li>';
        });
        $('#menus').html(html);
    }
    */
	// logout
	$("#logoutBtn").click(function(){
        $.ajax({
            type : 'DELETE',
            url : base_url + "/v1.0/auth/logout",
            success : function(data){
                //删除缓存

                window.location.reload();
            }
        });
	});


	// slideToTop
	var slideToTop = $("<div />");
	slideToTop.html('<i class="fa fa-chevron-up"></i>');
	slideToTop.css({
		position: 'fixed',
		bottom: '20px',
		right: '25px',
		width: '40px',
		height: '40px',
		color: '#eee',
		'font-size': '',
		'line-height': '40px',
		'text-align': 'center',
		'background-color': '#222d32',
		cursor: 'pointer',
		'border-radius': '5px',
		'z-index': '99999',
		opacity: '.7',
		'display': 'none'
	});
	slideToTop.on('mouseenter', function () {
		$(this).css('opacity', '1');
	});
	slideToTop.on('mouseout', function () {
		$(this).css('opacity', '.7');
	});
	$('.wrapper').append(slideToTop);
	$(window).scroll(function () {
		if ($(window).scrollTop() >= 150) {
			if (!$(slideToTop).is(':visible')) {
				$(slideToTop).fadeIn(500);
			}
		} else {
			$(slideToTop).fadeOut(500);
		}
	});
	$(slideToTop).click(function () {
		$("html,body").animate({		// firefox ie not support body, chrome support body. but found that new version chrome not support body too.
			scrollTop: 0
		}, 100);
	});

	// left menu status v: js + server + cookie
	$('.sidebar-toggle').click(function(){
		var xxljob_adminlte_settings = $.cookie('xxljob_adminlte_settings');	// on=open，off=close
		if ('off' == xxljob_adminlte_settings) {
            xxljob_adminlte_settings = 'on';
		} else {
            xxljob_adminlte_settings = 'off';
		}
		$.cookie('xxljob_adminlte_settings', xxljob_adminlte_settings, { expires: 7 });	//$.cookie('the_cookie', '', { expires: -1 });
	});



    // update pwd
    $('#updatePwd').on('click', function(){
        $('#updatePwdModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var updatePwdModalValidate = $("#updatePwdModal .form").validate({
        errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
            password : {
                required : true ,
                rangelength:[4,50]
            }
        },
        messages : {
            password : {
                required : '请输入密码'  ,
                rangelength : "密码长度限制为4~50"
            }
        },
        highlight : function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success : function(label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement : function(error, element) {
            element.parent('div').append(error);
        },
        submitHandler : function(form) {

            $.ajax({
                type: 'PUT',
                url: base_url + "/v1.0/user/changePwd",
                data: {
                    password: $("#updatePwdModal .form input[name='password']").val(),
                },
                success: function (d) {
                    $.ajax({
                        type : 'DELETE',
                        url : base_url + "/v1.0/auth/logout",
                        success : function(data){
                            //删除缓存
                            localStorage.removeItem("userInfo");
                            window.location.reload();
                        }
                    });
                }
            });
        }
    });


    $("#collapse").click(function(){
        let bodyClass = $('body').attr('class');
        let collapse = bodyClass && bodyClass.search("sidebar-collapse") !== -1;
        localStorage.setItem("collapse", !collapse);
    });

    initCollapse();
    function initCollapse() {
        let collapse = localStorage.getItem("collapse");
        let bodyClass = $('body').attr('class');

        if (collapse && collapse == 'true') {
            $('body').addClass('sidebar-collapse');
        }else {
            let bodyCollapse = bodyClass && bodyClass.search("sidebar-collapse") !== -1;
            if (bodyCollapse) {
                $('body').removeClass('sidebar-collapse')
            }
        }
    }

    initUserInfo();
    function initUserInfo() {
        let userInfo = localStorage.getItem("userInfo");
        if (userInfo) {
            let user = JSON.parse(userInfo);
            $('#header-user-info').text(user.username);
        }
    }
});

