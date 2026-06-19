package cn.daxpay.open.platform.capability.file.code;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 文件访问类型枚举
///
@Getter
@RequiredArgsConstructor
public enum UploadAccessTypeEnum implements I18nSupport {

    /// 公开文件
    PUBLIC("public"),
    /// 私有文件
    PRIVATE("private");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.upload_access_type";
    }
}
