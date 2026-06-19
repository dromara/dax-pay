package cn.daxpay.open.platform.capability.file.param;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

/// # 文件上传确认请求参数
///
@Data
@Accessors(chain = true)
public class FileUploadConfirmParam {

    /// 文件ID
    private Long fileId;

    /// 对象Key
    private String objectKey;

    /// 对象ETag
    private String etag;

    /// 文件大小(可选，用于辅助校验)
    private Long fileSize;

    /// 文件MIME类型(可选，用于辅助校验)
    private String contentType;
}
