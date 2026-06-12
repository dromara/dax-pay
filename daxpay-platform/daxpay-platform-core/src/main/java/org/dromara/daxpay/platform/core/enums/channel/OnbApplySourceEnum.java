package org.dromara.daxpay.platform.core.enums.channel;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 进件申请来源
///
@Getter
@RequiredArgsConstructor
public enum OnbApplySourceEnum implements I18nSupport {

    /// 商户创建
    MERCHANT("merchant"),
    /// 运营创建
    ADMIN("admin"),
    /// 接口创建
    GATEWAY("system");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.onb_apply_source";
    }

}
