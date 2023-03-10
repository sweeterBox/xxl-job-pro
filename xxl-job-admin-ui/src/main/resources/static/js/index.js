/**
 * Created by xuxueli on 17/4/24.
 */
$(function () {

    // filter Time
    var rangesConf = {};
    rangesConf[I18n.daterangepicker_ranges_today] = [moment().startOf('day'), moment().endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_yesterday] = [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_this_month] = [moment().startOf('month'), moment().endOf('month')];
    rangesConf[I18n.daterangepicker_ranges_last_month] = [moment().subtract(1, 'months').startOf('month'), moment().subtract(1, 'months').endOf('month')];
    rangesConf[I18n.daterangepicker_ranges_recent_week] = [moment().subtract(1, 'weeks').startOf('day'), moment().endOf('day')];
    rangesConf[I18n.daterangepicker_ranges_recent_month] = [moment().subtract(1, 'months').startOf('day'), moment().endOf('day')];

    $('#filterTime').daterangepicker({
        autoApply:false,
        singleDatePicker:false,
        showDropdowns:false,        // 是否显示年月选择条件
        timePicker: true, 			// 是否显示小时和分钟选择条件
        timePickerIncrement: 10, 	// 时间的增量，单位为分钟
        timePicker24Hour : true,
        opens : 'left', //日期选择框的弹出位置
        ranges: rangesConf,
        locale : {
            format: 'YYYY-MM-DD HH:mm:ss',
            separator : ' - ',
            customRangeLabel : I18n.daterangepicker_custom_name ,
            applyLabel : I18n.system_ok ,
            cancelLabel : I18n.system_cancel ,
            fromLabel : I18n.daterangepicker_custom_starttime ,
            toLabel : I18n.daterangepicker_custom_endtime ,
            daysOfWeek : I18n.daterangepicker_custom_daysofweek.split(',') ,        // '日', '一', '二', '三', '四', '五', '六'
            monthNames : I18n.daterangepicker_custom_monthnames.split(',') ,        // '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'
            firstDay : 1
        },
        startDate: rangesConf[I18n.daterangepicker_ranges_recent_week][0] ,
        endDate: rangesConf[I18n.daterangepicker_ranges_recent_week][1]
    }, function (start, end, label) {
        freshChartDate(start, end);
    });
    freshChartDate(rangesConf[I18n.daterangepicker_ranges_recent_week][0], rangesConf[I18n.daterangepicker_ranges_recent_week][1]);

    /**
     * fresh Chart Date
     *
     * @param startDate
     * @param endDate
     */
    function freshChartDate(startDate, endDate) {
        $.ajax({
            type : 'GET',
            url : base_url + '/v1.0/dashboard/chart',
            data : {
                'startDate':startDate.format('YYYY-MM-DD HH:mm:ss'),
                'endDate':endDate.format('YYYY-MM-DD HH:mm:ss')
            },
            dataType : "json",
            success : function(data){
                lineChartInit(data)
                pieChartInit(data);
            }
        });
    }

    /**
     * line Chart Init
     */
    function lineChartInit(data) {
        var option = {
               title: {
                   text: I18n.job_dashboard_date_report
               },
               tooltip : {
                   trigger: 'axis',
                   axisPointer: {
                       type: 'cross',
                       label: {
                           backgroundColor: '#6a7985'
                       }
                   }
               },
               legend: {
                   data:[I18n.joblog_status_suc, I18n.joblog_status_fail]
               },
               toolbox: {
                   feature: {
                       /*saveAsImage: {}*/
                   }
               },
               grid: {
                   left: '3%',
                   right: '4%',
                   bottom: '3%',
                   containLabel: true
               },
               xAxis : [
                   {
                       type : 'category',
                       boundaryGap : false,
                       data : data.triggerDayList
                   }
               ],
               yAxis : [
                   {
                       type : 'value'
                   }
               ],
               series : [
                   {
                       name:I18n.joblog_status_suc,
                       type:'line',
                       stack: 'Total',
                       areaStyle: {normal: {}},
                       data: data.triggerDayCountSucList
                   },
                   {
                       name:I18n.joblog_status_fail,
                       type:'line',
                       stack: 'Total',
                       label: {
                           normal: {
                               show: true,
                               position: 'top'
                           }
                       },
                       areaStyle: {normal: {}},
                       data: data.triggerDayCountFailList
                   }
               ],
                color:['#28a745', '#f6236f']
        };

        var lineChart = echarts.init(document.getElementById('lineChart'));
        lineChart.setOption(option);
    }

    /**
     * pie Chart Init
     */
    function pieChartInit(data) {
        var option = {
            title : {
                text: I18n.job_dashboard_rate_report ,
                /*subtext: 'subtext',*/
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: [I18n.joblog_status_suc, I18n.joblog_status_fail]
            },
            series : [
                {
                    //name: '分布比例',
                    type: 'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:[
                        {
                            name:I18n.joblog_status_suc,
                            value:data.triggerCountSucTotal
                        },
                        {
                            name:I18n.joblog_status_fail,
                            value:data.triggerCountFailTotal
                        }
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ],
            color:['#28a745', '#f6236f']
        };
        var pieChart = echarts.init(document.getElementById('pieChart'));
        pieChart.setOption(option);
    }

    statistics();
    function statistics() {
        $.ajax({
            type : 'GET',
            url : base_url + '/v1.0/dashboard/statistics',
            dataType : "json",
            success : function(data){
                $("#taskAllNum").text(data.taskAllNum)
                $("#taskRunningNum").text(data.taskRunningNum)
                $("#triggerAllNum").text(data.triggerAllNum)
                $("#triggerSuccessNum").text(data.triggerSuccessNum)
                $("#instanceAllNum").text(data.instanceAllNum)
                $("#instanceUpNum").text(data.instanceUpNum)
            }
        });

    }

    systemInfo();
    function systemInfo() {
        $.ajax({
            type : 'GET',
            url : base_url + '/v1.0/dashboard/systemInfo',
            dataType : "json",
            success : function(data){
                $("#initHeapMemorySize").text(formatBytes(data.heapMemory.initMemorySize))
                $("#maxHeapMemorySize").text(formatBytes(data.heapMemory.maxMemorySize));
                $("#usedHeapMemorySize").text(formatBytes(data.heapMemory.usedMemorySize));

                $("#initNonHeapMemorySize").text(formatBytes(data.nonHeapMemory.initMemorySize));
                $("#maxNonHeapMemorySize").text('-');
                $("#usedNonHeapMemorySize").text(formatBytes(data.nonHeapMemory.usedMemorySize));

                $("#totalPhysicalMemorySize").text(formatBytes(data.physicalMemory.totalPhysicalMemorySize));
                $("#freePhysicalMemorySize").text(formatBytes(data.physicalMemory.freePhysicalMemorySize));
                $("#usedPhysicalMemorySize").text(formatBytes(data.physicalMemory.usedPhysicalMemorySize));

                $("#pid").text(data.pid);
                $("#osName").text(data.osName);
                $("#cpuCoreSize").text(data.cpuCoreSize);
                $("#totalThread").text(data.totalThread);
                $("#startTime").text(data.startTime);
                updateRunTimeLength(data.startTime);
            }
        });

    }

    startClock();

    /**
     * 定时更新系统运行时长
     */
    function startClock() {
       let clock = setInterval(function(){
           var startTime = $("#startTime").text();
           if (startTime) {
               updateRunTimeLength(startTime);
           }
        }, 1000);
    }
    function updateRunTimeLength(startTime) {
        var span = moment.duration(moment() - moment(new Date(startTime)));
        $('#runTimeLength').text(span.days() + '天' + span.hours() + '小时' + span.minutes() + '分钟' + span.seconds() + '秒');
    }

});
