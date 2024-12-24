package org.dromara.daxpay.channel.wechat.service.pay.isv;

import cn.bootx.platform.core.exception.ValidationFailedException;
import cn.bootx.platform.core.util.BigDecimalUtil;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.param.pay.WechatPayParam;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.core.exception.AmountExceedLimitException;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 微信支付服务
 * @author xxm
 * @since 2024/7/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPaySubService {
    /**
     * 校验
     */
    public void validation(PayParam payParam, WechatPayParam wechatPayParam, WechatPayConfig weChatPayConfig) {
        // 支付金额是否超限
        if (BigDecimalUtil.isGreaterThan(payParam.getAmount(),weChatPayConfig.getLimitAmount())) {
            throw new AmountExceedLimitException("微信支付金额超限");
        }
        // 微信JSAPI支付
        if (Objects.equals(payParam.getMethod(), PayMethodEnum.JSAPI.getCode())){
            if (Objects.isNull(wechatPayParam.getOpenId())) {
                throw new ValidationFailedException("微信JSAPI支付必须传入openId参数");
            }
        }
        // 付款码支付
        if (Objects.equals(payParam.getMethod(), PayMethodEnum.BARCODE.getCode())){
            if (Objects.isNull(wechatPayParam.getAuthCode())) {
                throw new ValidationFailedException("微信付款码支付必须传入付款码参数");
            }
        }
    }
}
