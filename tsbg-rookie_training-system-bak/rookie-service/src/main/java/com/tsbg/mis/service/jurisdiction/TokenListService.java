package com.tsbg.mis.service.jurisdiction;

import com.tsbg.mis.jurisdiction.model.TokenList;

public interface TokenListService {

    int insertSelective(TokenList record);

    int updateStatusByTokenCode(String token);

    int selectCountFromTokenList(String token);
}
