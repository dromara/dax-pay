package org.dromara.daxpay.channel.wechat.service.payment.pay.isv;

import cn.bootx.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.param.pay.WechatPayParam;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 微信支付服务
 *
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
        PayMethodEnum methodEnum = PayMethodEnum.findByCode(payParam.getMethod());
        if (!List.of(PayMethodEnum.WECHAT_APP,PayMethodEnum.BARCODE,PayMethodEnum.WECHAT_APP,PayMethodEnum.WECHAT_QR,PayMethodEnum.WECHAT_JSAPI, PayMethodEnum.WECHAT_H5, PayMethodEnum.WECHAT_MINI).contains(methodEnum)) {
            throw new ValidationFailedException("不支持的支付方式");
        }
        // 微信JSAPI/小程序支付
        if (List.of(PayMethodEnum.WECHAT_JSAPI, PayMethodEnum.WECHAT_MINI).contains(methodEnum)) {
            if (Objects.isNull(payParam.getOpenId())) {
                throw new ValidationFailedException("微信JSAPI支付必须传入openId参数");
            }
        }
        // 付款码支付
        if (Objects.equals(methodEnum, PayMethodEnum.BARCODE)) {
            if (Objects.isNull(payParam.getAuthCode())) {
                throw new ValidationFailedException("微信付款码支付必须传入付款码参数");
            }
        }
    }
}
