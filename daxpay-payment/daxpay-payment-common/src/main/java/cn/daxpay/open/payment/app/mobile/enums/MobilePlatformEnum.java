package cn.daxpay.open.payment.app.mobile.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 移动端平台
///
/// 移动端应用所部署的具体移动平台分类
/// 字典: mobile_platform
@Getter
@RequiredArgsConstructor
public enum MobilePlatformEnum implements I18nSupport {

    /// 微信公众号(H5)
    WX_H5("wx_h5"),
    /// 微信小程序
    WX_MINI("wx_mini"),
    /// 支付宝小程序
    ALIPAY_MINI("alipay_mini"),
    /// 抖音小程序
    DY_MINI("dy_mini"),
    /// 安卓APP
    ANDROID("android"),
    /// iOS APP
    IOS("ios");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.mobile_platform";
    }

    /// 根据编码查找
    public static MobilePlatformEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 未找到对应的移动端平台: {0}
                .orElseThrow(() -> new DataNotExistException("error.mobile_app.platformNotFound", code));
    }
}
