package cn.daxpay.open.payment.merchant.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 客户端运行形态
///
/// 与 [ClientEnvEnum] 正交: env 表示钱包宿主(微信/支付宝…), runtime 表示页面容器(H5/小程序)。
/// 字典: client_runtime
@Getter
@RequiredArgsConstructor
public enum ClientRuntimeEnum implements I18nSupport {

    /// H5 / WebView 内页面
    H5("h5"),
    /// 微信/支付宝/抖音等小程序
    MINI("mini"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.client_runtime";
    }

    public static ClientRuntimeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.clientRuntimeNotExist", code));
    }

    /// 空或非法时默认 H5(一期主路径)
    public static ClientRuntimeEnum ofOrDefault(String code) {
        if (StrUtil.isBlank(code)) {
            return H5;
        }
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(H5);
    }
}
