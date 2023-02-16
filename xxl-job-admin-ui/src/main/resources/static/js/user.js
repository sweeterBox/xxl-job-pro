$(function() {
	var dataTable = $("#user_list").dataTable({
		"deferRender": true,
		"processing" : true,
	    "serverSide": true,
		"ajax": function (data, callback, settings) {
			let selectedSize = $('.dataTables_length').find('select').find("option:selected").val();
			let size = selectedSize || Number(data.length);
			let start = data.start;
			let page = size > 0 ? Number(data.start / data.length) : 0;
			let draw = data.draw;
			let order = data.order;
			let columns = data.columns;
			$.ajax({
				type: 'GET',
				url: base_url + "/v1.0/user/findPageList",
				data: {
					username: $('#username').val(),
					role: $('#role').val(),
					page: page,
					size: size
				},
				dataType: "json",
				success: (d) => {
					callback({
						recordsTotal: d.total,
						recordsFiltered: d.total,
						data: d.content,
					});
				}
			});
		},
	    "searching": false,
	    "ordering": false,
	    "scrollX": false,
	    "columns": [
	                {
	                	"data": 'id',
						"visible" : false,
						"width":'10%'
					},
	                {
	                	"data": 'username',
						"visible" : true,
						"width":'20%'
					},
	                {
	                	"data": 'password',
						"visible" : false,
                        "width":'20%',
                        "render": function ( data, type, row ) {
                            return '*********';
                        }
					},
					{
						"data": 'role',
						"visible" : true,
						"width":'10%',
                        "render": function ( data, type, row ) {
                            if (data == 1) {
                                return I18n.user_role_admin
                            } else {
                                return I18n.user_role_normal
                            }
                        }
					},
	                {
						"data": I18n.system_opt ,
						"width":'15%',
	                	"render": function ( data, type, row ) {
	                		return function(){
                                tableData['key'+row.id] = row;
								var html = '<p id="'+ row.id +'" >'+
									'<button class="btn btn-warning  update" type="button">'+ I18n.system_opt_edit +'</button>  '+
									'<button class="btn btn-danger  delete" style="margin-left: 10px;" type="button">'+ I18n.system_opt_del +'</button>  '+
									'</p>';

	                			return html;
							};
	                	}
	                }
	            ],
		"language" : {
			"sProcessing" : I18n.dataTable_sProcessing ,
			"sLengthMenu" : I18n.dataTable_sLengthMenu ,
			"sZeroRecords" : I18n.dataTable_sZeroRecords ,
			"sInfo" : I18n.dataTable_sInfo ,
			"sInfoEmpty" : I18n.dataTable_sInfoEmpty ,
			"sInfoFiltered" : I18n.dataTable_sInfoFiltered ,
			"sInfoPostFix" : "",
			"sSearch" : I18n.dataTable_sSearch ,
			"sUrl" : "",
			"sEmptyTable" : I18n.dataTable_sEmptyTable ,
			"sLoadingRecords" : I18n.dataTable_sLoadingRecords ,
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : I18n.dataTable_sFirst ,
				"sPrevious" : I18n.dataTable_sPrevious ,
				"sNext" : I18n.dataTable_sNext ,
				"sLast" : I18n.dataTable_sLast
			},
			"oAria" : {
				"sSortAscending" : I18n.dataTable_sSortAscending ,
				"sSortDescending" : I18n.dataTable_sSortDescending
			}
		}
	});

	$(".dataTables_paginate").parent().attr("class", "col-sm-12 col-md-4");
	$(".dataTables_info").parent().attr("class", "col-sm-12 col-md-6");
	let dataTablesLengthHtml = $(".dataTables_length").parent().html();
	$('.dataTables_length').parent().parent().remove();
	$(".dataTables_info").parent().parent().append('<div class="col-sm-12 col-md-2">' + dataTablesLengthHtml + '</div>');
	dataTable.fnDraw();
    // table data
    var tableData = {};

	// search btn
	$('#searchBtn').on('click', function(){
        dataTable.fnDraw();
	});

	// job operate
	$("#user_list").on('click', '.delete',function() {
		var id = $(this).parent('p').attr("id");
		$.ajax({
			type : 'DELETE',
			url : base_url + "/v1.0/user/"+id,
			success : function(data){
				dataTable.fnDraw();
				Toasts.success()
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				dataTable.fnDraw();
				Toasts.error()
			}

		});
	});

	// add role
    $("#addModal .form input[name=role]").change(function () {
		var role = $(this).val();
		if (role == 1) {
            $("#addModal .form input[name=permission]").parents('.form-group').hide();
		} else {
            $("#addModal .form input[name=permission]").parents('.form-group').show();
		}
        $("#addModal .form input[name='permission']").prop("checked",false);
    });

    jQuery.validator.addMethod("myValid01", function(value, element) {
        var length = value.length;
        var valid = /^[a-z][a-z0-9]*$/;
        return this.optional(element) || valid.test(value);
    }, I18n.user_username_valid );

	// add
	$(".add").click(function(){
		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
            username : {
				required : true,
                rangelength:[4, 20],
                myValid01: true
			},
            password : {
                required : true,
                rangelength:[4, 20]
            }
        },
        messages : {
            username : {
            	required : I18n.system_please_input + I18n.user_username,
                rangelength: I18n.system_lengh_limit + "[4-20]"
            },
            password : {
                required : I18n.system_please_input + I18n.user_password,
                rangelength: I18n.system_lengh_limit + "[4-20]"
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

            var permissionArr = [];
            $("#addModal .form input[name=permission]:checked").each(function(){
                permissionArr.push($(this).val());
            });

			var paramData = {
				"username": $("#addModal .form input[name=username]").val(),
                "password": $("#addModal .form input[name=password]").val(),
                "role": $("#addModal .form input[name=role]:checked").val(),
                "permission": permissionArr.join(',')
			};

        	$.post(base_url + "/v1.0/user", paramData, function(data, status) {
				$('#addModal').modal('hide');
				dataTable.fnDraw();
				Toasts.success()
    		});
		}
	});
	$("#addModal").on('hide.bs.modal', function () {
		$("#addModal .form")[0].reset();
		addModalValidate.resetForm();
		$("#addModal .form .form-group").removeClass("has-error");
		$(".remote_panel").show();	// remote

        $("#addModal .form input[name=permission]").parents('.form-group').show();
	});

    // update role
    $("#updateModal .form input[name=role]").change(function () {
        var role = $(this).val();
        if (role == 1) {
            $("#updateModal .form input[name=permission]").parents('.form-group').hide();
        } else {
            $("#updateModal .form input[name=permission]").parents('.form-group').show();
        }
        $("#updateModal .form input[name='permission']").prop("checked",false);
    });

	// update
	$("#user_list").on('click', '.update',function() {

        var id = $(this).parent('p').attr("id");
        var row = tableData['key'+id];

		// base data
		$("#updateModal .form input[name='id']").val( row.id );
		$("#updateModal .form input[name='username']").val( row.username );
		$("#updateModal .form input[name='password']").val( '' );
		$("#updateModal .form input[name='role'][value='"+ row.role +"']").click();
        var permissionArr = [];
        if (row.permission) {
            permissionArr = row.permission.split(",");
		}
        $("#updateModal .form input[name='permission']").each(function () {
            if($.inArray($(this).val(), permissionArr) > -1) {
                $(this).prop("checked",true);
            } else {
                $(this).prop("checked",false);
            }
        });

		// show
		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
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

            var permissionArr =[];
            $("#updateModal .form input[name=permission]:checked").each(function(){
                permissionArr.push($(this).val());
            });

            var paramData = {
                "id": $("#updateModal .form input[name=id]").val(),
                "username": $("#updateModal .form input[name=username]").val(),
                "password": $("#updateModal .form input[name=password]").val(),
                "role": $("#updateModal .form input[name=role]:checked").val(),
                "permission": permissionArr.join(',')
            };

            $.post(base_url + "/v1.0/user", paramData, function(data, status) {
				$('#updateModal').modal('hide');
				dataTable.fnDraw();
				Toasts.success()
			});
		}
	});
	$("#updateModal").on('hide.bs.modal', function () {
        $("#updateModal .form")[0].reset();
        updateModalValidate.resetForm();
        $("#updateModal .form .form-group").removeClass("has-error");
        $(".remote_panel").show();	// remote

        $("#updateModal .form input[name=permission]").parents('.form-group').show();
	});

});
