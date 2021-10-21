/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.common.web;

import com.yum.ucp.common.beanvalidator.BeanValidators;
import com.yum.ucp.common.mapper.JsonMapper;
import com.yum.ucp.common.utils.DateUtils;
import com.yum.ucp.modules.common.entity.CommonSelect;
import com.yum.ucp.modules.sys.entity.Menu;
import com.yum.ucp.modules.sys.service.MenuService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 控制器支持类
 *
 * @author ThinkGem
 * @version 2013-3-23
 */
public abstract class BaseController {
    @Autowired
    public MenuService menuService;

    /**
     * 日志对象
     */
    public Logger logger = LoggerFactory.getLogger(this.getClass());

    public static int pageSize = 20;

    public static int indexPageSize = 5;
    /**
     * 管理基础路径
     */
    @Value("${adminPath}")
    protected String adminPath;

    @Value("#{APP_PROP['temp.path']}")
    public String tempPath;
    /**
     * 前端基础路径
     */
    @Value("${frontPath}")
    protected String frontPath;

    /**
     * 前端URL后缀
     */
    @Value("${urlSuffix}")
    protected String urlSuffix;

    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator validator;

    /**
     * 服务端参数有效性验证
     *
     * @param object 验证的实体对象
     * @param groups 验证组
     * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
     */
    protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
        try {
            BeanValidators.validateWithException(validator, object, groups);
        } catch (ConstraintViolationException ex) {
            List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
//			list.add(0, "数据验证失败：");
            addMessage(model, list.toArray(new String[]{}));
            return false;
        }


        return true;
    }

    /**
     * 服务端参数有效性验证
     *
     * @param object 验证的实体对象
     * @param groups 验证组
     * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
     */
    protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
        try {
            BeanValidators.validateWithException(validator, object, groups);
        } catch (ConstraintViolationException ex) {
            List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
            list.add(0, "数据验证失败：");
            addMessage(redirectAttributes, list.toArray(new String[]{}));
            return false;
        }
        return true;
    }

    /**
     * 服务端参数有效性验证
     *
     * @param object 验证的实体对象
     * @param groups 验证组，不传入此参数时，同@Valid注解验证
     */
    protected void beanValidator(Object object, Class<?>... groups) {
        BeanValidators.validateWithException(validator, object, groups);
    }

    /**
     * 添加Model消息
     *
     * @param messages
     */
    protected void addMessage(Model model, String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
        }
        model.addAttribute("message", sb.toString());
    }

    /**
     * 添加Flash消息
     *
     * @param messages
     */
    protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
        }
        redirectAttributes.addFlashAttribute("message", sb.toString());
        redirectAttributes.addFlashAttribute("alertClass", "success");
    }

    /**
     * 添加Flash消息
     *
     * @param messages
     */
    protected void addErrorMessage(RedirectAttributes redirectAttributes, String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
        }
        redirectAttributes.addFlashAttribute("message", sb.toString());
        redirectAttributes.addFlashAttribute("alertClass", "error");
    }

    /**
     * 客户端返回JSON字符串
     *
     * @param response
     * @param object
     * @return
     */
    protected String renderString(HttpServletResponse response, Object object) {
        return renderString(response, JsonMapper.toJsonString(object), "application/json");
    }

    /**
     * 客户端返回字符串
     *
     * @param response
     * @param string
     * @return
     */
    protected String renderString(HttpServletResponse response, String string, String type) {
        try {
            response.reset();
            response.setContentType(type);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
    public String bindException() {
        return "error/400";
    }

    /**
     * 授权登录异常
     */
    @ExceptionHandler({AuthenticationException.class})
    public String authenticationException() {
        return "error/403";
    }

    /**
     * 初始化数据绑定
     * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
     * 2. 将字段中Date类型转换为String类型
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
//			@Override
//			public String getAsText() {
//				Object value = getValue();
//				return value != null ? DateUtils.formatDateTime((Date)value) : "";
//			}
        });
    }

    /**
     * 执行接口
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    protected HttpResponse execute(String url, String params) throws IOException
    {
        //POST的URL
        HttpPost httppost = new HttpPost(url);
        //添加参数
        httppost.addHeader("Content-type", "application/json");
        httppost.setHeader("Accept", "application/json");
        httppost.setEntity(new StringEntity(params, Charset.forName("UTF-8")));
        //设置编码
        return new DefaultHttpClient().execute(httppost);
    }

    /**
     * 根据项目key,issueType过滤status
     *
     * @param projectId
     * @param issueTypeId
     * @return
     */
    public List<CommonSelect> findCommonSelects(String projectId, String issueTypeId) {
        String strs = "";
        StringBuffer parentIds = new StringBuffer();
        if (StringUtils.isNotBlank(projectId)) {
            parentIds.append(projectId).append(",");
        }
        if (StringUtils.isNotBlank(issueTypeId)) {
            parentIds.append(issueTypeId).append(",");
        }
        if (parentIds.toString().length() > 0) {
            strs = parentIds.substring(0, parentIds.lastIndexOf(","));
        }
        List<Menu> menus = menuService.findMenusByParentIds(strs, 3);
        List<CommonSelect> commonSelectLists = new ArrayList<>();
        List<String> list = new ArrayList<>();
        for (Menu menu : menus) {
            if (!list.contains(menu.getHref())) {
                commonSelectLists.add(new CommonSelect(menu.getHref(), menu.getName(), null));
            }
            list.add(menu.getHref());
        }
        return commonSelectLists;
    }
}