package cn.daxpay.open.payment.app.admin.service.trade;

import cn.daxpay.open.payment.admin.service.trade.FundFlowAdminService;
import cn.daxpay.open.payment.trade.flow.param.FundFlowQuery;
import cn.daxpay.open.payment.trade.flow.result.FundFlowResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-资金流水服务
///
/// 转发至 [FundFlowAdminService]
@Service
@RequiredArgsConstructor
public class AppAdminFundFlowService {

    private final FundFlowAdminService fundFlowAdminService;

    /// 分页查询
    public PageResult<FundFlowResult> page(PageParam pageParam, FundFlowQuery query) {
        return fundFlowAdminService.page(pageParam, query);
    }

    /// 详情查询
    public FundFlowResult findById(Long id) {
        return fundFlowAdminService.findById(id);
    }
}
