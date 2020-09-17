package com.tsbg.mis.jurisdiction.vo;

import com.tsbg.mis.jurisdiction.model.FileInfo;

/**
 * @PackgeName: com.tsbg.mis.file.model.file.vo
 * @ClassName: FileInfoVo
 * @Author: Nico
 * Date: 2019/12/26 0026 上午 11:10
 * Description:返回文件下载路径
 */
public class FileInfoVo extends FileInfo {

    //返给前端的文件下载路径
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
