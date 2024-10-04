package cn.bootx.platform.starter.file.handler;

import cn.bootx.platform.starter.file.dao.UploadFileManager;
import cn.bootx.platform.starter.file.entity.UploadFileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.recorder.DefaultFileRecorder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * x.file.storage 文件上传信息储存
 * @author xxm
 * @since 2023/11/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileDetailRecordHandler extends DefaultFileRecorder {
    private final UploadFileManager uploadFileManager;

    /**
     * 保存文件记录
     */
    @Override
    public boolean save(FileInfo fileInfo) {
        UploadFileInfo uploadFileInfo = UploadFileInfo.init(fileInfo);
        uploadFileInfo.setCreateTime(LocalDateTime.now());
        uploadFileManager.save(uploadFileInfo);
        fileInfo.setId(String.valueOf(uploadFileInfo.getId()));
        return true;
    }

    /**
     * 根据URL获取文件记录
     */
    @Override
    public FileInfo getByUrl(String url) {
        return uploadFileManager.findByUrl(url).map(UploadFileInfo::toFileInfo).orElse(null);
    }

    /**
     * 根据URL删除
     */
    @Override
    public boolean delete(String url) {
            return uploadFileManager.deleteByUrl(url);
    }
}
