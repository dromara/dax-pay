package cn.daxpay.open.payment.app.admin.service.trade;

import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import cn.daxpay.open.payment.trade.order.service.PayTradeService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-资金交易凭证服务
///
/// 转发至 core [PayTradeService]
@Service
@RequiredArgsConstructor
public class AppAdminPayTradeService {

    private final PayTradeService payTradeService;

    /// 分页查询
    public PageResult<PayTradeResult> page(PageParam pageParam, PayTradeQuery query) {
        return payTradeService.page(pageParam, query);
    }

    /// 详情查询
    public PayTradeResult findById(Long id) {
        return payTradeService.findById(id);
    }

    /// 同步支付状态
    public NormalPaySyncResult sync(Long id) {
        return payTradeService.sync(id);
    }

    /// 关闭/撤销订单
    public void close(Long id, boolean useCancel) {
        payTradeService.close(id, useCancel);
    }
}
