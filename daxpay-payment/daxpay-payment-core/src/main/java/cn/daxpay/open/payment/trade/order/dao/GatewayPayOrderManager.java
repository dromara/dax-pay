package cn.daxpay.open.payment.trade.order.dao;

import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.param.GatewayPayOrderQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/// # 网关支付业务单管理器
@Repository
public class GatewayPayOrderManager extends BaseManager<GatewayPayOrderMapper, GatewayPayOrder> {

    /// 按平台网关单号查询
    public Optional<GatewayPayOrder> findByOrderNo(String orderNo) {
        return lambdaQuery()
                .eq(GatewayPayOrder::getOrderNo, orderNo)
                .oneOpt();
    }

    /// 按平台网关单号查询(忽略租户, H5 落地页用)
    @IgnoreTenant
    public Optional<GatewayPayOrder> findByOrderNoNotTenant(String orderNo) {
        return lambdaQuery()
                .eq(GatewayPayOrder::getOrderNo, orderNo)
                .oneOpt();
    }

    /// 按商户业务单号查询（按商户号自动租户隔离）
    public Optional<GatewayPayOrder> findByBizOrderNo(String bizOrderNo) {
        return lambdaQuery()
                .eq(GatewayPayOrder::getBizOrderNo, bizOrderNo)
                .oneOpt();
    }

    /// 按商户业务单号+商户号查询（显式商户维度, 运营端主路径: 运营端忽略租户, 须显式条件防跨商户同单号串单;
    /// 商户端租户已自动隔离, 双条件等值冗余无害）
    public Optional<GatewayPayOrder> findByBizOrderNoAndMch(String bizOrderNo, String mchNo) {
        return lambdaQuery()
                .eq(GatewayPayOrder::getBizOrderNo, bizOrderNo)
                .eq(GatewayPayOrder::getMchNo, mchNo)
                .oneOpt();
    }

    /// 分页查询
    public Page<GatewayPayOrder> page(PageParam pageParam, GatewayPayOrderQuery query) {
        Page<GatewayPayOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<GatewayPayOrder> wrapper = QueryGenerator.generator(query);
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// 已过期仍待支付/支付中的网关单(超时兜底)
    @IgnoreTenant
    public List<GatewayPayOrder> findTimeoutOrders(OffsetDateTime now) {
        return listLimit(500, q -> q
                .in(GatewayPayOrder::getStatus,
                        GatewayOrderStatusEnum.WAIT_PAY.getCode(),
                        GatewayOrderStatusEnum.PAYING.getCode())
                .lt(GatewayPayOrder::getExpiredTime, now)
                .orderByAsc(GatewayPayOrder::getExpiredTime));
    }
}
