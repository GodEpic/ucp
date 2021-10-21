/**
 * Created by admin on 2019/7/2.
 */
// 根据加载的页面调整iframe的高度
$(window.parent.document).find("#contentFrame").height($(".tab-con").height() + 150);

//layui 模块化引用
layui.use(['table', 'layer'], function() {
	var table = layui.table,
		form = layui.form,
		layer = layui.layer;

	//数据表格实例化			
	var tbWidth = $("#tableRes").width();
	var layTableId = "layTable";
	var tableIns = table.render({
		elem: '#dataTable',
		id: layTableId,
		data: viewObj.tbData,
		width: tbWidth,
		page: false,
		loading: false,
		even: false, //不开启隔行背景
		cols: [
			[{
					title: '序号',
					type: 'numbers',
					width: 80
				},
				{
					field: 'couponLink',
					title: '图片链接',
					edit: 'text',
					templet: function(d) {
                        if(d.taskId) {
                            return "<div class='layui-cell-text layui-no-edit'>" + (d.couponLink || '') + "</div>";
                        }
						return "<div class='layui-cell-text'>" + (d.couponLink || '') + "</div>";
					}
				},
				{
					field: 'couponDesc',
					title: '图片备注',
					edit: 'textarea',
					templet: function(d) {
                        if(d.taskId) {
                            return "<div class='layui-cell-text layui-no-edit'>" + (d.couponDesc.replace(RegExp('\n', "g"),'<br/>') || '') + "</div>";
                        }
						return "<div class='layui-cell-text'>" + (d.couponDesc.replace(RegExp('\n', "g"),'<br/>') || '') + "</div>";
					}
				},
				{
					field: 'tempId',
					title: '操作',
					width: 100,
					templet: function(d) {
						if(d.taskId) {
							return "";
						}
						return "<a class='layui-btn layui-btn-xs layui-btn-danger' lay-event='del' lay-id='" + d.tempId + "'>删除</a>";
					}
				}
			]
		],
		done: function(res, curr, count) {
			viewObj.tbData = res.data;

			$(".layui-no-edit").each(function(){
				$(this).parent().parent().removeAttr("data-edit");
			});
		}
	});

	//定义事件集合
	var active = {
		addRow: function() { //添加一行
			var oldData = table.cache[layTableId];

			for(var i = 0, row; i < oldData.length; i++) {
				row = oldData[i];
				if(row.couponLink.length == 0) {
					Common.myAlert("请输入信息", 2);
					return;
				}
			}

			var newRow = {
				tempId: new Date().valueOf(),
				cpId:'',
				couponLink: '',
				couponDesc: ''
			};
			oldData.push(newRow);
			tableIns.reload({
				data: oldData
			});
		},
		updateRow: function(obj) {
			console.info(obj);

			var oldData = table.cache[layTableId];
			for(var i = 0, row; i < oldData.length; i++) {
				row = oldData[i];
				if(row.tempId == obj.tempId) {
					$.extend(oldData[i], obj);
					return;
				}
			}
			tableIns.reload({
				data: oldData
			});
		},
		removeEmptyTableCache: function() {
			var oldData = table.cache[layTableId];
			for(var i = 0, row; i < oldData.length; i++) {
				row = oldData[i];
				if(!row || !row.tempId) {
					oldData.splice(i, 1); //删除一项
				}
				continue;
			}
			tableIns.reload({
				data: oldData
			});
		}
	}

	//激活事件
	var activeByType = function(type, arg) {
		if(arguments.length === 2) {
			active[type] ? active[type].call(this, arg) : '';
		} else {
			active[type] ? active[type].call(this) : '';
		}
	}

	//注册按钮事件
	$('.layui-btn[data-type]').on('click', function() {
		var type = $(this).data('type');
		activeByType(type);
	});

	//监听工具条
	table.on('tool(dataTable)', function(obj) {
		try{
			var data = obj.data,
				event = obj.event,
				tr = obj.tr; //获得当前行 tr 的DOM对象;
		
			switch(event) {
				case "del": 
					layer.confirm('确认要删除该行吗？', function(index) {
						if(data.cpId){
							$.ajax({
								type: "post",
								data: {id: data.cpId},
								url: ctx + "/activity/couponImage/deleteCouponImage?time=" + new Date().getTime(),
								success: function (msg) {
									layer.msg(msg.message);
								}
							});	
						}
						obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
						layer.close(index);
						activeByType('removeEmptyTableCache');
					});
					
					break;
			}
		}catch(e){}
		
	});

		// 保存活动
	$(".activitySave").click(function () {
		validator = $("#activityForm").validate();
		if (validator.form()) { 
			$("input[name='couponImages']").val(JSON.stringify(table.cache[layTableId], null, 2));
			var param = $("form").serializeArray();
            var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
			$.ajax({
				type: "post",
				data: param,
				url: ctx + "/activity/save?time=" + new Date().getTime(),
				success: function (msg) {
                    layer.close(mask);
					if (msg.success) {
						$("input[name='id']").val(msg.obj.id);
						$("input[name='jiraNo']").val(msg.obj.jiraNo);
						$("input[name='jiraStatus']").val(msg.obj.jiraStatus);
						$("input[name='releaseStatus']").val(msg.obj.releaseStatus);
						if($("select[name='priority']").val() == undefined && $("input[name='priority']").val() == undefined) {
                            $("input[name='releaseStatus']").after("<input type='hidden' name='priority' value='"+msg.obj.priority+"'/>");
                        } else {
                            $("select[name='priority']").val(msg.obj.priority);
                        }
                        $("input[name='status']").val(msg.obj.status);
                        layer.msg(msg.message);
						loadCouponImage(msg.obj.id);
					} else {
                        layer.msg(msg.message);
					}
				} ,
				error : function(){
                    layer.close(mask);
                    layer.msg("保存失败");
				}
			});
		}
		else {
            layer.msg("数据校验未通过请查看");
		}
	});

	/**
	*加载券图信息
	*/
	function loadCouponImage(actId){
		$.ajax({
			type: "post",
			data: {actId: actId},
			url: ctx + "/activity/couponImage/findByActId?time=" + new Date().getTime(),
			success: function (obj) {
				tableIns.reload({
					data: obj
				});
			}
		});
	}

    $(".saveAndCreateTask").click(function () {
        layer.confirm('是否确认保存并发送该任务？', function(index) {
            validator = $("#activityForm").validate();
            if (validator.form()) {
                $("input[name='couponImages']").val(JSON.stringify(table.cache[layTableId], null, 2));
                var param = $("form").serializeArray();
                var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                $.ajax({
                    type: "post",
                    data: param,
                    url: ctx + "/activity/save?time=" + new Date().getTime(),
                    success: function (msg) {
                    	layer.close(mask);
                        if (msg.success) {
                            $("input[name='id']").val(msg.obj.id);
                            $("input[name='jiraNo']").val(msg.obj.jiraNo);
                            $("input[name='jiraStatus']").val(msg.obj.jiraStatus);
                            $("input[name='releaseStatus']").val(msg.obj.releaseStatus);
                            if($("select[name='priority']").val() == undefined && $("input[name='priority']").val() == undefined) {
                                $("input[name='releaseStatus']").after("<input type='hidden' name='priority' value='"+msg.obj.priority+"'/>");
                            } else {
                                $("select[name='priority']").val(msg.obj.priority);
                            }
                            $("input[name='status']").val(msg.obj.status);
                            layer.msg(msg.message);
                            loadCouponImage(msg.obj.id);

                            // 发送任务
                            var param = $("form").serializeArray();
                            param = param.splice(0,16);

                            var sendMask = layer.msg('正在发送任务', {icon: 16, time: 0, shade: [0.8, '#393D49']});

                            $.ajax({
                                type: "post",
                                data: param,
                                url: ctx + "/task/ucpTask/saveAndCreateTask?time=" + new Date().getTime(),
                                success: function (msg) {
                                    layer.close(sendMask);
                                    layer.msg(msg.message);
                                    if(msg.success) {
                                        $("input[name='releaseStatus']").val(msg.obj.releaseStatus);
                                    }
                                    setTimeout(function(){
                                        window.parent.close();
                                    },3000);
                                },
								error : function(){
                                    layer.close(sendMask);
                                    layer.msg("发送任务失败");
								}
                            });
                        } else {
                            layer.msg(msg.message);
                        }
                    },
					error : function(){
                        layer.close(mask);
                        layer.msg("保存活动失败");
					}
                });
            }
            else {
                layer.msg("数据校验未通过请查看");
            }
        });
    });
});


