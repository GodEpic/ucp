package com.yum.ucp.modules.export;

import com.yum.ucp.common.utils.DateUtils;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.utils.excel.ExportExcel;
import com.yum.ucp.modules.export.entity.ExportConfig;
import com.yum.ucp.modules.export.service.ExportConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * exportConfig 导出配置Controller
 *
 * @author tony
 * @version 2019-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/export")
public class ExportController {
    /**
     * 日志对象
     */
    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExportConfigService exportConfigService;

    @RequestMapping(value = {"list", ""})
    public String list(String exportConfigId, HttpServletRequest request, HttpServletResponse response, Model model) {
        List<ExportConfig> exportConfigList = exportConfigService.findList(new ExportConfig());
        model.addAttribute("exportConfigList", exportConfigList);
        model.addAttribute("exportConfigId", exportConfigId);
        if (StringUtils.isNotEmpty(exportConfigId)) {
            ExportConfig exportConfig = exportConfigService.get(exportConfigId);
            List<LinkedHashMap<String, Object>> list = getExportList(exportConfig);
            List<String> headList = getExportHeadList(list);
            model.addAttribute("list", list);
            model.addAttribute("headList", headList);
        }
        return "ucp/export/exportList";
    }

    @RequestMapping(value = "exportRecord", method= RequestMethod.POST)
    public String exportFile(String exportConfigId, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "Export"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            if (StringUtils.isNotEmpty(exportConfigId)) {
                ExportConfig exportConfig = exportConfigService.get(exportConfigId);
                List<LinkedHashMap<String, Object>> list = getExportList(exportConfig);
                List<String> headList = getExportHeadList(list);
                String title = exportConfig.getName();
                new ExportExcel(title, headList).setDataListWithoutAnotation(list).write(response, fileName).dispose();
            }
            return null;
        } catch (Exception e) {
            logger.error("导出失败，失败信息：", e);
            redirectAttributes.addFlashAttribute("errorMessage", "导出失败");
        }
        return "redirect:list";
    }

    private List<LinkedHashMap<String, Object>> getExportList(ExportConfig exportConfig) {
        List<LinkedHashMap<String, Object>> list = null;
        String sql = "";
        if(exportConfig != null) {
            sql = exportConfig.getExportSql();
            sql = sql.replace("&lt;", "<");
            sql = sql.replace("&gt;", ">");
            sql = sql.replace("&quot;", "\"");
        }
        list = exportConfigService.findExportRecordList(sql);
        return list;
    }

    private List<String> getExportHeadList(List<LinkedHashMap<String, Object>> list) {
        List<String> headList = new LinkedList<>();
        if(list != null && list.size() > 0) {
            LinkedHashMap<String, Object> map = list.get(0);
            headList = new LinkedList<>(map.keySet());
        }
        return headList;
    }
}
