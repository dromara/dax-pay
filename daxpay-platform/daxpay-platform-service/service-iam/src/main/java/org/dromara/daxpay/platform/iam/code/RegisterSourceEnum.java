package org.dromara.daxpay.platform.iam.code;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 注册来源
///
@Getter
@RequiredArgsConstructor
public enum RegisterSourceEnum implements I18nSupport {

    /// H5页面
    H5("H5"),
    /// 小程序
    MINI_APP("MINI_APP"),
    /// 后台创建
    ADMIN("ADMIN"),
    /// PC端
    PC("PC");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.register_source";
    }
}
