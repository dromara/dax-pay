package org.dromara.daxpay.channel.union.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.reconcile.ReconcileResolveResultBo;
import org.dromara.daxpay.service.enums.ReconcileFileTypeEnum;
import org.dromara.daxpay.service.strategy.AbsReconcileStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 银联对账
 * @author xxm
 * @since 2024/3/8
 */
@Slf4j
@Service
@Scope(SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UnionPayReconcileStrategy extends AbsReconcileStrategy {

    @Override
    public ReconcileResolveResultBo uploadAndResolve(MultipartFile file, ReconcileFileTypeEnum fileType) {
        return null;
    }

    @Override
    public ReconcileResolveResultBo downAndResolve() {
        return null;
    }

    @Override
    public String getChannel() {
        return ChannelEnum.UNION_PAY.getCode();
    }
}
