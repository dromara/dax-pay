package org.dromara.daxpay.platform.capability.file.code;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 文件上传状态枚举
///
@Getter
@RequiredArgsConstructor
public enum FileUploadStatusEnum implements I18nSupport {

    /// 待上传
    PENDING("pending"),
    /// 已上传
    UPLOADED("uploaded");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.file_upload_status";
    }
}
