package com.yum.ucp.modules.activity.web;

import com.atlassian.jira.rest.client.api.RestClientException;
import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.activity.entity.Template;
import com.yum.ucp.modules.activity.service.TemplateService;
import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.sys.entity.User;
import com.yum.ucp.modules.sys.utils.MessageUtils;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import com.yum.ucp.modules.sys.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

@Controller
@RequestMapping(value = "${adminPath}/template")
public class TemplateController extends DscBaseController {
  Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired private TemplateService templateService;

  @ModelAttribute
  public Template get(@RequestParam(required = false) String id) {
    Template entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = templateService.get(id);
    }
    if (entity == null) {
      entity = new Template();
    }
    return entity;
  }

  /**
   * 活动列表-查询所有未发布的活动
   *
   * @param template
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping("list")
  public String list(
      Template template, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<Template> page = new Page<>(request, response);
    page = templateService.findPage(page, template);
    model.addAttribute("searchKey", template.getSearchKey());
    model.addAttribute("page", page);
    return "ucp/activity/templateList";
  }

  @RequestMapping("/form")
  public String form(Template template, Model model) {
    model.addAttribute("template", template);
    return "ucp/activity/templateForm";
  }

  @RequestMapping("/detail")
  public String detail(Template template, Model model) {
    model.addAttribute("template", template);
    return "ucp/activity/templateDetail";
  }

  @RequestMapping(value = "delete")
  public String delete(Template template, RedirectAttributes redirectAttributes) {
    try {
      templateService.delete(template);
      addMessage(redirectAttributes, "删除活动成功");
    } catch (Exception e) {
      logger.error("jira活动删除失败:", e);
      addErrorMessage(redirectAttributes, "删除活动失败");
    }
    return "redirect:" + adminPath + "/template/list";
  }

  @RequestMapping(value = "save", method = RequestMethod.POST)
  @ResponseBody
  public ResponseMessage save(Template template) throws Exception {
    try {
      User user = UserUtils.getUser();
      template.setUpdateDate(new Date());
      template.setUpdater(user.getId());
      if (StringUtils.isBlank(template.getId())) {
        template.setCreateDate(new Date());
        template.setCreator(user.getId());
      } else {
        template.setIsNewRecord(false);
      }
      templateService.save(template);
    } catch (RestClientException e) {
      log.error("保存失败", e);
      return ResponseMessage.error(
          MessageUtils.returnErrors(e.getErrorCollections(), new HashMap<>()));
    }
    return ResponseMessage.success("保存成功", template);
  }

  private void list(Template template, Page<Template> p, Model model) {
    // String roleType = SessionUtils.getRoleType();
    //// 未测试的QA可以看到所有的，其余状态QA只能看到自己的
    // if (!ACTIVITY_STATUS_NOT_TESTED.equals(activity.getStatus()) &&
    // ROLE_TYPE_QA.equals(roleType)) {
    //  activity.setReceiveUser(SessionUtils.getPhoneAgent());
    // }
    //// 资深员工只能看到自己的
    // else if (ROLE_TYPE_SENIOR.equals(roleType)) {
    //  activity.setCreateBy(UserUtils.getUser());
    // }
    // if (!ROLE_TYPE_ADMIN.equals(roleType) && !ROLE_TYPE_QA.equals(roleType)) {
    //  activity.setCreateBy(UserUtils.getUser());
    // }
    //// 如果是已发布，则状态不包含已测试通过的
    // if (RELEASE_STATUS_RELEASED.equals(activity.getReleaseStatus())) {
    //  activity.setInStatus(false);
    //  activity.setStatus(ACTIVITY_STATUS_TEST_PASS);
    // }
    //// 已完成
    // if (ACTIVITY_STATUS_TEST_PASS.equals(activity.getReleaseStatus())) {
    //  model.addAttribute("releaseStatus", ACTIVITY_STATUS_TEST_PASS);
    //  activity.setReleaseStatus(RELEASE_STATUS_RELEASED);
    //  activity.setStatus(ACTIVITY_STATUS_TEST_PASS);
    //  // 已完成的按照上架时间降序，优先级降序排列
    //  p.setOrderBy("b.\"launchTime\" desc, b.priority, b.\"createDate\", b.id");
    // }
    //
    // User user = UserUtils.getUser();
    // if (user.getBrand() != null && !"".equals(user.getBrand())) {
    //  activity.setBrand(user.getBrand());
    // }

    Page<Template> page = templateService.findPage(p, template);
    model.addAttribute("page", page);
    // model.addAttribute("priorities", DictUtils.getDictList("priorities"));
    // if (!model.containsAttribute("releaseStatus")) {
    // model.addAttribute("releaseStatus", activity.getReleaseStatus());
    // }
    // model.addAttribute("status", activity.getStatus());
    model.addAttribute("summary", template.getSearchKey());
    // model.addAttribute("RELEASE_STATUS_UNRELEASED", RELEASE_STATUS_UNRELEASED);
    // model.addAttribute("RELEASE_STATUS_RELEASED", RELEASE_STATUS_RELEASED);
  }
}
