package cn.daxpay.open.payment.common.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 收银场景(环境识别)
///
/// 字典: cashier_scene；
/// 聚合扫码使用微信/支付宝/云闪付/抖音；
/// 收银台 H5 额外支持 browser(普通浏览器), 聚合配置不使用 browser。
@Getter
@RequiredArgsConstructor
public enum CashierSceneEnum implements I18nSupport {

    /// 普通浏览器环境(仅收银台 H5 配置使用)
    BROWSER("browser"),
    /// 微信环境
    WECHAT_PAY("wechat_pay"),
    /// 支付宝环境
    ALIPAY("alipay"),
    /// 云闪付环境
    UNION_PAY("union_pay"),
    /// 抖音环境
    DOUYIN("douyin"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.cashier_scene";
    }

    public static CashierSceneEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.cashierSceneNotExist", code));
    }
}
