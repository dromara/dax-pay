package org.dromara.daxpay.platform.core.model;

import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 渠道支付方式目录项
///
@Getter
@RequiredArgsConstructor
public class PayProviderMethodEntry {

    /// 支付渠道
    private final PayProviderEnum provider;

    /// 支付方式（与 provider 成对；barcode 等同码在不同渠道下为不同目录项）
    private final PayMethodEnum method;

    public String getProviderCode() {
        return provider.getCode();
    }

    public String getMethodCode() {
        return method.getCode();
    }
}
