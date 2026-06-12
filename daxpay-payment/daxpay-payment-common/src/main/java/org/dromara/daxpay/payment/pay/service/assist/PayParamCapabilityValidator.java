package org.dromara.daxpay.payment.pay.service.assist;

import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.payment.pay.service.masterdata.provider.PayProviderMethodService;
import org.dromara.daxpay.platform.core.util.PayProviderMethodUtil;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/// # 下单参数：支付渠道与支付方式校验
///
/// `PayProviderMethodUtil` 仅用于判断某 `method` 是否必须同传 `provider`；
/// 目录合法性以 `PayProviderMethodService#contains`（DB 已启用目录）为准。
///
@Component
@RequiredArgsConstructor
public class PayParamCapabilityValidator {

    private final PayProviderMethodService payProviderMethodService;

    /// 当已传入支付方式 method 时，校验其与支付渠道 provider 属于渠道支付方式目录（路由解析前）
    public void validate(PayParam payParam) {
        if (StrUtil.isBlank(payParam.getMethod())) {
            // 基础模式可先只传 provider，由路由回填 method
            return;
        }
        PayMethodEnum methodEnum = PayMethodEnum.findByCode(payParam.getMethod());
        // other 无法单从 method 推导渠道，必须同传 provider
        if (PayProviderMethodUtil.requiresProviderForMethod(payParam.getMethod())) {
            if (StrUtil.isBlank(payParam.getProvider())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "error.payment.capability.providerRequiredForMethod",
                        I18nUtil.getEnumName(methodEnum));
            }
        }
        if (StrUtil.isBlank(payParam.getProvider())) {
            return;
        }
        PayProviderEnum provider = PayProviderEnum.findByCode(payParam.getProvider());
        if (provider == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.capability.invalidProvider");
        }
        // 校验 (provider, method) 是否为平台认可的目录组合
        if (!payProviderMethodService.contains(provider.getCode(), payParam.getMethod())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.capability.methodNotInDirectory",
                    I18nUtil.getEnumName(methodEnum),
                    I18nUtil.getEnumName(provider));
        }
    }
}
