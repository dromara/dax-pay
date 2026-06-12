package org.dromara.daxpay.platform.capability.file.param;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

/// # 文件上传预签名请求参数
///
@Data
@Accessors(chain = true)
public class FileUploadPresignParam {

    /// 文件名
    private String fileName;

    /// 文件大小(字节)
    private Long fileSize;

    /// 文件MIME类型
    private String contentType;

    /// 访问类型
    /// @see FileAccessTypeEnum
    private String accessType;

    /// 业务类型
    private String businessType;

    /// 业务主键
    private String businessId;
}

