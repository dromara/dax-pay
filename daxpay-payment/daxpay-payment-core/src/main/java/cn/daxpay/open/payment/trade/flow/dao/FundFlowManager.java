package cn.daxpay.open.payment.trade.flow.dao;

import cn.daxpay.open.payment.trade.flow.entity.FundFlow;
import cn.daxpay.open.payment.trade.flow.param.FundFlowQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/// # 资金流水管理器
///
@Repository
public class FundFlowManager extends BaseManager<FundFlowMapper, FundFlow> {

    /// 分页查询
    public Page<FundFlow> page(PageParam pageParam, FundFlowQuery query) {
        Page<FundFlow> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<FundFlow> wrapper = QueryGenerator.generator(query);
        wrapper.lambda().orderByDesc(FundFlow::getId);
        return this.page(mpPage, wrapper);
    }
}
