package cn.bootx.platform.daxpay.service.core.notice.service;

import cn.bootx.platform.daxpay.service.configuration.DaxPayProperties;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderExtraManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService;
import cn.bootx.platform.daxpay.service.param.channel.alipay.AliPayReturnParam;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付同步跳转服务类
 * @author xxm
 * @since 2024/2/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayReturnService {
    private final PayOrderQueryService payOrderQueryService;
    private final PayOrderExtraManager payOrderExtraManager;

    private final DaxPayProperties properties;

    /**
     * 支付宝同步回调
     */
    public String alipay(AliPayReturnParam param){
        PayOrderExtra payOrderExtra = payOrderExtraManager.findById(param.getOut_trade_no()).orElse(null);
        PayOrder prOrder = payOrderQueryService.findById(param.getOut_trade_no()).orElse(null);
        if (Objects.isNull(payOrderExtra) || Objects.isNull(prOrder)){
            return StrUtil.format("{}/result/error?msg={}", properties.getFrontUrl(), URLEncodeUtil.encode("支付订单有问题，请排查"));
        }

        // 如果不需要同步回调, 跳转到支付成功页面
        if (payOrderExtra.isNotReturn()){
            return StrUtil.format("{}/result/success?msg={}", properties.getFrontUrl(), URLEncodeUtil.encode("支付成功..."));
        }
        return StrUtil.format("{}?paymentId={}&businessNo={}", payOrderExtra.getReturnUrl(),prOrder.getId(),prOrder.getBusinessNo());
    }
}
