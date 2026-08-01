package cn.daxpay.open.payment.app.admin.service.trade;

import cn.daxpay.open.payment.admin.service.trade.PayTradeAdminService;
import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-资金交易凭证服务
///
/// 转发至 [PayTradeAdminService]
@Service
@RequiredArgsConstructor
public class AppAdminPayTradeService {

    private final PayTradeAdminService payTradeAdminService;

    /// 分页查询
    public PageResult<PayTradeResult> page(PageParam pageParam, PayTradeQuery query) {
        return payTradeAdminService.page(pageParam, query);
    }

    /// 详情查询
    public PayTradeResult findById(Long id) {
        return payTradeAdminService.findById(id);
    }

    /// 同步支付状态
    public NormalPaySyncResult sync(Long id) {
        return payTradeAdminService.sync(id);
    }
}
