package com.tsbg.mis.serviceImpl.jurisdiction;

import com.tsbg.mis.jurisdiction.model.TokenList;
import com.tsbg.mis.dao.jurisdiction.TokenListDao;
import com.tsbg.mis.service.jurisdiction.TokenListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenListServiceImpl implements TokenListService {

    @Autowired
    private TokenListDao tokenListDao;

    @Override
    public int insertSelective(TokenList record) {
        return tokenListDao.insertSelective(record);
    }

    @Override
    public int updateStatusByTokenCode(String token) {
        return tokenListDao.updateStatusByTokenCode(token);
    }

    @Override
    public int selectCountFromTokenList(String token) {
        return tokenListDao.selectCountFromTokenList(token);
    }
}
