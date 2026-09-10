package cn.daxpay.open.payment.app.merchant.service.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchFundFlowService;
import cn.daxpay.open.payment.trade.flow.param.FundFlowQuery;
import cn.daxpay.open.payment.trade.flow.result.FundFlowResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-资金流水服务
///
/// 转发至 [MchFundFlowService]
@Service
@RequiredArgsConstructor
public class AppMerchantFundFlowService {

    private final MchFundFlowService mchFundFlowService;

    /// 分页查询
    public PageResult<FundFlowResult> page(PageParam pageParam, FundFlowQuery query) {
        return mchFundFlowService.page(pageParam, query);
    }

    /// 详情查询
    public FundFlowResult findById(Long id) {
        return mchFundFlowService.findById(id);
    }
}
