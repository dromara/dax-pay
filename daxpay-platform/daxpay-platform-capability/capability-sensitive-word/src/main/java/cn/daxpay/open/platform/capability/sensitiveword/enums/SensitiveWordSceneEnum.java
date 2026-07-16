package cn.daxpay.open.platform.capability.sensitiveword.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/// # 敏感词校验场景
///
@Getter
@RequiredArgsConstructor
public enum SensitiveWordSceneEnum implements I18nSupport {

    /// 支付标题
    PAY_TITLE("pay_title"),
    /// 支付描述
    PAY_DESCRIPTION("pay_description"),
    /// 商品名称
    GOODS_NAME("goods_name"),
    /// 商品描述
    GOODS_DESCRIPTION("goods_description"),
    /// 商户名称
    MCH_NAME("mch_name"),
    /// 应用名称
    APP_NAME("app_name"),
    /// 门店名称
    STORE_NAME("store_name"),
    /// 用户显示名
    USER_NAME("user_name"),
    /// 码牌名称
    QR_NAME("qr_name"),
    /// 公告
    NOTICE("notice"),
    /// 协议
    PROTOCOL("protocol"),
    /// 管理端试检
    MANUAL_CHECK("manual_check"),
    /// 通用文本
    GENERAL("general");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.sensitive_word_scene";
    }

    public static Optional<SensitiveWordSceneEnum> findByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.code.equals(code)).findFirst();
    }
}

