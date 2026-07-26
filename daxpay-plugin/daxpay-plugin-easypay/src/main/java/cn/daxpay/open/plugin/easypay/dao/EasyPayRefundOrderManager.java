package cn.daxpay.open.plugin.easypay.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.plugin.easypay.entity.EasyPayRefundOrder;
import cn.daxpay.open.plugin.easypay.param.order.EasyPayRefundOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 易支付协议退款订单 Manager
///
@Repository
public class EasyPayRefundOrderManager extends BaseManager<EasyPayRefundOrderMapper, EasyPayRefundOrder> {

    /// 按内核退款单 ID 查询
    public Optional<EasyPayRefundOrder> findByRefundId(Long refundId) {
        return findByField(EasyPayRefundOrder::getRefundId, refundId);
    }

    /// 按平台退款单号查询
    public Optional<EasyPayRefundOrder> findByRefundNo(String refundNo) {
        return findByField(EasyPayRefundOrder::getRefundNo, refundNo);
    }

    /// 忽略租户按内核退款单 ID 查询（插件钩子无租户上下文场景）
    @IgnoreTenant
    public Optional<EasyPayRefundOrder> findByRefundIdNotTenant(Long refundId) {
        return findByField(EasyPayRefundOrder::getRefundId, refundId);
    }

    /// 忽略租户按主键查询
    @IgnoreTenant
    public Optional<EasyPayRefundOrder> findByIdNotTenant(Long id) {
        return findById(id);
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<EasyPayRefundOrder> page(PageParam pageParam, EasyPayRefundOrderQuery query) {
        Page<EasyPayRefundOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<EasyPayRefundOrder> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }
}
