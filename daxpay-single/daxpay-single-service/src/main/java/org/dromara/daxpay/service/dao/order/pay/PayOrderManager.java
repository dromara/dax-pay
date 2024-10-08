package org.dromara.daxpay.service.dao.order.pay;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.core.enums.PayAllocStatusEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.param.order.pay.PayOrderQuery;
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
public class PayOrderManager extends BaseManager<PayOrderMapper, PayOrder> {

    /**
     * 根据订单号查询
     */
    public Optional<PayOrder> findByOrderNo(String orderNo) {
        return findByField(PayOrder::getOrderNo,orderNo);
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
     * 分页
     */
    public Page<PayOrder> page(PageParam pageParam, PayOrderQuery query){
        Page<PayOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayOrder> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }

    /**
     * 查询对账用订单记录(指定时间和状态的订单)
     */
    public List<PayOrder> findReconcile(String channel, LocalDateTime startTime, LocalDateTime endTime) {
        return this.lambdaQuery()
                .eq(PayOrder::getChannel, channel)
                .between(PayOrder::getPayTime, startTime, endTime)
                .eq(PayOrder::getStatus, PayStatusEnum.SUCCESS.getCode())
                .list();
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
        // 商户应用AppId
        generator.eq(MpUtil.getColumnName(PayOrder::getStatus), PayStatusEnum.SUCCESS.getCode());
        return baseMapper.getTotalAmount(generator);
    }

    /**
     * 查询当前超时的未支付订单
     */
    @IgnoreTenant
    public List<PayOrder> queryExpiredOrderNotTenant() {
        return lambdaQuery()
                .eq(PayOrder::getStatus, PayStatusEnum.PROGRESS.getCode())
                .lt(PayOrder::getExpiredTime, LocalDateTime.now())
                .list();
    }

}
