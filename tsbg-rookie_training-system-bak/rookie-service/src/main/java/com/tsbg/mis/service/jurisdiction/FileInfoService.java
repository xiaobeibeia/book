package com.tsbg.mis.service.jurisdiction;

import com.tsbg.mis.jurisdiction.model.FileInfo;

import java.util.List;


public interface FileInfoService {

    int insertSelective(FileInfo record);

    //重复文件名判断
    int selectFileCountByFileName(String filename, Integer partnerNo);

    //通过文件名查询文件路径
    String selectRealPathByName(String fileName, Integer partnerNo);

    //根据公司编号查询当前文件列表对应的编号
    List<Integer> selectFileNoByNo(Integer partnerNo);

    //根据文件名查询当前文件编号
    List<Integer> selectFileIdByFileName(String fileName);

    //根据文件编号修改文件状态
    int updateFileStatusByFileNo(Integer fileNo);

    //根据上传时的文件名查询文件状态
    List<Integer> selectFileStatusByFileName(String fileName, Integer partnerNo);

    //通过公司编号和文件名定位文件ID
    Integer selectFileNo(Integer partnerNo, String fileName);

    //根据公司编号和用户工号去修改最后下载者
    int updateDownloader(String userCode, Integer fileNo);

    //根據問題反饋id查詢文件名
    List<FileInfo> selectFileNameByQuestionFeedBackId(Integer questionFeedbackId);

    //根據QuestionHandleId查詢文件名
    List<FileInfo> selectFileNameByQuestionHandleId(Integer questionHandleId);

    //通過處理反饋id更新文件的狀態為刪除（實際未刪除）
    int UpdateFileByFileNameAndQuestionHandleId(String fileName, Integer questionHandleId);

    //通過問題反饋id更新文件的狀態為刪除（實際未刪除）
    int UpdateFileByFileNameAndQuestionFeedBackId(String fileName, Integer questionFeedbackId);

    //通過文件名、處理反饋附件編號查詢文件附件
    String selectRealPathByNameAndQuestionHandleId(String fileName, Integer questionHandleId);

    //通過文件名、反饋附件編號查詢文件附件
    String selectRealPathByNameAndQuestionFeedBackId(String fileName, Integer questionFeedbackId);

    //软删除file_info中的文件
    Integer setFileStatusStopByFileId(Integer fileId);

    //根据fileId获得文件路径
    String getFilePathByFileId(Integer fileId);
}
