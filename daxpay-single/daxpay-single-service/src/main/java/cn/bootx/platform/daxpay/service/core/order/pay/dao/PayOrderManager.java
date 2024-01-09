package cn.bootx.platform.daxpay.service.core.order.pay.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.param.order.PayOrderQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付订单
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayOrderManager extends BaseManager<PayOrderMapper, PayOrder> {
    public Optional<PayOrder> findByBusinessNo(String businessNo) {
        return findByField(PayOrder::getBusinessNo,businessNo);
    }

    /**
     * 分页
     */
    public Page<PayOrder> page(PageParam pageParam, PayOrderQuery query){
        Page<PayOrder> mpPage = MpUtil.getMpPage(pageParam, PayOrder.class);
        return lambdaQuery()
                .orderByDesc(MpIdEntity::getId)
                .page(mpPage);
    }
}
