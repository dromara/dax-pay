package cn.daxpay.open.platform.core.enums.merchant;

import cn.daxpay.open.platform.core.exception.config.ConfigNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 门店状态
///
/// 字典: store_status
@Getter
@RequiredArgsConstructor
public enum StoreStatusEnum implements I18nSupport {

    /// 禁用
    DISABLED("disabled"),
    /// 启用
    ENABLE("enable");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.store_status";
    }

    /// 根据编码查找
    public static StoreStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 未找到对应的门店状态类型: {0}
                .orElseThrow(() -> new ConfigNotExistException("error.common.storeStatusNotFound", code));
    }

}
