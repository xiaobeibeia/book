package com.tsbg.mis.service.rookie;

import com.tsbg.mis.signed.bag.SignedPackage;
import com.tsbg.mis.util.ResultUtils;

/**
 * @author 汪永晖
 */
public interface SignedStreamService {

    /**
     * 新增签核流程
     *
     * @param signedPackage
     * @return
     */
    ResultUtils insert(SignedPackage signedPackage);


    /**
     * 查询流程
     *
     * @return
     */
    ResultUtils inquire();

    /**
     * 修改签核流程
     *
     * @return
     */
    ResultUtils update(SignedPackage signedPackage);

    /**
     * 删除签核流程
     *
     * @param businessId
     * @return
     */
    ResultUtils delete(Integer businessId);

    /**
     * 根据ID查询签核流程
     *
     * @param businessId
     * @return
     */
    ResultUtils selectBusiness(Integer businessId);

    /**
     * 根据报告ID查询签核进度
     *
     * @param id
     * @return
     */
    ResultUtils selectApprovalInfo(Integer id);

    /**
     * 查询签核进度
     *
     * @param reportId
     * @param reportType
     * @return
     */
    ResultUtils selectProgress(Integer reportId, Integer reportType);
}
