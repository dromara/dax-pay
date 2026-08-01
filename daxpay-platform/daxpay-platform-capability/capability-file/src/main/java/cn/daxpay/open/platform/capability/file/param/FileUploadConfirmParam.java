package cn.daxpay.open.platform.capability.file.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 文件上传确认请求参数
///
@Data
@Accessors(chain = true)
public class FileUploadConfirmParam {

    /// 文件ID
    @NotNull(message = "{validation.field.fileId.notNull}")
    private Long fileId;

    /// 对象Key
    @NotNull(message = "{validation.field.objectKey.notBlank}")
    private String objectKey;

    /// 对象ETag
    private String etag;

    /// 文件大小(可选，用于辅助校验)
    private Long fileSize;

    /// 文件MIME类型(可选，用于辅助校验)
    private String contentType;
}
