/**
 * Created by admin on 2019/7/2.
 */
// 根据加载的页面调整iframe的高度
$(window.parent.document).find("#contentFrame").height($(".tab-con").height() + 150);

//layui 模块化引用
layui.use(['table', 'layer','upload','laytpl', 'element', 'upload', 'form'], function() {
	var table = layui.table,
		form = layui.form,
        upload = layui.upload,
        laytpl = layui.laytpl,
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
                    title: '图片',
                    templet: function(d) {
                        return '<input id="uploadImg" name="img" type="file" onchange="uploadImg" style="width:330px"/>';
                    }
                },
				{
					field: 'fileName',
					title: '附件',
					templet: function(d) {
                        return '<input id="uploadFile" name="file" type="file" style="width:330px"/>';
					}
				},
                {
                field: 'couponDesc',
                title: '备注',
                edit: 'textarea',
                templet: function(d) {
                    return "<div class='layui-cell-text'>" + (d.couponDesc.replace(RegExp('\n', "g"),'<br/>') || '') + "</div>";
                }
            }
			]
		],
		done: function(res, curr, count) {
			viewObj.tbData = res.data;
            $("div[data-rowId='"+ layTableId +"']").find(".layui-upload-btn").each(function(){
                var _t = $(this);
                var mask = "";
                var dataRowId = _t.attr("data-rowId");
                var up = upload.render({
                    elem: _t.parent(),
                    url: '${ctx}/common/uploadFtpAttach',
                    multiple: true,
                    accept:"images",
                    exts:"jpg|png|gif|bmp|jpeg",
                    acceptMime:'image/*',
                    auto : true,
                    before:function(obj){
                        mask = layer.msg('努力上传中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                    },
                    done : function(res){
                        layer.close(mask);
                        var obj = res.obj;
                        if(obj.errcode === '0'){
                            var item = _t.parent().parent();
                            _t.parent().before('<div class="layui-upload-item"><a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a><img alt="'+ obj.originalFileName +'" src="'+ obj.filePath +'" class="layui-upload-img" onclick="previewImage(this)"></div>');

                            var lastUpload = item.find(".layui-upload-item:last");
                            var input = "<input alt='" + obj.originalFileName + "' type='hidden' value='" + JSON.stringify(obj) + "'>";
                            lastUpload.append(input);

                            var oldData = table.cache[layTableId];
                            for(var i = 0, row; i < oldData.length; i++) {
                                row = oldData[i];
                                if(row.tempId.toString() === dataRowId) {
                                    row.imgSrc[row.imgSrc.length] = obj;
                                    $.extend(oldData[i], row);
                                    return;
                                }
                            }

                            previewImage(item);
                            layer.msg('上传成功');
                        } else {
                            layer.msg('上传失败');
                        }
                        up.config.elem.next()[0].value = '';
                    },
                    error: function(index, upload){
                        layer.close(mask);
                        layer.msg('上传失败');
                        up.config.elem.next()[0].value = '';
                    }
                });
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
				cpId:'',
				couponLink: '',
                fileName: '',
				couponDesc: ''
			};
			oldData.push(newRow);
			tableIns.reload({
				data: oldData
			});
		},
		removeEmptyTableCache: function() {
			var oldData = table.cache[layTableId];
			for(var i = 0, row; i < oldData.length; i++) {
				row = oldData[i];
				if(!row || !row.fileName) {
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
									Common.myAlert(msg.message, 2);
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

	$("#receiveUser").bind("input propertychange",function() {
        var value = this.value;
        console.log(value);
    });
		// 保存活动
	$(".activitySave").click(function () {
		validator = $("#activityForm").validate();
		if (validator.form()) {
			$("input[name='couponImages']").val(JSON.stringify(table.cache[layTableId], null, 2));
			var param = $("form").serializeArray();
			//param.push({name: "issueTypeId", value: $(window.parent.document).find("#contentFrame").attr("issue-type")});
			showLoad(false);
			$.ajax({
				type: "post",
				data: param,
				url: ctx + "/activity/save?time=" + new Date().getTime(),
				success: function (msg) {
					closeLoad();
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
						Common.myAlert(msg.message, 2);
						loadCouponImage(msg.obj.id);
					} else {
						Common.myAlert(msg.message, 2);
					}
				}
			});
		}
		else {
			Common.myAlert("数据校验未通过请查看", 2);
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
        if(confirm("是否确认保存并发送该任务？")) {
            debugger;
            validator = $("#activityForm").validate();
            if (validator.form()) {
                $("input[name='couponImages']").val(JSON.stringify(table.cache[layTableId], null, 2));
                var param = $("form").serializeArray();
                //param.push({name: "issueTypeId", value: $(window.parent.document).find("#contentFrame").attr("issue-type")});
                showLoad(false);
                $.ajax({
                    type: "post",
                    data: param,
                    url: ctx + "/activity/save?time=" + new Date().getTime(),
                    success: function (msg) {

                        closeLoad();
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
                            Common.myAlert(msg.message, 2);
                            loadCouponImage(msg.obj.id);

                            // 发送任务
                            var param = $("form").serializeArray();
                            param = param.splice(0,16);
                            showLoad(true);
                            $.ajax({
                                type: "post",
                                data: param,
                                url: ctx + "/task/ucpTask/saveAndCreateTask?time=" + new Date().getTime(),
                                success: function (msg) {
                                    closeLoad();
                                    layer.msg(msg.message);
                                    if(msg.success) {
                                        $("input[name='releaseStatus']").val(msg.obj.releaseStatus);
                                    }
                                    setTimeout(function(){
                                        window.parent.close();
                                    },3000);
                                }
                            });
                        } else {
                            Common.myAlert(msg.message, 2);
                        }
                    }
                });
            }
            else {
                Common.myAlert("数据校验未通过请查看", 2);
            }
        }
    });
});


