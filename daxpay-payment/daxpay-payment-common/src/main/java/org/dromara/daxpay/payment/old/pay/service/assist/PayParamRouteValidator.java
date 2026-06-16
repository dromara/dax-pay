package org.dromara.daxpay.payment.old.pay.service.assist;

import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

/// # 支付参数路由校验
///
@Component
public class PayParamRouteValidator {

    /// 校验支付参数与路由模式相关的字段
    public void validate(PayParam payParam) {
        if (StrUtil.isNotBlank(payParam.getProduct())) {
            if (StrUtil.isBlank(payParam.getMethod())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.methodRequiredWhenProductSet");
            }
            return;
        }
        if (StrUtil.isBlank(payParam.getProvider())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.providerRequired");
        }
    }
}
