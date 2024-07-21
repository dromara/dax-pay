package cn.daxpay.multi.channel.wechat.service.pay;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.core.exception.AmountExceedLimitException;
import cn.daxpay.multi.core.param.trade.pay.PayParam;
import cn.daxpay.multi.core.util.PayUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信支付服务
 * @author xxm
 * @since 2024/7/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayService {
    /**
     * 校验
     */
    public void validation(PayParam payParam, WechatPayConfig weChatPayConfig) {
        // 支付金额是否超限
        if (PayUtil.isGreaterThan(payParam.getAmount(),weChatPayConfig.getLimitAmount())) {
            throw new AmountExceedLimitException("微信支付金额超限");
        }
    }
}
