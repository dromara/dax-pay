package org.dromara.daxpay.service.service.cashier;

import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.CheckoutCallTypeEnum;
import org.dromara.daxpay.core.enums.CheckoutTypeEnum;
import org.dromara.daxpay.core.exception.TradeProcessingException;
import org.dromara.daxpay.core.exception.UnsupportedAbilityException;
import org.dromara.daxpay.core.param.cashier.CheckoutParam;
import org.dromara.daxpay.core.param.cashier.CheckoutPayParam;
import org.dromara.daxpay.core.result.checkout.CheckoutOrderAndConfigResult;
import org.dromara.daxpay.core.result.checkout.CheckoutUrlResult;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutItemConfigManager;
import org.dromara.daxpay.service.entity.config.PlatformConfig;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.config.PlatformConfigService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 收银台服务
 * @author xxm
 * @since 2024/11/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CheckoutAssistService checkoutAssistService;
    private final LockTemplate lockTemplate;
    private final PlatformConfigService platformConfigService;
    private final PaymentAssistService paymentAssistService;
    private final CheckoutQueryService checkoutQueryService;
    private final CheckoutItemConfigManager checkoutItemConfigManager;

    /**
     * 生成收银台链接
     */
    public CheckoutUrlResult creat(CheckoutParam checkoutParam){
        // 校验支付限额
        checkoutAssistService.validationLimitAmount(checkoutParam);
        // 校验超时时间, 不可早于当前
        checkoutAssistService.validationExpiredTime(checkoutParam);
        // 获取商户订单号
        String bizOrderNo = checkoutParam.getBizOrderNo();
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:pay:" + bizOrderNo,10000,200);
        if (Objects.isNull(lock)){
            log.warn("正在发起调起收银台中，请勿重复操作");
            throw new TradeProcessingException("正在发起调起收银台中，请勿重复操作");
        }
        try {
            // 查询并检查订单
            PayOrder payOrder = checkoutAssistService.getOrderAndCheck(checkoutParam);
            // 订单已经存在直接返回链接, 不存在创建订单后返回链接
            if (Objects.isNull(payOrder)){
                // 执行支付前的保存动作, 保存支付订单和扩展记录
                payOrder = checkoutAssistService.createPayOrder(checkoutParam);
                String checkoutUrl = this.getCheckoutUrl(payOrder.getOrderNo(), checkoutParam.getCheckoutType());
                return new CheckoutUrlResult().setUrl(checkoutUrl);
            } else {
                // 直接返回收银台链接
                String checkoutUrl = this.getCheckoutUrl(payOrder.getOrderNo(), checkoutParam.getCheckoutType());
                return new CheckoutUrlResult().setUrl(checkoutUrl);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 获取收银台链接
     */
    public String getCheckoutUrl(String orderNo, String checkoutType){
        CheckoutTypeEnum checkoutTypeEnum = CheckoutTypeEnum.findBuyCode(checkoutType);

        PlatformConfig config = platformConfigService.getConfig();

        switch (checkoutTypeEnum) {
            case H5 -> {
                return StrUtil.format("{}/checkout/{}",config.getGatewayMobileUrl(), orderNo);
            }
            case PC -> {
                return StrUtil.format("{}/checkout/{}",config.getGatewayPcUrl(), orderNo);
            }
            case AGGREGATE -> {
                return StrUtil.format("{}/aggregate/{}",config.getGatewayPcUrl(), orderNo);
            }
            case MINI_APP -> throw new UnsupportedAbilityException("暂不支持小程序收银台");
            default -> throw new UnsupportedAbilityException("不支持的收银台类型");
        }
    }

    /**
     * 支付调用
     */
    public void pay(CheckoutPayParam param){
        // 订单信息
        PayOrder order = checkoutAssistService.getOrderAndCheck(param.getOrderNo());
        paymentAssistService.initMchApp(order.getAppId());
        // 获取配置项
        var itemConfig = checkoutItemConfigManager.findByIdAndAppId(param.getItemId(),order.getAppId())
                .orElseThrow(() -> new TradeProcessingException("支付配置项不存在"));
        // 判断支付调用类型
        CheckoutCallTypeEnum callTypeEnum = CheckoutCallTypeEnum.findBuyCode(itemConfig.getCallType());
        switch (callTypeEnum) {
            case SCAN -> {
                // 根据通道和支付方式返回扫码链接
            }
            case BAR_CODE -> {
                // 调用支付逻辑直接进行支付
            }
            case LINK -> {
                // 返回支付链接
            }
            case AGGREGATE -> {
                // 直接返回手机端的聚合收银台链接， 如何判断
            }
            default -> throw new UnsupportedAbilityException("不支持的支付调用类型");
        }
    }

}
