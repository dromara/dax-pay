package cn.daxpay.open.payment.app.mobile.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 移动端应用端类型
///
/// 区分移动端应用的用途分类: 商户端(商户员工使用)/管理端(平台运营使用)/收银台小程序(消费者扫码支付)
/// 字典: mobile_app_type
@Getter
@RequiredArgsConstructor
public enum MobileAppTypeEnum implements I18nSupport {

    /// 商户端
    MERCHANT("merchant"),
    /// 管理端
    ADMIN("admin"),
    /// 收银台小程序
    CASHIER("cashier");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.mobile_app_type";
    }

    /// 根据编码查找
    public static MobileAppTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 通用: 未找到对应的移动端应用端类型: {0}
                .orElseThrow(() -> new DataNotExistException("error.mobile_app.appTypeNotFound", code));
    }
}
