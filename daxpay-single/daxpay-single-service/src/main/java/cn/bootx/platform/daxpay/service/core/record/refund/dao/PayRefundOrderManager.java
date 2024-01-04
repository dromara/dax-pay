package cn.bootx.platform.daxpay.service.core.record.refund.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.record.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.dto.record.refund.PayRefundOrderDto;
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

    public Page<PayRefundOrder> page(PageParam pageParam, PayRefundOrderDto param) {
        Page<PayRefundOrder> mpPage = MpUtil.getMpPage(pageParam, PayRefundOrder.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(Objects.nonNull(param.getPaymentId()), PayRefundOrder::getPaymentId, param.getPaymentId())
            .like(Objects.nonNull(param.getBusinessNo()), PayRefundOrder::getBusinessNo, param.getBusinessNo())
            .like(Objects.nonNull(param.getTitle()), PayRefundOrder::getTitle, param.getTitle())
            .page(mpPage);
    }

}
