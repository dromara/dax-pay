package org.dromara.daxpay.service.dao.order.refund;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.param.order.refund.RefundOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/6/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RefundOrderManager extends BaseManager<RefundOrderMapper, RefundOrder> {

    /**
     * 分页
     */
    public Page<RefundOrder> page(PageParam pageParam, RefundOrderQuery query) {
        Page<RefundOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<RefundOrder> generator = QueryGenerator.generator(query);
        return page(mpPage,generator);
    }

    /**
     * 根据退款号查询
     */
    public Optional<RefundOrder> findByRefundNo(String refundNo) {
        return findByField(RefundOrder::getRefundNo, refundNo);
    }


    /**
     * 根据商户退款号查询
     */
    public Optional<RefundOrder> findByBizRefundNo(String bizRefundNo, String appId) {
        return lambdaQuery()
                .eq(RefundOrder::getBizRefundNo,bizRefundNo)
                .eq(RefundOrder::getAppId,appId)
                .oneOpt();
    }

    /**
     * 查询支付号是否重复
     */
    public boolean existsByRefundNo(String refundNo){
        return this.existedByField(RefundOrder::getRefundNo,refundNo);
    }

    /**
     * 查询一分钟之前的退款中的支付订单
     */
    @IgnoreTenant
    public List<RefundOrder> findAllByProgress() {
        LocalDateTime now = LocalDateTime.now();
        return lambdaQuery()
                .eq(RefundOrder::getStatus, RefundStatusEnum.PROGRESS.getCode())
                .le(MpCreateEntity::getCreateTime, now.plusMinutes(-1L))
                .list();
    }

    /**
     * 查询对账用订单记录(指定时间和状态的订单)
     */
    public List<RefundOrder> findSuccessReconcile(String channel, LocalDateTime startTime, LocalDateTime endTime) {
        return this.lambdaQuery()
                .eq(RefundOrder::getChannel, channel)
                .between(RefundOrder::getFinishTime, startTime, endTime)
                .eq(RefundOrder::getStatus, RefundStatusEnum.SUCCESS)
                .list();
    }

    /**
     * 查询汇总金额
     */
    public BigDecimal getTotalAmount(RefundOrderQuery query){
        QueryWrapper<RefundOrder> generator = QueryGenerator.generator(query);
        generator.eq(MpUtil.getColumnName(RefundOrder::getStatus), RefundStatusEnum.SUCCESS.getCode());
        return baseMapper.getTotalAmount(generator);
    }
}
