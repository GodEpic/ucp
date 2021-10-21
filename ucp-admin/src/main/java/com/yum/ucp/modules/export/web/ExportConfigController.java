package com.yum.ucp.modules.export.web;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.export.entity.ExportConfig;
import com.yum.ucp.modules.export.service.ExportConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * exportConfig 导出配置Controller
 *
 * @author tony
 * @version 2019-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/export")
public class ExportConfigController extends BaseController {

	@Autowired
	private ExportConfigService exportConfigService;

	@ModelAttribute
	public ExportConfig get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return exportConfigService.get(id);
		}else{
			return new ExportConfig();
		}
	}

	@RequestMapping(value = {"list", ""})
	public String list(ExportConfig exportConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ExportConfig> page = exportConfigService.findPage(new Page<ExportConfig>(request, response), exportConfig);
        model.addAttribute("page", page);
		return "modules/export/exportConfigList";
	}

	@RequestMapping(value = "form")
	public String form() {
		return "modules/export/exportConfigForm";
	}

	@RequestMapping(value = "save")
	public String save(ExportConfig exportConfig, RedirectAttributes redirectAttributes) {
		// 保存导出配置
		exportConfigService.save(exportConfig);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:" + adminPath + "/export/list?repage";
	}
	
	@RequestMapping(value = "delete")
	public String delete(ExportConfig exportConfig, RedirectAttributes redirectAttributes) {
		exportConfigService.delete(exportConfig);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:" + adminPath + "/export/list?repage";
	}
	
}
