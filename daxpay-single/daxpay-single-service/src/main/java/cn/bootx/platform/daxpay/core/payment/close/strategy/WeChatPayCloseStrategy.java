package cn.bootx.platform.daxpay.core.payment.close.strategy;

import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPayCloseService;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPayConfigService;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPayOrderService;
import cn.bootx.platform.daxpay.func.AbsPayCloseStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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
public class WeChatPayCloseStrategy extends AbsPayCloseStrategy {

    private final WeChatPayConfigService weChatPayConfigService;
    private final WeChatPayOrderService weChatPayOrderService;
    private final WeChatPayCloseService weChatPayCloseService;

    private WeChatPayConfig weChatPayConfig;
    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCloseHandler() {
        this.weChatPayConfig = weChatPayConfigService.getConfig();
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCloseHandler() {
        weChatPayCloseService.close(this.getOrder(), weChatPayConfig);
        // 调用关闭本地支付记录
        weChatPayOrderService.updateClose(this.getOrder().getId());
    }
}
