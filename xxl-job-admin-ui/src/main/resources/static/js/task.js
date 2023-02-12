$(function() {

	// init date tables
	var dataTable = $("#job_list").dataTable({
		"deferRender": true,
		"processing" : true,
	    "serverSide": true,
		"lengthMenu": [10, 25, 50,75, 100],
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
				url: base_url + "/v1.0/task/findPageList",
				data: {
					applicationName:  $('#allApplication').val(),
					triggerStatus: $('#triggerStatus').val(),
					description: $('#description').val(),
					executorHandler: $('#executorHandler').val(),
					author: $('#author').val(),
					page: page,
					size: size
				},
				dataType: "json",
				success: function (d) {
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
	    "columns": [
	                {
	                	"data": 'id',
						"bSortable": false,
						"visible" : true,
						"width":'5%'
					},
	                {
	                	"data": 'applicationName',
	                	"visible" : true,
						"width":'10%'
            		},
	                {
	                	"data": 'description',
						"visible" : true,
						"width":'15%'
					},
					{
						"data": 'scheduleType',
						"visible" : true,
						"width":'12%',
						"render": function ( data, type, row ) {
							if (row.scheduleConf) {
								return '<span style="font-weight: bolder;color:#343a40;">' + row.scheduleType + ':' + row.scheduleConf + '</span>';
							} else {
								return row.scheduleType;
							}
						}
					},
					{
						"data": 'glueType',
						"width":'15%',
						"visible" : true,
						"render": function ( data, type, row ) {
							var glueTypeTitle = findGlueTypeTitle(row.glueType);
                            if (row.executorHandler) {
								return '<span style="font-weight: bolder;color:#343a40;">' + glueTypeTitle + ':' + row.executorHandler + '</span>';
                            } else {
                                return glueTypeTitle;
                            }
						}
					},
	                {
	                	"data": 'executorParam',
						"visible" : false
					},
	                {
	                	"data": 'updateTime',
	                	"visible" : false,
	                	"render": function ( data, type, row ) {
							return data ? moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss") : "";
	                	}
	                },
	                { "data": 'author', "visible" : true, "width":'10%'},
	                { "data": 'alarmEmail', "visible" : false},
	                { "data": 'executorTimeout', "visible" : false},
	                { "data": 'executorBlockStrategy', "visible" : false},
	                {
	                	"data": 'triggerStatus',
						"width":'3%',
	                	"visible" : true,
	                	"render": function (data, type, row ) {
                            // status
                            if (1 == data.value) {
                                return '<span style="color: #13ce66">RUNNING</span>';
                            } else {
								return '<span  style="color: #ffc107 ">STOP</span>';
                            }
	                		return data;
	                	}
	                },
			     {"data": 'lastTriggerTime',
					 "width":'12%',
					 "visible" : true,
					 "render": function ( data, type, row ) {
					return data ? moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss") : "";
				 }},
	              {
						"data": I18n.system_opt ,
						"width":'35%',
	                	"render": function ( data, type, row ) {
	                		return function(){

                                // status
                                var start_stop_div = "";
                                if (1 == row.triggerStatus.value ) {
                                    start_stop_div = '<button type="button" class="btn btn-danger btn-sm job_operate" _type="job_pause">停止</button>';
                                } else {
                                    start_stop_div = '<button type="button" class="btn btn-success btn-sm job_operate" _type="job_resume">启动</button>';
                                }

                                // job_next_time_html
								var job_next_time_html = '';
								if (row.scheduleType == 'CRON' || row.scheduleType == 'FIX_RATE') {
									job_next_time_html = '<li><a href="javascript:void(0);" class="job_next_time" >' + I18n.jobinfo_opt_next_time + '</a></li>\n';
								}


                                // code url
                                var codeBtn = "";
                                if ('BEAN' != row.glueType) {
                                    var codeUrl = base_url +'/jobcode?jobId='+ row.id;
                                    codeBtn = '<li><a href="'+ codeUrl +'" target="_blank" >GLUE IDE</a></li>\n';
                                    codeBtn += '<li class="divider"></li>\n';
                                }

                                // data
                                tableData['key'+row.id] = row;

                                // opt
                                var html =
									'<div class="btn-group" style="margin-left: 10px">' +
                                    '<button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">更多' +
                                    '       <span class="caret"></span>' +
                                    '       <span class="sr-only">Toggle Dropdown</span>' +
                                    '</button>' +
                                    '<ul class="dropdown-menu" role="menu" _id="'+ row.id +'" >' +
                                    ' 	<div class="dropdown-divider"></div>' +
									' 	<a href="javascript:void(0);" class="update dropdown-item">编辑</a>' +
                                    ' 	<div class="dropdown-divider"></div>' +
									' 	<a href="javascript:void(0);" class="job_operate dropdown-item" _type="job_del" >'+ '删除' +'</a>' +
                                    '</ul>' +
                                    '</div>';

								let executeOnce = '<button type="button" class="job_trigger btn btn-success btn-sm" style="margin-left: 10px">执行一次</button>';

								return '<div id="' + row.id + '" >' + start_stop_div + executeOnce + html + '</div>';
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

	// jobGroup change
	$('#jobGroup').on('change', function(){
        //reload
        var jobGroup = $('#jobGroup').val();
        window.location.href = base_url + "/jobinfo?jobGroup=" + jobGroup;
    });

	// job operate
	$("#job_list").on('click', '.job_operate',function() {
		var typeName;
		var url;
		var id = $(this).parents('ul').attr("_id");
		var type = $(this).attr("_type");
		if ("job_pause" == type) {
			id = $(this).parent('div').attr("id");
			url = base_url + "/v1.0/task/stop/" + id;
			$.ajax({
				type : 'POST',
				url : url,
				success : function(data){
					dataTable.fnDraw();
				}
			});
		} else if ("job_resume" == type) {
			typeName = I18n.jobinfo_opt_start ;
			id = $(this).parent('div').attr("id");
			url = base_url + "/v1.0/task/start/" + id;
			needFresh = true;
			$.ajax({
				type : 'POST',
				url : url,
				success : function(data){
					dataTable.fnDraw();
				}
			});
		} else if ("job_del" == type) {
			typeName = I18n.system_opt_del ;
			url = base_url + "/v1.0/task/"+ id;
			$.ajax({
				type : 'DELETE',
				url : url,
				success : function(data){
					dataTable.fnDraw();
				}
			});
		} else {

		}
	});

    // job trigger
    $("#job_list").on('click', '.job_trigger',function() {
        var id = id = $(this).parent('div').attr("id");;
        var row = tableData['key'+id];

        $("#jobTriggerModal .form input[name='id']").val( row.id );
        $("#jobTriggerModal .form textarea[name='executorParam']").val( row.executorParam );

        $('#jobTriggerModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    $("#jobTriggerModal .ok").on('click',function() {
		let param = {
			"id": $("#jobTriggerModal .form input[name='id']").val(),
			"executorParam": $("#jobTriggerModal .textarea[name='executorParam']").val(),
			"clientUrl": $("#jobTriggerModal .textarea[name='addressList']").val()
		};
        $.ajax({
            type : 'POST',
            url : base_url + "/v1.0/task/executeOnce",
			contentType: 'application/json',
			data: JSON.stringify(param),
            success : function(data){
				$('#jobTriggerModal').modal('hide');

            }
        });
    });
    $("#jobTriggerModal").on('hide.bs.modal', function () {
        $("#jobTriggerModal .form")[0].reset();
    });


    // job registryinfo
    $("#job_list").on('click', '.job_registryinfo',function() {
        var id = $(this).parents('ul').attr("_id");
        var row = tableData['key'+id];

        var jobGroup = row.jobGroup;

        $.ajax({
            type : 'POST',
            url : base_url + "/jobgroup/loadById",
            data : {
                "id" : jobGroup
            },
            dataType : "json",
            success : function(data){

                var html = '<div>';
                if (data.code == 200 && data.content.registryList) {
                    for (var index in data.content.registryList) {
                        html += (parseInt(index)+1) + '. <span class="badge bg-green" >' + data.content.registryList[index] + '</span><br>';
                    }
                }
                html += '</div>';

                layer.open({
                    title: I18n.jobinfo_opt_registryinfo ,
                    btn: [ I18n.system_ok ],
                    content: html
                });

            }
        });

    });

    // job_next_time
    $("#job_list").on('click', '.job_next_time',function() {
        var id = $(this).parents('ul').attr("_id");
        var row = tableData['key'+id];

        $.ajax({
            type : 'POST',
            url : base_url + "/jobinfo/nextTriggerTime",
            data : {
                "scheduleType" : row.scheduleType,
				"scheduleConf" : row.scheduleConf
            },
            dataType : "json",
            success : function(data){

            	if (data.code != 200) {
                    layer.open({
                        title: I18n.jobinfo_opt_next_time ,
                        btn: [ I18n.system_ok ],
                        content: data.msg
                    });
				} else {
                    var html = '<center>';
                    if (data.code == 200 && data.content) {
                        for (var index in data.content) {
                            html += '<span>' + data.content[index] + '</span><br>';
                        }
                    }
                    html += '</center>';

                    layer.open({
                        title: I18n.jobinfo_opt_next_time ,
                        btn: [ I18n.system_ok ],
                        content: html
                    });
				}

            }
        });

    });

	// add
	$(".add").click(function(){

		// init-cronGen
        $("#addModal .form input[name='schedule_conf_CRON']").show().siblings().remove();

        $("#addModal .form input[name='schedule_conf_CRON']").cronGen({});

		// 》init scheduleType
		$("#updateModal .form select[name=scheduleType]").change();

		// 》init glueType
		$("#updateModal .form select[name=glueType]").change();

		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
			description : {
				required : true,
				maxlength: 50
			},
			author : {
				required : true
			},
			glueType : {
				required : true
			}

			/*,
            executorTimeout : {
                digits:true
            },
            executorFailRetryCount : {
                digits:true
            }*/
        },
        messages : {
			description : {
            	required : I18n.system_please_input + I18n.jobinfo_field_jobdesc
            },
            author : {
            	required : I18n.system_please_input + I18n.jobinfo_field_author
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

			// process executorTimeout+executorFailRetryCount
            var executorTimeout = $("#addModal .form input[name='executorTimeout']").val();
            if(!/^\d+$/.test(executorTimeout)) {
                executorTimeout = 0;
			}
            $("#addModal .form input[name='executorTimeout']").val(executorTimeout);
            var executorFailRetryCount = $("#addModal .form input[name='executorFailRetryCount']").val();
            if(!/^\d+$/.test(executorFailRetryCount)) {
                executorFailRetryCount = 0;
            }
            $("#addModal .form input[name='executorFailRetryCount']").val(executorFailRetryCount);

            // process schedule_conf
			var scheduleType = $("#addModal .form select[name='scheduleType']").val();
			var scheduleConf;
			if (scheduleType == 'CRON') {
				scheduleConf = $("#addModal .form input[name='cronGen_display']").val();
			} else if (scheduleType == 'FIX_RATE') {
				scheduleConf = $("#addModal .form input[name='schedule_conf_FIX_RATE']").val();
			} else if (scheduleType == 'FIX_DELAY') {
				scheduleConf = $("#addModal .form input[name='schedule_conf_FIX_DELAY']").val();
			}
			$("#addModal .form input[name='scheduleConf']").val( scheduleConf );


			let param = $("#addModal .form").serializeJson();
			param.scheduleConf = $('#scheduleConf').val();
			$.ajax({
				type: 'POST',
				url: base_url + "/v1.0/task/save",
				contentType: 'application/json',
				data: JSON.stringify(param),
				success: (d) => {
					$('#addModal').modal('hide');
					dataTable.fnDraw();
				}
			});
		}
	});
	$("#addModal").on('hide.bs.modal', function () {
        addModalValidate.resetForm();
		$("#addModal .form")[0].reset();
		$("#addModal .form .form-group").removeClass("has-error");
		$(".remote_panel").show();	// remote

		$("#addModal .form input[name='executorHandler']").removeAttr("readonly");
		let applicationName = $("#addAllApplication").val();
		getTask(applicationName);
	});

	$("#addAllApplication").change(function(){
		var applicationName = $(this).val();
		 getTask(applicationName);
	});
	$('#tasks ').on('click', '.dropdown-item', function () {
		console.log($(this).data('name'))
		$('input[name="executorHandler"]').val($(this).data('name'));
	});
	// scheduleType change
	$(".scheduleType").change(function(){
		var scheduleType = $(this).val();
		$(this).parents("form").find(".schedule_conf").hide();
		$(this).parents("form").find(".schedule_conf_" + scheduleType).show();

	});

    // glueType change
    $(".glueType").change(function(){
		// executorHandler
        var $executorHandler = $(this).parents("form").find("input[name='executorHandler']");
        var glueType = $(this).val();
        if ('BEAN' != glueType) {
           // $executorHandler.val("");
           // $executorHandler.attr("readonly","readonly");
        } else {
           // $executorHandler.removeAttr("readonly");
        }
    });

	$("#addModal .glueType").change(function(){
		// glueSource
		var glueType = $(this).val();
		if ('GLUE_GROOVY'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_java").val() );
		} else if ('GLUE_SHELL'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_shell").val() );
		} else if ('GLUE_PYTHON'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_python").val() );
		} else if ('GLUE_PHP'==glueType){
            $("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_php").val() );
        } else if ('GLUE_NODEJS'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_nodejs").val() );
		} else if ('GLUE_POWERSHELL'==glueType){
            $("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_powershell").val() );
        } else {
            $("#addModal .form textarea[name='glueSource']").val("");
		}


	});

	// update
	$("#job_list").on('click', '.update',function() {

        var id = $(this).parents('ul').attr("_id");
        var row = tableData['key'+id];

		// fill base
		$("#updateModal .form input[name='id']").val(row.id);
		$("#updateModal .form select[name='applicationName'] option[value="+ row.applicationName +"]").prop('selected', true);
		$("#updateModal .form input[name='description']").val(row.description);
		$("#updateModal .form input[name='author']").val(row.author);
		$("#updateModal .form input[name='alarmEmail']").val(row.alarmEmail);

		// fill trigger
		$('#updateModal .form select[name=scheduleType] option[value='+ row.scheduleType +']').prop('selected', true);
		$("#updateModal .form input[name='scheduleConf']").val( row.scheduleConf );
		if (row.scheduleType == 'CRON') {
			$("#updateModal .form input[name='schedule_conf_CRON']").val( row.scheduleConf );
		} else if (row.scheduleType == 'FIX_RATE') {
			$("#updateModal .form input[name='schedule_conf_FIX_RATE']").val( row.scheduleConf );
		} else if (row.scheduleType == 'FIX_DELAY') {
			$("#updateModal .form input[name='schedule_conf_FIX_DELAY']").val( row.scheduleConf );
		}

		// 》init scheduleType
		$("#updateModal .form select[name=scheduleType]").change();

		// fill job
		$('#updateModal .form select[name=glueType] option[value='+ row.glueType +']').prop('selected', true);
		$("#updateModal .form input[name='executorHandler']").val( row.executorHandler );
		$("#updateModal .form textarea[name='executorParam']").val( row.executorParam );

		// 》init glueType
		$("#updateModal .form select[name=glueType]").change();

		// 》init-cronGen
		$("#updateModal .form input[name='schedule_conf_CRON']").show().siblings().remove();
		$("#updateModal .form input[name='schedule_conf_CRON']").cronGen({});

		// fill advanced
		$('#updateModal .form select[name=executorRouteStrategy] option[value='+ row.executorRouteStrategy +']').prop('selected', true);
		$("#updateModal .form input[name='childJobId']").val( row.childJobId );
		$('#updateModal .form select[name=misfireStrategy] option[value='+ row.misfireStrategy +']').prop('selected', true);
		$('#updateModal .form select[name=executorBlockStrategy] option[value='+ row.executorBlockStrategy +']').prop('selected', true);

		$("#updateModal .form input[name='executorTimeout']").val(row.executorTimeout);
		$("#updateModal .form input[name='executorFailRetryCount']").val(row.executorFailRetryCount);

		$("#updateModal .form select[name='glueType']").attr("disabled",true);
		$("#updateModal .form input[name='executorHandler']").attr("disabled",true);
		//$("#updateModal .form #tasks").attr("disabled",true);

		// show
		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,

		rules : {
			jobDesc : {
				required : true,
				maxlength: 50
			},
			author : {
				required : true
			}
		},
		messages : {
			jobDesc : {
                required : I18n.system_please_input + I18n.jobinfo_field_jobdesc
			},
			author : {
				required : I18n.system_please_input + I18n.jobinfo_field_author
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

            // process executorTimeout + executorFailRetryCount
            var executorTimeout = $("#updateModal .form input[name='executorTimeout']").val();
            if(!/^\d+$/.test(executorTimeout)) {
                executorTimeout = 0;
            }
            $("#updateModal .form input[name='executorTimeout']").val(executorTimeout);
            var executorFailRetryCount = $("#updateModal .form input[name='executorFailRetryCount']").val();
            if(!/^\d+$/.test(executorFailRetryCount)) {
                executorFailRetryCount = 0;
            }
            $("#updateModal .form input[name='executorFailRetryCount']").val(executorFailRetryCount);


			// process schedule_conf
			var scheduleType = $("#updateModal .form select[name='scheduleType']").val();
			var scheduleConf;
			if (scheduleType == 'CRON') {
				scheduleConf = $("#updateModal .form input[name='cronGen_display']").val();
			} else if (scheduleType == 'FIX_RATE') {
				scheduleConf = $("#updateModal .form input[name='schedule_conf_FIX_RATE']").val();
			} else if (scheduleType == 'FIX_DELAY') {
				scheduleConf = $("#updateModal .form input[name='schedule_conf_FIX_DELAY']").val();
			}
			$("#updateModal .form input[name='scheduleConf']").val( scheduleConf );

			let param = $("#updateModal .form").serializeJson();
			param.scheduleConf = $('#scheduleConf').val();
			$.ajax({
				type: 'POST',
				url: base_url + "/v1.0/task/save",
				contentType: 'application/json',
				data: JSON.stringify(param),
				success: (d) => {
					$('#updateModal').modal('hide');
					dataTable.fnDraw();
				}
			});
		}
	});
	$("#updateModal").on('hide.bs.modal', function () {
        updateModalValidate.resetForm();
        $("#updateModal .form")[0].reset();
        $("#updateModal .form .form-group").removeClass("has-error");
	});

    /**
	 * find title by name, GlueType
     */
	function findGlueTypeTitle(glueType) {
        return glueType;
    }


	findAllApplication();

	function findAllApplication() {
		$.ajax({
			type : 'GET',
			url : base_url + "/v1.0/application/findAll",
			dataType : "json",
			success : function(response){
				$("#addAllApplication").append("<option value=''>" +'请选择'+"</option>");
				$("#allApplication").append("<option value=''>" +'请选择'+"</option>");

				response.forEach((v)=>{
					let html = "<option value='" + v.name+ "'>" +v.name+'('+ v.title + ")</option>";
					$("#allApplication").append(html);

					let html2 = "<option value='" + v.name+ "'>" +v.name+'('+ v.title + ")</option>";
					$("#addAllApplication").append(html2);
					$("#editAllApplication").append(html2);
				})
			}
		});
	}


	function getTask(applicationName) {
		$('#tasks').html('<div role="separator" class="dropdown-divider"></div>');

		$.ajax({
			type : 'GET',
			url: base_url + '/v1.0/task/findClientTasks?applicationName=' + applicationName,
			success : function(data){
				if (data && Array.isArray(data)) {
					let html = "";
					data.forEach(m => {
						html += '<div role="separator" class="dropdown-divider"></div>'
						html += '<a class="dropdown-item" data-name="' + m.name + '" href="#">' + m.name + '(' + m.description + ')' + '</a>';
					});
					$('#tasks').html(html);
				}
			}
		});
	}
});
