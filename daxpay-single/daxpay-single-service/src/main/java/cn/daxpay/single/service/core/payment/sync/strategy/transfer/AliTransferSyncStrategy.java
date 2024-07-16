package cn.daxpay.single.service.core.payment.sync.strategy.transfer;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.daxpay.single.service.core.payment.sync.result.RefundRemoteSyncResult;
import cn.daxpay.single.service.func.AbsTransferSyncStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 转账同步接口
 * @author xxm
 * @since 2024/6/17
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliTransferSyncStrategy extends AbsTransferSyncStrategy {


    @Override
    public String getChannel() {
        return PayChannelEnum.ALI.getCode();
    }

    @Override
    public RefundRemoteSyncResult doSyncStatus() {
        return null;
    }

}
