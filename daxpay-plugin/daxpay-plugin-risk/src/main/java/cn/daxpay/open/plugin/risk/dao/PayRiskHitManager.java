package cn.daxpay.open.plugin.risk.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.plugin.risk.entity.PayRiskHit;
import cn.daxpay.open.plugin.risk.param.PayRiskHitQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/// # 风险命中 Manager
///
@Repository
public class PayRiskHitManager extends BaseManager<PayRiskHitMapper, PayRiskHit> {

    /// 分页
    public Page<PayRiskHit> page(PageParam pageParam, PayRiskHitQuery query) {
        Page<PayRiskHit> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayRiskHit> wrapper = QueryGenerator.generator(query);
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }
}
