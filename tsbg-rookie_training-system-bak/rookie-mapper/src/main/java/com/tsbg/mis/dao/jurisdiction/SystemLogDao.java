package com.tsbg.mis.dao.jurisdiction;

import com.tsbg.mis.log.model.SystemLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author: 海波
 * @Date: 2019/12/4 16:09
 */
@Mapper
@Component
public interface SystemLogDao {

    //记录操作日志
    int insert(SystemLog systemLog);
}
