package org.dromara.daxpay.payment.old.pay.dao.order.refund;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpCreateEntity;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.annotation.IgnoreTenant;
import org.dromara.daxpay.platform.core.exception.DangerSqlException;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.enums.pay.refund.RefundStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.SettleStatusEnum;
import org.dromara.daxpay.payment.old.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.payment.old.pay.param.order.refund.RefundOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


@Slf4j
@Repository
@RequiredArgsConstructor
public class RefundOrderManager extends BaseManager<RefundOrderMapper, RefundOrder> {

    /// 分页
    public Page<RefundOrder> page(PageParam pageParam, RefundOrderQuery query) {
        Page<RefundOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<RefundOrder> generator = QueryGenerator.generator(query);
        return page(mpPage,generator);
    }

    /// 根据退款号查询
    public Optional<RefundOrder> findByRefundNo(String refundNo) {
        return findByField(RefundOrder::getRefundNo, refundNo);
    }

    /// 根据关联退款号查询
    public Optional<RefundOrder> findByRelationOrderNo(String relationOrderNo) {
        return findByField(RefundOrder::getRelationOrderNo, relationOrderNo);
    }

    /// 根据商户退款号查询
    public Optional<RefundOrder> findByBizRefundNo(String bizRefundNo, String appId) {
        return lambdaQuery()
                .eq(RefundOrder::getBizRefundNo,bizRefundNo)
                .eq(RefundOrder::getAppId,appId)
                .oneOpt();
    }

    /// 查询支付号是否重复
    public boolean existsByRefundNo(String refundNo){
        return this.existedByField(RefundOrder::getRefundNo,refundNo);
    }

    /// 查询指定时间的退款中的订单
    @IgnoreTenant
    public List<RefundOrder> findAllByProgress(OffsetDateTime startTime, OffsetDateTime endTime) {
        return lambdaQuery()
                .eq(RefundOrder::getStatus, RefundStatusEnum.PROGRESS.getCode())
                .between(MpCreateEntity::getCreateTime, startTime,  endTime)
                .list();
    }

    /// 查询支付完成未结算的订单
    @IgnoreTenant
    public List<RefundOrder> findAllBySettleAndBeforeNotTenant(OffsetDateTime dateTime) {
        return lambdaQuery()
                .eq(RefundOrder::getSettleStatus, SettleStatusEnum.NOT_SETTLE.getCode())
                .eq(RefundOrder::getStatus, RefundStatusEnum.SUCCESS.getCode())
                .le(RefundOrder::getFinishTime, dateTime)
                .list();
    }


    /// 查询汇总金额
    public BigDecimal getTotalAmount(RefundOrderQuery query){
        QueryWrapper<RefundOrder> generator = QueryGenerator.generator(query);
        generator.eq(MpUtil.getColumnName(RefundOrder::getStatus), RefundStatusEnum.SUCCESS.getCode());
        return baseMapper.getTotalAmount(generator);
    }

    /// 查询订单, 不过滤租户
    @IgnoreTenant
    public Optional<RefundOrder> findByIdNotTenant(Long id) {
        return findById(id);
    }

    /// 根据id进行更新
    @Override
    public int updateById(RefundOrder refundOrder) {
        int i = super.updateById(refundOrder);
        if (i<1){
            // 更新退款订单失败
            throw new DangerSqlException(CommonCode.DANGER_SQL, "pay.error.refund.syncProcessing", refundOrder.getRefundNo());
        }
        return i;
    }
}
