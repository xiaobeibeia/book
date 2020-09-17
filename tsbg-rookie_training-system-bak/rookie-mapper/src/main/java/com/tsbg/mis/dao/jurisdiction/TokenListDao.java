package com.tsbg.mis.dao.jurisdiction;

import com.tsbg.mis.jurisdiction.model.TokenList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenListDao {
    int deleteByPrimaryKey(Long tokenId);

    int insert(TokenList record);

    int insertSelective(TokenList record);

    TokenList selectByPrimaryKey(Long tokenId);

    int updateByPrimaryKeySelective(TokenList record);

    int updateByPrimaryKey(TokenList record);

    int updateStatusByTokenCode(String token);

    int selectCountFromTokenList(String token);
}