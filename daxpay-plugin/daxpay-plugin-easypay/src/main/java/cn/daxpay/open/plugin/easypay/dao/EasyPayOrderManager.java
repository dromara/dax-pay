package cn.daxpay.open.plugin.easypay.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.param.order.EasyPayOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 易支付协议订单 Manager
///
@Repository
@RequiredArgsConstructor
public class EasyPayOrderManager extends BaseManager<EasyPayOrderMapper, EasyPayOrder> {

    /// 按商户订单号查询
    public Optional<EasyPayOrder> findByOutTradeNo(String outTradeNo) {
        return findByField(EasyPayOrder::getOutTradeNo, outTradeNo);
    }

    /// 按平台业务单号查询
    public Optional<EasyPayOrder> findByTradeNo(String tradeNo) {
        return findByField(EasyPayOrder::getTradeNo, tradeNo);
    }

    /// 按内核容器 ID 查询
    public Optional<EasyPayOrder> findByOrderId(Long orderId) {
        return findByField(EasyPayOrder::getOrderId, orderId);
    }

    /// 忽略租户按主键查询
    @IgnoreTenant
    public Optional<EasyPayOrder> findByIdNotTenant(Long id) {
        return findById(id);
    }

    /// 忽略租户按内核容器 ID 查询
    @IgnoreTenant
    public Optional<EasyPayOrder> findByOrderIdNotTenant(Long orderId) {
        return findByField(EasyPayOrder::getOrderId, orderId);
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<EasyPayOrder> page(PageParam pageParam, EasyPayOrderQuery query) {
        Page<EasyPayOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<EasyPayOrder> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }
}
