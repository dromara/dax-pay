package cn.daxpay.open.payment.trade.abnormal.dao;

import cn.daxpay.open.payment.trade.abnormal.entity.AbnormalOrder;
import cn.daxpay.open.payment.trade.abnormal.enums.AbnormalHandleStatusEnum;
import cn.daxpay.open.payment.trade.abnormal.param.AbnormalOrderQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 异常订单管理器
///
@Repository
public class AbnormalOrderManager extends BaseManager<AbnormalOrderMapper, AbnormalOrder> {

    /// 分页查询
    public Page<AbnormalOrder> page(PageParam pageParam, AbnormalOrderQuery query) {
        Page<AbnormalOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<AbnormalOrder> wrapper = QueryGenerator.generator(query);
        wrapper.lambda().orderByDesc(AbnormalOrder::getId);
        return this.page(mpPage, wrapper);
    }

    /// 查交易当前的待处理异常单(部分唯一索引保证至多一条)
    public Optional<AbnormalOrder> findPendingByTradeNo(String tradeNo) {
        return lambdaQuery()
                .eq(AbnormalOrder::getTradeNo, tradeNo)
                .eq(AbnormalOrder::getHandleStatus, AbnormalHandleStatusEnum.PENDING.getCode())
                .oneOpt();
    }
}
