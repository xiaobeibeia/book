package com.tsbg.mis.serviceImpl.jurisdiction.shiro;

import com.alibaba.fastjson.JSONObject;
import com.tsbg.mis.util.constants.ErrorEnum;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 对没有登录的请求进行拦截, 全部返回json信息. 覆盖掉shiro原本的跳转login.jsp的拦截方式
 */
public class AjaxPermissionsAuthorizationFilter extends FormAuthenticationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if (null != request &&request instanceof HttpServletRequest) {
			if(null != ((HttpServletRequest) request).getMethod()){
				if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")
						|| ((HttpServletRequest) request).getMethod().toUpperCase().equals("POST")) {
					return true;
				}
			}
			return super.isAccessAllowed(request, response, mappedValue);
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", ErrorEnum.E_20011.getErrorCode());
		jsonObject.put("message", ErrorEnum.E_20011.getErrorMessage());
		PrintWriter out = null;
		HttpServletResponse res = (HttpServletResponse) response;
		try {
			res.setCharacterEncoding("UTF-8");
			res.setContentType("application/json");
			out = response.getWriter();
			out.println(jsonObject);
		} catch (Exception e) {
		} finally {
			if (null != out) {
				out.flush();
				out.close();
			}
		}
		return false;
	}

	@Bean
	public FilterRegistrationBean registration(AjaxPermissionsAuthorizationFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.setEnabled(false);
		return registration;
	}
}
