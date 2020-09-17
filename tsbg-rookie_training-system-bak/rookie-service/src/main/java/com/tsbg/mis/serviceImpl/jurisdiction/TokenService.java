package com.tsbg.mis.serviceImpl.jurisdiction;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tsbg.mis.jurisdiction.model.TokenList;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.service.jurisdiction.TokenListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/***
 * token 下发
 */
@Service("TokenService")
public class TokenService {

	@Autowired
	private TokenListService tokenBlackService;

	public String getToken(UserInfo userInfo) {
		Date start = new Date();
		long currentTime = System.currentTimeMillis() + 60 * 60 * 1000 * 3;//三小时有效时间
		Date end = new Date(currentTime);
		String token = JWT.create().withAudience(userInfo.getUserId().toString()).withIssuedAt(start).withExpiresAt(end)
				.sign(Algorithm.HMAC256(userInfo.getUserPwd()));
		//在数据库插入token
		TokenList tokenList = new TokenList();
		if(null != token && !("").equals(token)){
			tokenList.setTokenCode(token);
			tokenBlackService.insertSelective(tokenList);
		}
		return token;
	}
}
