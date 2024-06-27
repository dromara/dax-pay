package cn.daxpay.single.service.core.payment.sync.strategy.transfer;

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
public class AliTransferSyncStrategy {
}
