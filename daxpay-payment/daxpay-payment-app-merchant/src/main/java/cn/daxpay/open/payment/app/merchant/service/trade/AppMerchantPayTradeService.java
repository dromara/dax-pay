package cn.daxpay.open.payment.app.merchant.service.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchPayTradeService;
import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-资金交易凭证服务
///
/// 转发至 [MchPayTradeService]
@Service
@RequiredArgsConstructor
public class AppMerchantPayTradeService {

    private final MchPayTradeService mchPayTradeService;

    /// 分页查询
    public PageResult<PayTradeResult> page(PageParam pageParam, PayTradeQuery query) {
        return mchPayTradeService.page(pageParam, query);
    }

    /// 详情查询
    public PayTradeResult findById(Long id) {
        return mchPayTradeService.findById(id);
    }
}
