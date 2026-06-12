package org.dromara.daxpay.platform.capability.file.result;

import lombok.Data;
import lombok.experimental.Accessors;

/// # S3文件上传结果
///
@Data
@Accessors(chain = true)
public class S3UploadResult {

    /// 文件名（UUID.后缀，用于访问/下载）
    private String filename;
}
