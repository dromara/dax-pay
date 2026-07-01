package cn.daxpay.open.payment.old.pay.service.assist;

import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

/// # 支付参数路由校验
///
@Component
public class PayParamRouteValidator {

    /// 校验支付参数与路由模式相关的字段
    public void validate(NormalPayParam payParam) {
        if (StrUtil.isNotBlank(payParam.getProduct())) {
            if (StrUtil.isBlank(payParam.getMethod())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.methodRequiredWhenProductSet");
            }
        }
    }
}
