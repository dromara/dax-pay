package org.dromara.daxpay.payment.old.pay.service.assist;

import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

/// # 下单参数：支付方式校验（路由解析前）
///
@Component
public class PayParamCapabilityValidator {

    /// 当已传入支付方式 method 时，校验其必填性
    public void validate(PayParam payParam) {
        if (StrUtil.isBlank(payParam.getMethod())) {
            return;
        }
        PayMethodEnum methodEnum = PayMethodEnum.findByCode(payParam.getMethod());
        if (methodEnum == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.capability.invalidMethod",
                    payParam.getMethod());
        }
    }
}
