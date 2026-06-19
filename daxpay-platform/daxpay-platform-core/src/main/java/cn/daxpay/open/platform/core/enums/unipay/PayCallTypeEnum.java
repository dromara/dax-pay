package cn.daxpay.open.platform.core.enums.unipay;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import cn.daxpay.open.platform.core.exception.config.ConfigNotExistException;
import java.util.Arrays;
import java.util.Objects;

/// # 支付调起方式
///
/// 字典: gateway_call_type
@Getter
@RequiredArgsConstructor
public enum PayCallTypeEnum implements I18nSupport {

    /// 扫码支付
    QR_CODE("qr_code"),
    /// 跳转链接
    LINK("link"),
    /// 小程序跳转链接
    MINI_APP("mini_app"),
    /// JSAPI
    JSAPI("jsapi"),
    /// 表单方式
    FROM("from"),
    /// APP支付方式
    APP("app");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.gateway_call_type";
    }
    public static PayCallTypeEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                // 通用: 不支持的收银台类型: {0}
                .orElseThrow(() -> new ConfigNotExistException("error.common.payCallTypeNotSupported", code));
    }

}
