package com.tsbg.mis.dao.jurisdiction;


import com.tsbg.mis.jurisdiction.model.FileInfo;
import com.tsbg.mis.jurisdiction.vo.FileInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Component
@Repository
public interface FileInfoDao {

    int deleteByPrimaryKey(Integer fileId);

    int insert(FileInfo record);

    int insertSelective(FileInfo record);
    int testInsert();

    FileInfo selectByPrimaryKey(Integer fileId);

    int updateByPrimaryKeySelective(FileInfo record);

    int updateByPrimaryKey(FileInfo record);

    //重复文件名判断
    int selectFileCountByFileName(String filename, Integer partnerNo);

    //判断文件是否未修改
    int judgeIfFileChanged(@Param("partnerNo") Integer partnerNo, @Param("fileName") String fileName);

    //查找当前文件的ID和更新时间
    FileInfo selectIDandDate();

    //查询当前文件编号在表中是否存在
    int selectIfExistId(int fileId);

    //更新最新的文件记录
    int updateRecord(@Param("partnerNo") int partnerNo, @Param("updater") String updater, @Param("fileId") int fileId);

    //通过文件名查询文件路径
    String selectRealPathByName(String fileName, Integer partnerNo);

    //根据公司编号查询文件列表
    List<String> selectFileListByNo(Integer partnerNo);

    //根据公司编号查询当前文件列表对应的编号
    List<Integer> selectFileNoByNo(Integer partnerNo);

    //根据文件名查询当前文件编号
    List<Integer> selectFileIdByFileName(String fileName);

    //根据文件编号修改文件状态
    int updateFileStatusByFileNo(Integer fileNo);

    //通過文件名、處理反饋附件編號查詢文件附件
    String selectRealPathByNameAndQuestionHandleId(String fileName, Integer questionHandleId);

    //通過文件名、反饋附件編號查詢文件附件
    String selectRealPathByNameAndQuestionFeedBackId(String fileName, Integer questionFeedbackId);

    //根据公司编号和用户工号去修改最后下载者
    int updateDownloader(String userCode, Integer fileNo);

    //根據處理反饋編號修改文件狀態
    int updateAllFileStatusByQuestionHandleId(Integer questionHandleId);

    //根据上传时的文件名查询文件状态
    List<Integer> selectFileStatusByFileName(String fileName, Integer partnerNo);

    //通过公司编号和文件名定位文件ID
    Integer selectFileNo(Integer partnerNo, String fileName);

    //通過QuestionHandleId和文件名定位文件ID
    Integer selectFileNoByQuestionHandleIdAdnFileName(Integer questionHandleId, String fileName);

    //通過QuestionFeedbackId和文件名定位文件ID
    Integer selectFileNoByQuestionFeedbackIdAdnFileName(Integer questionFeedbackId, String fileName);

    //根據問題反饋id查詢文件的路徑
    List<FileInfo> selectFilePathByQuestionFeedBackId(Integer questionFeedbackId);

    //根據問題反饋id查詢文件名
    List<FileInfo> selectFileNameByQuestionFeedBackId(Integer questionFeedbackId);

    //根據QuestionHandleId查詢文件名
    List<FileInfo> selectFileNameByQuestionHandleId(Integer questionHandleId);

    //通過處理反饋id更新文件的狀態為刪除（實際未刪除）
    int UpdateFileByFileNameAndQuestionHandleId(String fileName, Integer questionHandleId);

    //通過問題反饋id更新文件的狀態為刪除（實際未刪除）
    int UpdateFileByFileNameAndQuestionFeedBackId(String fileName, Integer questionFeedbackId);

    //根据處理反饋编号查询当前文件列表对应的编号
    List<Integer> selectFileNoByQuestionHandleId(Integer questionHandleId);

    //软删除file_info中的文件
    Integer setFileStatusStopByFileId(Integer fileId);

    //根据fileId获得文件路径
    String getFilePathByFileId(@Param("fileId") Integer fileId);

    // 根据 rel_table_name 查出所有记录
    List<FileInfoVo> selectFileByTableName(List<Integer> registrationBatchList);
}
