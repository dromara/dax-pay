package cn.bootx.platform.daxpay.service.core.payment.notice.service;

import cn.bootx.platform.daxpay.service.configuration.DaxPayProperties;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderExtraManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderExtra;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderQueryService;
import cn.bootx.platform.daxpay.service.core.system.config.service.PlatformConfigService;
import cn.bootx.platform.daxpay.service.param.channel.alipay.AliPayReturnParam;
import cn.bootx.platform.daxpay.service.param.channel.union.UnionPayReturnParam;
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
    private final PlatformConfigService platformConfigService;

    private final DaxPayProperties properties;

    /**
     * 支付宝同步回调
     */
    public String alipay(AliPayReturnParam param){
        PayOrderExtra payOrderExtra = payOrderExtraManager.findById(param.getOut_trade_no()).orElse(null);
        PayOrder payOrder = payOrderQueryService.findById(param.getOut_trade_no()).orElse(null);
        if (Objects.isNull(payOrderExtra) || Objects.isNull(payOrder)){
            return StrUtil.format("{}/result/error?msg={}", properties.getFrontH5Url(), URLEncodeUtil.encode("支付订单有问题，请排查"));
        }

        // 如果同步跳转参数为空, 获取系统配置地址, 系统配置如果也为空, 则返回默认地址
        String returnUrl = payOrderExtra.getReturnUrl();
        if (StrUtil.isBlank(returnUrl)){
            returnUrl = platformConfigService.getConfig().getReturnUrl();
        }
        if (StrUtil.isNotBlank(returnUrl)){
            return StrUtil.format("{}?orderNo={}&bizOrderNo={}", payOrderExtra.getReturnUrl(),payOrder.getOrderNo(),payOrder.getBizOrderNo());
        }
        // 跳转到默认页
        return StrUtil.format("{}/result/success?msg={}", properties.getFrontH5Url(), URLEncodeUtil.encode("支付成功..."));
    }

    public String union(UnionPayReturnParam param) {
        // 获取orderId
        String orderId = param.getOrderId();
        if (StrUtil.isBlank(orderId)){
            orderId = param.getOrderNo();
        }

        PayOrderExtra payOrderExtra = payOrderExtraManager.findById(orderId).orElse(null);
        PayOrder prOrder = payOrderQueryService.findById(Long.valueOf(orderId)).orElse(null);
        if (Objects.isNull(payOrderExtra) || Objects.isNull(prOrder)){
            return StrUtil.format("{}/result/error?msg={}", properties.getFrontH5Url(), URLEncodeUtil.encode("支付订单有问题，请排查"));
        }

        // 如果同步跳转参数为空, 获取系统配置地址, 系统配置如果也为空, 则返回默认地址
        String returnUrl = payOrderExtra.getReturnUrl();
        if (StrUtil.isBlank(returnUrl)){
            returnUrl = platformConfigService.getConfig().getReturnUrl();
        }
        if (StrUtil.isNotBlank(returnUrl)){
            return StrUtil.format("{}?orderNo={}&bizOrderNo={}", payOrderExtra.getReturnUrl(),prOrder.getOrderNo(), prOrder.getBizOrderNo());
        }
        // 跳转到默认页
        return StrUtil.format("{}/result/success?msg={}", properties.getFrontH5Url(), URLEncodeUtil.encode("支付成功..."));
    }
}
