package cn.daxpay.single.service.core.payment.cancel.strategy;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.PayMethodEnum;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayCloseService;
import cn.daxpay.single.service.core.channel.wechat.service.WeChatPayConfigService;
import cn.daxpay.single.service.func.AbsPayCancelStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付关闭策略
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WeChatPayCancelStrategy extends AbsPayCancelStrategy {

    private final WeChatPayConfigService weChatPayConfigService;
    private final WeChatPayCloseService weChatPayCloseService;

    private WeChatPayConfig weChatPayConfig;

    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCancelHandler() {
        // 非当面付订单不可以关闭
        if (!Objects.equals(this.getOrder().getMethod(), PayMethodEnum.BARCODE.getCode())) {
            throw new RuntimeException("微信当面付订单才可以被撤销");
        }
        this.weChatPayConfig = weChatPayConfigService.getConfig();
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCancelHandler() {
        weChatPayCloseService.cancel(this.getOrder(), weChatPayConfig);
    }
}
