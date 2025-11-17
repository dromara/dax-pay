package org.dromara.daxpay.payment.pay.service.assist;

import cn.bootx.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.payment.pay.strategy.AbsChannelBasicStrategy;
import org.dromara.daxpay.payment.pay.strategy.AbsGatewayPayStrategy;
import org.dromara.daxpay.payment.pay.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.unipay.enums.CashierSceneEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通道基础数据获取
 * @author xxm
 * @since 2025/6/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelBasicService {

    /**
     * 获取所有支付方式
     */
    public List<LabelValue> payMethodList(String channel){
        var basicStrategy = PaymentStrategyFactory.create(channel, AbsChannelBasicStrategy.class);
        return basicStrategy.payMethods().stream()
                .map(payMethodEnum -> new LabelValue(payMethodEnum.getName(), payMethodEnum.getCode()))
                .toList();
    }

    /**
     * 根据通道和场景获取支付方式
     */
    public List<LabelValue> findMethodByScene(String channel, String scene) {
        var cashierSceneEnum = CashierSceneEnum.findByCode(scene);
        var basicStrategy = PaymentStrategyFactory.create(channel, AbsChannelBasicStrategy.class);
        switch (cashierSceneEnum){
            case WECHAT_PAY -> {
                return basicStrategy.wechatPayMethods().stream()
                        .map(payMethodEnum -> new LabelValue(payMethodEnum.getName(), payMethodEnum.getCode()))
                        .toList();
            }
            case ALIPAY -> {
                return basicStrategy.alipayPayMethods().stream()
                        .map(payMethodEnum -> new LabelValue(payMethodEnum.getName(), payMethodEnum.getCode()))
                        .toList();
            }
            case UNION_PAY -> {
                return basicStrategy.unionPayMethods().stream()
                        .map(payMethodEnum -> new LabelValue(payMethodEnum.getName(), payMethodEnum.getCode()))
                        .toList();
            }
        }
        return List.of();
    }

    /**
     * 根据通道和场景获取支付方式(网关支付)
     */
    public List<LabelValue> findGatewayMethodByScene(String channel, String scene) {
        var cashierSceneEnum = CashierSceneEnum.findByCode(scene);
        var gatewayPayStrategy = PaymentStrategyFactory.create(channel, AbsGatewayPayStrategy.class);
        switch (cashierSceneEnum){
            case WECHAT_PAY -> {
                return gatewayPayStrategy.wechatPayMethods().stream()
                        .map(payMethodEnum -> new LabelValue(payMethodEnum.getName(), payMethodEnum.getCode()))
                        .toList();
            }
            case ALIPAY -> {
                return gatewayPayStrategy.alipayPayMethods().stream()
                        .map(payMethodEnum -> new LabelValue(payMethodEnum.getName(), payMethodEnum.getCode()))
                        .toList();
            }
            case UNION_PAY -> {
                return gatewayPayStrategy.unionPayMethods().stream()
                        .map(payMethodEnum -> new LabelValue(payMethodEnum.getName(), payMethodEnum.getCode()))
                        .toList();
            }
        }
        return List.of();
    }
}
