package com.tsbg.mis.serviceImpl.jurisdiction.base;

import com.auth0.jwt.JWT;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.service.jurisdiction.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class TokenAnalysis {

	@Autowired
	private UserInfoService userInfoService;

	public  UserInfo getTokenUser() {
		UserInfo userInfo = new UserInfo();
		if(getRequest() != null){
			String token = getRequest().getHeader("token");// 从 http 请求头中取出 token
			String userId = JWT.decode(token).getAudience().get(0);
			//根据userId查询用户工号和姓名
			userInfo = userInfoService.selectUserMsgbyUserId(userId);
		}
		return userInfo;
	}

	/**
	 * 获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		return requestAttributes == null ? null : requestAttributes.getRequest();
	}
}
