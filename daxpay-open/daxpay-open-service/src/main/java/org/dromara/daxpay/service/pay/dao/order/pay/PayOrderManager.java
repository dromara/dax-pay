package org.dromara.daxpay.service.pay.dao.order.pay;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.exception.DangerSqlException;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.core.enums.PayAllocStatusEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.pay.param.order.pay.PayOrderQuery;
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
 * 支付订单查询
 * @author xxm
 * @since 2024/6/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayOrderManager extends BaseManager<PayOrderMapper, PayOrder> {

    /**
     * 根据订单号查询
     */
    public Optional<PayOrder> findByOrderNo(String orderNo) {
        return findByField(PayOrder::getOrderNo,orderNo);
    }

    /**
     * 根据订单号查询
     */
    public Optional<PayOrder> findByOrderNo(String orderNo, String appId) {
        return lambdaQuery()
                .eq(PayOrder::getOrderNo, orderNo)
                .eq(PayOrder::getAppId,appId)
                .oneOpt();
    }

    /**
     * 根据商户订单号查询
     */
    public Optional<PayOrder> findByBizOrderNo(String bizOrderNo, String appId) {
        return lambdaQuery()
                .eq(PayOrder::getBizOrderNo,bizOrderNo)
                .eq(PayOrder::getAppId,appId)
                .oneOpt();
    }

    /**
     * 根据通道订单号查询
     */
    public Optional<PayOrder> findByOutOrderNo(String outOrderNo) {
        return findByField(PayOrder::getOutOrderNo,outOrderNo);
    }

    /**
     * 根据通道订单号查询
     */
    public Optional<PayOrder> findByOutOrderNo(String outOrderNo, String appId) {
        return lambdaQuery()
                .eq(PayOrder::getOutOrderNo,outOrderNo)
                .eq(PayOrder::getAppId,appId)
                .oneOpt();
    }

    /**
     * 分页
     */
    public Page<PayOrder> page(PageParam pageParam, PayOrderQuery query){
        Page<PayOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayOrder> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }

    /**
     * 查询自动分账的订单记录(指定时间和状态的订单)
     */
    public List<PayOrder> findAutoAllocation() {
        return this.lambdaQuery()
                .eq(PayOrder::getAllocation, true)
                .eq(PayOrder::getAutoAllocation, true)
                .eq(PayOrder::getAllocStatus, PayAllocStatusEnum.WAITING.getCode())
                .eq(PayOrder::getStatus, PayStatusEnum.SUCCESS.getCode())
                .list();
    }

    /**
     * 查询汇总金额
     */
    public BigDecimal getTotalAmount(PayOrderQuery query){
        QueryWrapper<PayOrder> generator = QueryGenerator.generator(query);
        // 商户和应用AppId
        generator.eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS.getCode());
        return baseMapper.getTotalAmount(generator);
    }

    /**
     * 查询当前超时的未支付订单
     */
    @IgnoreTenant
    public List<PayOrder> queryExpiredOrderNotTenant() {
        return lambdaQuery()
                .in(PayOrder::getStatus, PayStatusEnum.PROGRESS.getCode(),PayStatusEnum.WAIT.getCode())
                .lt(PayOrder::getExpiredTime, LocalDateTime.now())
                .list();
    }

    /**
     * 查询指定时间的的未支付订单
     */
    @IgnoreTenant
    public List<PayOrder> queryExpiredOrderNotTenant(LocalDateTime start, LocalDateTime end) {
        return lambdaQuery()
                .in(PayOrder::getStatus, PayStatusEnum.PROGRESS.getCode(),PayStatusEnum.WAIT.getCode())
                .between(PayOrder::getExpiredTime, start,end)
                .list();
    }

    /**
     * 查询订单, 不过滤租户
     */
    @IgnoreTenant
    public Optional<PayOrder> findByIdNotTenant(Long id) {
        return this.findById(id);
    }

    /**
     * 根据id进行更新
     */
    @Override
    public int updateById(PayOrder payOrder) {
        int i = super.updateById(payOrder);
        if (i<1){
            throw new DangerSqlException("更新支付订单失败");
        }
        return i;
    }
}
