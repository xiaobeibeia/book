package com.tsbg.mis.dao.jurisdiction;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginDao {

    /**
     * 根据工号和密码查询对应的用户
     */
    JSONObject getMyUser(@Param("accountName") String accountName, @Param("userPwd") String userPwd);
}
