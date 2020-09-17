package com.tsbg.mis.serviceImpl.jurisdiction;

import com.tsbg.mis.jurisdiction.model.FileInfo;
import com.tsbg.mis.dao.jurisdiction.FileInfoDao;
import com.tsbg.mis.service.jurisdiction.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Autowired
    private FileInfoDao fileInfoDao;

    @Override
    public int insertSelective(FileInfo record) {
        return fileInfoDao.insertSelective(record);
    }

    @Override
    public int selectFileCountByFileName(String filename, Integer partnerNo) {
        return fileInfoDao.selectFileCountByFileName(filename,partnerNo);
    }

    @Override
    public String selectRealPathByName(String fileName, Integer partnerNo) {
        return fileInfoDao.selectRealPathByName(fileName,partnerNo);
    }

    @Override
    public List<Integer> selectFileNoByNo(Integer partnerNo) {
        return fileInfoDao.selectFileNoByNo(partnerNo);
    }

    @Override
    public List<Integer> selectFileIdByFileName(String fileName) {
        return fileInfoDao.selectFileIdByFileName(fileName);
    }

    @Override
    public int updateFileStatusByFileNo(Integer fileNo) {
        return fileInfoDao.updateFileStatusByFileNo(fileNo);
    }

    @Override
    public List<Integer> selectFileStatusByFileName(String fileName, Integer partnerNo) {
        return fileInfoDao.selectFileStatusByFileName(fileName,partnerNo);
    }

    @Override
    public Integer selectFileNo(Integer partnerNo, String fileName) {
        return fileInfoDao.selectFileNo(partnerNo,fileName);
    }

    @Override
    public int updateDownloader(String userCode, Integer fileNo) {
        return fileInfoDao.updateDownloader(userCode,fileNo);
    }

    //根據問題反饋id查詢文件名
    public List<FileInfo> selectFileNameByQuestionFeedBackId(Integer questionFeedbackId){
        return fileInfoDao.selectFileNameByQuestionFeedBackId(questionFeedbackId);
    }

    //根據QuestionHandleId查詢文件名
    public List<FileInfo> selectFileNameByQuestionHandleId(Integer questionHandleId){
        return fileInfoDao.selectFileNameByQuestionHandleId(questionHandleId);
    }

    //通過處理反饋id更新文件的狀態為刪除（實際未刪除）
    @Override
    public int UpdateFileByFileNameAndQuestionHandleId(String fileName,Integer questionHandleId){
        return fileInfoDao.UpdateFileByFileNameAndQuestionHandleId(fileName,questionHandleId);
    }

    //通過問題反饋id更新文件的狀態為刪除（實際未刪除）
    @Override
    public int UpdateFileByFileNameAndQuestionFeedBackId(String fileName,Integer questionFeedbackId){
        return fileInfoDao.UpdateFileByFileNameAndQuestionFeedBackId(fileName,questionFeedbackId);
    }

    @Override
    //通過文件名、反饋附件編號查詢文件附件
    public String selectRealPathByNameAndQuestionFeedBackId(String fileName,Integer questionFeedbackId){
        return fileInfoDao.selectRealPathByNameAndQuestionFeedBackId(fileName,questionFeedbackId);
    }

    @Override
    //通過文件名、處理反饋附件編號查詢文件附件
    public String selectRealPathByNameAndQuestionHandleId(String fileName,Integer questionHandleId){
        return fileInfoDao.selectRealPathByNameAndQuestionHandleId(fileName,questionHandleId);
    }

    @Override
    public Integer setFileStatusStopByFileId(Integer fileId) {
        return fileInfoDao.setFileStatusStopByFileId(fileId);
    }

    @Override
    public String getFilePathByFileId(Integer fileId) {
        return fileInfoDao.getFilePathByFileId(fileId);
    }
}
