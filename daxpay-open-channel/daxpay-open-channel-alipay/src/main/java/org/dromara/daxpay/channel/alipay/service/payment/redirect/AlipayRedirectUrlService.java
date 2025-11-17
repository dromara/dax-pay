package org.dromara.daxpay.channel.alipay.service.payment.redirect;

import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.service.config.AlipayConfigService;
import org.dromara.daxpay.payment.common.context.PaymentReqInfoLocal;
import org.dromara.daxpay.payment.common.util.PayUtil;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderExpandManager;
import org.dromara.daxpay.payment.pay.local.PaymentContextLocal;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 同步通知回调处理
 * @author xxm
 * @since 2024/9/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayRedirectUrlService {
    private final AlipayConfigService aliPayConfigService;
    private final PayOrderManager payOrderManager;
    private final PayOrderExpandManager payOrderExpandManager;

    /**
     * 回调地址处理
     */
    public String redirect(HttpServletRequest request, boolean isv){

        Map<String, String> map = PayUtil.toMap(request);
        // 首先进行验签
        if (!aliPayConfigService.verifyNotify(map,isv)){
            log.error("支付宝同步通知消息验签失败");
            throw new ValidationFailedException("支付宝同步通知消息验签失败");
        }
        // 获取订单号
        String outTradeNo = map.get(AlipayCode.ResponseParams.OUT_TRADE_NO);
        PayOrder order = payOrderManager.findByOrderNo(outTradeNo)
                .orElseThrow(() -> new DataNotExistException("订单不存在"));
        var orderExpand = payOrderExpandManager.findById(order.getId())
                .orElseThrow(() -> new DataNotExistException("支付订单扩展信息不存在!"));
        // 订单配置后跳转到指定的地址, 不存在跳转到预设的地址
        if (StrUtil.isNotBlank(orderExpand.getReturnUrl())){
            return StrUtil.format("{}?biz_trade_no={}&trade_no={}", orderExpand.getReturnUrl(),order.getBizOrderNo(),order.getOrderNo() );
        } else {
            PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
            String serverUrl = reqInfo.getGatewayH5Url();
            return StrUtil.format("{}/paySuccess/{}", serverUrl,order.getOrderNo());

        }
    }
}
