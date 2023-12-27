package cn.bootx.platform.daxpay.core.order.sync.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.order.sync.entity.PaySyncOrder;
import cn.bootx.platform.daxpay.dto.order.sync.PaySyncOrderDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 *
 * @author xxm
 * @since 2023/7/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PaySyncOrderManager extends BaseManager<PaySyncOrderMapper, PaySyncOrder> {

    public Page<PaySyncOrder> page(PageParam pageParam, PaySyncOrderDto param) {
        Page<PaySyncOrder> mpPage = MpUtil.getMpPage(pageParam, PaySyncOrder.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
                .like(Objects.nonNull(param.getPaymentId()), PaySyncOrder::getPaymentId, param.getPaymentId())
                .eq(Objects.nonNull(param.getChannel()), PaySyncOrder::getChannel, param.getChannel())
                .eq(Objects.nonNull(param.getStatus()), PaySyncOrder::getStatus, param.getStatus())
                .page(mpPage);
    }

}
