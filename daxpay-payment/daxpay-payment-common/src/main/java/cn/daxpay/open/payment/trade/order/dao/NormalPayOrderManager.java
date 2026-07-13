package cn.daxpay.open.payment.trade.order.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.param.NormalPayOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 普通支付业务单管理器
///
@Repository
public class NormalPayOrderManager extends BaseManager<NormalPayOrderMapper, NormalPayOrder> {

    /// 根据业务单号查询（按商户号自动租户隔离）
    public Optional<NormalPayOrder> findByBizOrderNo(String bizOrderNo) {
        return lambdaQuery()
                .eq(NormalPayOrder::getBizOrderNo, bizOrderNo)
                .oneOpt();
    }

    /// 根据业务单号和应用号查询
    public Optional<NormalPayOrder> findByBizOrderNo(String bizOrderNo, String appId) {
        return lambdaQuery()
                .eq(NormalPayOrder::getBizOrderNo, bizOrderNo)
                .eq(NormalPayOrder::getAppId, appId)
                .oneOpt();
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<NormalPayOrder> page(PageParam pageParam, NormalPayOrderQuery query) {
        Page<NormalPayOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<NormalPayOrder> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }
}
