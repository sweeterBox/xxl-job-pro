function searchParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}



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
                url: base_url + "/v1.0/instance/findPageList",
                data: {
                    name: searchParam("name"),
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
                "width":'20%'
            },
            {
                "data": 'title',
                "visible" : true,
                "width":'20%'
            },
            {
                "data": 'healthy',
                "width":'5%',
                "visible" : true,
                "render": function (data, type, row ) {
                    if (data) {
                        return '<span style="color: #13ce66">'+data+'</span>';
                    }else {
                        return '<span  style="color: #dc3545">'+data+'</span>';
                    }
                    return data;
                }
            },
            {
                "data": 'ephemeral',
                "width":'5%',
                "visible" : true
            },

            {
                "data": 'weight',
                "width":'5%',
                "visible" : true
            },
            {
                "data": 'host',
                "width":'5%',
                "visible" : true
            },
            {
                "data": 'port',
                "width":'5%',
                "visible" : true
            },
            {
                "data": 'url',
                "width":'25%',
                "visible" : true
            },
            {
                "data": 'status',
                "width":'10%',
                "visible" : true,
                "render": function (data, type, row ) {
                    if ('UP' == data) {
                        return '<span style="color: #13ce66">UP</span>';
                    }
                    if ('DOWN' == data){
                        return '<span  style="color: #ffc107 ">DOWN</span>';
                    }
                    return data;
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


    $("#name").text(searchParam("name"));

    $('#back').on('click', function(){
        window.history.go(-1);
    });
});
