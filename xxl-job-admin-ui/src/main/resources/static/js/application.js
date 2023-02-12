

$(function() {
	var dataTable = $("#tab").dataTable({
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
				url: base_url + "/v1.0/application/findPageList",
				data: {
					name: $('#name').val(),
					title: $('#title').val(),
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
				"visible" : false
			},
			{
				"data": 'name',
				"visible" : true,
				"width":'30%'
			},
			{
				"data": 'title',
				"visible" : true,
				"width":'30%'
			},
			{
				"data": 'instanceAllSize',
				"width":'10%',
				"visible" : true
			},
			{
				"data": 'instanceHealthySize',
				"width":'15%',
				"visible" : true
			},
			{
				"data": I18n.system_opt ,
				"width":'15%',
				"render": function (data, type, row ) {
					return function(){
						tableData['key'+row.id] = row;
						var html = '<p id="'+ row.id +'" >'+
							'<button type="button" class="btn btn-sm btn-primary view" style="margin-right: 5px">查看实例</button>'+
							'<button type="button" class="btn btn-sm btn-danger delete">删除</button>'+
							'</p>'
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

	// table data
	var tableData = {};

	// search btn
	$('#searchBtn').on('click', function(){
		dataTable.fnDraw();
	});


	// opt_del
	$("#tab").on('click', '.delete',function() {
		var id = $(this).parent('p').attr("id");
		$.ajax({
			type : 'DELETE',
			url : base_url + '/v1.0/application/'+id,
			success : function(data){
				dataTable.fnDraw();
				Toasts.success();
			}
		});
	});

	$("#tab").on('click', '.view',function() {
		var id = $(this).parent('p').attr("id");
		var row = tableData['key'+id];
		window.location.href = base_url + '/application/clientList' + '?name=' + row.name;
	});


	// jquery.validate “low letters start, limit contants、 letters、numbers and line-through.”
	jQuery.validator.addMethod("myValid01", function(value, element) {
		var length = value.length;
		var valid = /^[a-z][a-zA-Z0-9-]*$/;
		return this.optional(element) || valid.test(value);
	}, I18n.jobgroup_field_appname_limit );

	$('.add' ).on('click', function(){
		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
		rules : {
			appname : {
				required : true,
				rangelength:[4,64],
				myValid01 : true
			},
			title : {
				required : true,
				rangelength:[4, 12]
			}
		},
		messages : {
			appname : {
				required : I18n.system_please_input+"AppName",
				rangelength: I18n.jobgroup_field_appname_length ,
				myValid01: I18n.jobgroup_field_appname_limit
			},
			title : {
				required : I18n.system_please_input + I18n.jobgroup_field_title ,
				rangelength: I18n.jobgroup_field_title_length
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
			$.post(base_url + "/v1.0/application",  $("#addModal .form").serialize(), function(data, status) {
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
	});



	// opt_edit
	$("#tab").on('click', '.edit',function() {
		var id = $(this).parents('ul').attr("_id");
		var row = tableData['key'+id];

		$("#updateModal .form input[name='id']").val( row.id );
		$("#updateModal .form input[name='appname']").val( row.appname );
		$("#updateModal .form input[name='title']").val( row.title );

		// 注册方式
		$("#updateModal .form input[name='addressType']").removeAttr('checked');
		$("#updateModal .form input[name='addressType'][value='"+ row.addressType +"']").click();
		// 机器地址
		$("#updateModal .form textarea[name='addressList']").val( row.addressList );

		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : true,
		rules : {
			appname : {
				required : true,
				rangelength:[4,64],
				myValid01 : true
			},
			title : {
				required : true,
				rangelength:[4, 12]
			}
		},
		messages : {
			appname : {
                required : I18n.system_please_input+"AppName",
                rangelength: I18n.jobgroup_field_appname_length ,
                myValid01: I18n.jobgroup_field_appname_limit
            },
            title : {
                required : I18n.system_please_input + I18n.jobgroup_field_title ,
                rangelength: I18n.jobgroup_field_title_length
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
			$.post(base_url + "/jobgroup/update",  $("#updateModal .form").serialize(), function(data, status) {
				if (data.code == "200") {
					$('#updateModal').modal('hide');

					layer.open({
						title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
						content: I18n.system_update_suc ,
						icon: '1',
						end: function(layero, index){
							dataTable.fnDraw();
						}
					});
				} else {
					layer.open({
						title: I18n.system_tips,
                        btn: [ I18n.system_ok ],
						content: (data.msg || I18n.system_update_fail  ),
						icon: '2'
					});
				}
			});
		}
	});
	$("#updateModal").on('hide.bs.modal', function () {
		$("#updateModal .form")[0].reset();
		addModalValidate.resetForm();
		$("#updateModal .form .form-group").removeClass("has-error");
	});


});
