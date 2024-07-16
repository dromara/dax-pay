package cn.daxpay.single.service.core.payment.sync.strategy.transfer;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.core.payment.sync.result.RefundRemoteSyncResult;
import cn.daxpay.single.service.func.AbsTransferSyncStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信转账同步策略
 * @author xxm
 * @since 2024/7/16
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WeChatTransferSyncService extends AbsTransferSyncStrategy {
    @Override
    public String getChannel() {
        return PayChannelEnum.WECHAT.getCode();
    }

    @Override
    public RefundRemoteSyncResult doSyncStatus() {
        return null;
    }

}
