package org.dromara.daxpay.service.pay.dao.order.refund;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.exception.DangerSqlException;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.service.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.pay.param.order.refund.RefundOrderQuery;
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
     * 查询一分钟之前的退款中的订单
     */
    @IgnoreTenant
    public List<RefundOrder> findAllByBeforeProgress(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        return lambdaQuery()
                .eq(RefundOrder::getStatus, RefundStatusEnum.PROGRESS.getCode())
                .le(MpCreateEntity::getCreateTime, dateTime)
                .list();
    }
    /**
     * 查询指定时间的退款中的订单
     */
    @IgnoreTenant
    public List<RefundOrder> findAllByProgress(LocalDateTime startTime, LocalDateTime endTime) {
        return lambdaQuery()
                .eq(RefundOrder::getStatus, RefundStatusEnum.PROGRESS.getCode())
                .between(MpCreateEntity::getCreateTime, startTime,  endTime)
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

    /**
     * 查询订单, 不过滤租户
     */
    @IgnoreTenant
    public Optional<RefundOrder> findByIdNotTenant(Long id) {
        return findById(id);
    }

    /**
     * 根据id进行更新
     *
     */
    @Override
    public int updateById(RefundOrder refundOrder) {
        int i = super.updateById(refundOrder);
        if (i<1){
            throw new DangerSqlException("更新退款订单失败");
        }
        return i;
    }
}
