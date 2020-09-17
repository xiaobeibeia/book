package com.tsbg.mis.serviceImpl.jurisdiction;

import com.alibaba.fastjson.JSONObject;
import com.tsbg.mis.dao.jurisdiction.PermDao;
import com.tsbg.mis.service.jurisdiction.PermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermServiceImpl implements PermService {

    @Autowired
    private PermDao permDao;

    @Override
    public JSONObject getMyUserPermission(String userCode, Integer projId) {
        return permDao.getMyUserPermission(userCode,projId);
    }

    @Override
    public String selectPermListByUserCode(String userCode) {
        return permDao.selectPermListByUserCode(userCode);
    }

    @Override
    public List<String> selectPowerDetailByPowerList(List<Integer> powerList) {
        return permDao.selectPowerDetailByPowerList(powerList);
    }

    @Override
    public String selectAssetsPowerByUserCode(String userCode) {
        return permDao.selectAssetsPowerByUserCode(userCode);
    }
}
