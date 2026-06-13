package org.dromara.daxpay.payment.pay.dao.order.pay;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.annotation.IgnoreTenant;
import org.dromara.daxpay.platform.core.exception.DangerSqlException;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayAllocStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.SettleStatusEnum;
import org.dromara.daxpay.payment.pay.param.order.pay.PayOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/// # 支付订单查询
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayOrderManager extends BaseManager<PayOrderMapper, PayOrder> {

    /// 根据订单号查询
    public Optional<PayOrder> findByOrderNo(String orderNo) {
        return findByField(PayOrder::getOrderNo,orderNo);
    }
    /// 根据订单号查询
    @IgnoreTenant
    public Optional<PayOrder> findByOrderNoNotTenant(String orderNo) {
        return findByField(PayOrder::getOrderNo,orderNo);
    }

    /// 根据订单号查询
    public Optional<PayOrder> findByOrderNo(String orderNo, String appId) {
        return lambdaQuery()
                .eq(PayOrder::getOrderNo, orderNo)
                .eq(PayOrder::getAppId,appId)
                .oneOpt();
    }

    /// 根据商户订单号查询
    public Optional<PayOrder> findByBizOrderNo(String bizOrderNo, String appId) {
        return lambdaQuery()
                .eq(PayOrder::getBizOrderNo,bizOrderNo)
                .eq(PayOrder::getAppId,appId)
                .oneOpt();
    }

    /// 根据特殊通道关联订单号查询
    public Optional<PayOrder> findByRelationOrderNo(String relationOrderNo) {
        return findByField(PayOrder::getRelationOrderNo, relationOrderNo);
    }

    /// 根据通道订单号查询
    public Optional<PayOrder> findByOutOrderNo(String outOrderNo, String appId) {
        return lambdaQuery()
                .eq(PayOrder::getOutOrderNo,outOrderNo)
                .eq(PayOrder::getAppId,appId)
                .oneOpt();
    }

    /// 分页
    public Page<PayOrder> page(PageParam pageParam, PayOrderQuery query){
        Page<PayOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayOrder> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }

    /// 查询对账用订单记录(指定时间和状态的订单)
    public List<PayOrder> findReconcile(String product, OffsetDateTime startTime, OffsetDateTime endTime) {
        return this.lambdaQuery()
                .eq(PayOrder::getProduct, product)
                .between(PayOrder::getPayTime, startTime, endTime)
                .eq(PayOrder::getStatus, PayStatusEnum.SUCCESS.getCode())
                .list();
    }

    /// 查询自动分账的订单记录(指定时间和状态的订单)
    public List<PayOrder> findAutoAllocation() {
        return this.lambdaQuery()
                .eq(PayOrder::getAllocation, true)
                .eq(PayOrder::getAutoAllocation, true)
                .eq(PayOrder::getAllocStatus, PayAllocStatusEnum.WAITING.getCode())
                .eq(PayOrder::getStatus, PayStatusEnum.SUCCESS.getCode())
                .list();
    }

    /// 查询汇总金额
    public BigDecimal getTotalAmount(PayOrderQuery query){
        QueryWrapper<PayOrder> generator = QueryGenerator.generator(query);
        // 商户和应用AppId
        generator.eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS.getCode());
        return baseMapper.getTotalAmount(generator);
    }

    /// 查询当前超时的未支付订单
    @IgnoreTenant
    public List<PayOrder> queryExpiredOrderNotTenant() {
        return lambdaQuery()
                .in(PayOrder::getStatus, PayStatusEnum.PROGRESS.getCode(),PayStatusEnum.WAIT.getCode())
                .lt(PayOrder::getExpiredTime, OffsetDateTime.now(ZoneOffset.UTC))
                .list();
    }

    /// 查询指定时间的的未支付订单
    @IgnoreTenant
    public List<PayOrder> queryExpiredOrderNotTenant(OffsetDateTime start, OffsetDateTime end) {
        return lambdaQuery()
                .in(PayOrder::getStatus, PayStatusEnum.PROGRESS.getCode(),PayStatusEnum.WAIT.getCode())
                .between(PayOrder::getExpiredTime, start,end)
                .list();
    }

    /// 查询支付完成未结算的订单
    @IgnoreTenant
    public List<PayOrder> findAllBySettleAndBeforeNotTenant(OffsetDateTime dateTime) {
        return lambdaQuery()
                .eq(PayOrder::getSettleStatus, SettleStatusEnum.NOT_SETTLE.getCode())
                .eq(PayOrder::getStatus, PayStatusEnum.SUCCESS.getCode())
                .le(PayOrder::getPayTime, dateTime)
                .list();
    }

    /// 查询订单, 不过滤租户
    @IgnoreTenant
    public Optional<PayOrder> findByIdNotTenant(Long id) {
        return this.findById(id);
    }

    /// 根据id进行更新
    @Override
    public int updateById(PayOrder payOrder) {
        int i = super.updateById(payOrder);
        if (i<1){
            // 更新支付订单失败
            throw new DangerSqlException(CommonCode.DANGER_SQL, "pay.error.pay.closeFailed", payOrder.getOrderNo());
        }
        return i;
    }
}
