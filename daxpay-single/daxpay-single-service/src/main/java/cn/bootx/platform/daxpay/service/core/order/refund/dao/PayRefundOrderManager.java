package cn.bootx.platform.daxpay.service.core.order.refund.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.param.order.PayRefundOrderQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @author xxm
 * @since 2022/3/2
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayRefundOrderManager extends BaseManager<PayRefundOrderMapper, PayRefundOrder> {

    /**
     * 分页
     */
    public Page<PayRefundOrder> page(PageParam pageParam, PayRefundOrderQuery query) {
        Page<PayRefundOrder> mpPage = MpUtil.getMpPage(pageParam, PayRefundOrder.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(Objects.nonNull(query.getPaymentId()), PayRefundOrder::getPaymentId, query.getPaymentId())
            .like(Objects.nonNull(query.getBusinessNo()), PayRefundOrder::getBusinessNo, query.getBusinessNo())
            .like(Objects.nonNull(query.getTitle()), PayRefundOrder::getTitle, query.getTitle())
            .page(mpPage);
    }

}
