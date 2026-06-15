package org.dromara.daxpay.platform.core.enums.merchant;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.platform.core.exception.config.ConfigNotExistException;
import java.util.Arrays;

/// # 商户应用状态
///
/// 字典: mch_app_status
@Getter
@RequiredArgsConstructor
public enum MchAppStatusEnum implements I18nSupport {

    /// 禁用
    DISABLED("disabled"),
    /// 启用
    ENABLE("enable");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.mch_app_status";
    }
    /// 根据编码查找
    public static MchAppStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 未找到对应的商户应用状态类型: {0}
                .orElseThrow(() -> new ConfigNotExistException("error.common.mchAppStatusNotFound", code));
    }

}
