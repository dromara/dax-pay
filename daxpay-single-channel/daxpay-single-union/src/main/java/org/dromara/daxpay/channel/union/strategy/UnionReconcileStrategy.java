package org.dromara.daxpay.channel.union.strategy;

import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.union.entity.config.UnionPayConfig;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayKit;
import org.dromara.daxpay.channel.union.service.config.UnionPayConfigService;
import org.dromara.daxpay.channel.union.service.reconcile.UnionReconcileService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.reconcile.ReconcileResolveResultBo;
import org.dromara.daxpay.service.enums.ReconcileFileTypeEnum;
import org.dromara.daxpay.service.strategy.AbsReconcileStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

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
public class UnionReconcileStrategy extends AbsReconcileStrategy {

    private final UnionReconcileService reconcileService;

    private final UnionPayConfigService unionPayConfigService;

    private UnionPayConfig config;

    /**
     * 对账前处理, 主要是初始化支付SDK配置
     */
    @Override
    public void doBeforeHandler() {
        UnionPayConfig config = unionPayConfigService.getAndCheckConfig();
        // 测试环境使用测试号
        if (config.isSandbox()) {
            config.setUnionMachId("700000000000001");
        }
        this.config = config;
    }

    @Override
    public ReconcileResolveResultBo uploadAndResolve(MultipartFile file, ReconcileFileTypeEnum fileType) {
        return null;
    }

    @Override
    public ReconcileResolveResultBo downAndResolve() {
        Date date = DateUtil.date(this.getStatement().getDate());
        UnionPayKit unionPayKit = unionPayConfigService.initPayKit(config);
        return reconcileService.downAndResolve(this.getStatement(), date, unionPayKit);
    }

    @Override
    public String getChannel() {
        return ChannelEnum.UNION_PAY.getCode();
    }
}
