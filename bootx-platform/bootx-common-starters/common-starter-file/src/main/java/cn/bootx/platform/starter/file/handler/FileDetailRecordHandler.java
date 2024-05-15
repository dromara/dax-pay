package cn.bootx.platform.starter.file.handler;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.starter.file.dao.UploadFileManager;
import cn.bootx.platform.starter.file.entity.UploadFileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.springframework.stereotype.Service;

/**
 * x.file.storage 文件上传信息储存
 * @author xxm
 * @since 2023/11/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileDetailRecordHandler implements FileRecorder {
    private final UploadFileManager uploadFileManager;

    /**
     * 保存文件记录
     */
    @Override
    public boolean save(FileInfo fileInfo) {
        UploadFileInfo save = uploadFileManager.save(UploadFileInfo.init(fileInfo));
        fileInfo.setId(String.valueOf(save.getId()));
        return true;
    }

    /**
     * 根据 ID(Long) 获取文件记录, 注意不是根据URL
     */
    @Override
    public FileInfo getByUrl(String url) {
        try {
            Long id = Long.valueOf(url);
            return uploadFileManager.findById(id).map(UploadFileInfo::toFileInfo).orElse(null);
        } catch (NumberFormatException e) {
            throw new BizException("URL是文件的ID，注意不要传错参数");
        }
    }

    /**
     * 根据  ID(Long) 删除文件记录, 注意不是根据URL
     */
    @Override
    public boolean delete(String url) {
        try {
            Long id = Long.valueOf(url);
            return uploadFileManager.deleteById(id);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
