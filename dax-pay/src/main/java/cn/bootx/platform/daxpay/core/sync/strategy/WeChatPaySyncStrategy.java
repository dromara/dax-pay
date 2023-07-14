package cn.bootx.platform.daxpay.core.sync.strategy;

import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPayConfigManager;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPaySyncService;
import cn.bootx.platform.daxpay.core.sync.func.AbsPaySyncStrategy;
import cn.bootx.platform.daxpay.core.sync.result.PaySyncResult;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付同步
 * @author xxm
 * @since 2023/7/14
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WeChatPaySyncStrategy extends AbsPaySyncStrategy {

    private final WeChatPayConfigManager weChatPayConfigManager;

    private final WeChatPaySyncService weChatPaySyncService;

    private WeChatPayConfig weChatPayConfig;

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public PaySyncResult doSyncPayStatusHandler() {
        // 检查并获取微信支付配置
        this.initWeChatPayConfig(this.getPayment().getMchAppCode());
        return weChatPaySyncService.syncPayStatus(this.getPayment().getId(), this.weChatPayConfig);
    }

    /**
     * 初始化微信支付
     */
    private void initWeChatPayConfig(String appCode) {
        // 检查并获取微信支付配置
        this.weChatPayConfig = weChatPayConfigManager.findByMchAppCode(appCode)
                .orElseThrow(() -> new PayFailureException("支付配置不存在"));
    }
}
