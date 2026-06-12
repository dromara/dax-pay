package org.dromara.daxpay.platform.capability.file.result;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;

/// # 文件上传预签名返回结果
///
@Data
@Accessors(chain = true)
public class FileUploadPresignResult {

    /// 文件ID
    private Long fileId;

    /// 存储源编码
    private String storageCode;

    /// 存储桶
    private String bucket;

    /// 对象Key
    private String objectKey;

    /// 文件名（UUID.后缀，用于访问/下载）
    private String filename;

    /// 上传预签名URL
    private String uploadUrl;

    /// 过期时间
    private LocalDateTime expireTime;
}
