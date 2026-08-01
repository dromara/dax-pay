package cn.daxpay.open.payment.app.admin.service.trade;

import cn.daxpay.open.payment.admin.service.trade.NormalPayOrderAdminService;
import cn.daxpay.open.payment.trade.order.param.NormalPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.NormalPayOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-普通支付业务单服务
///
/// 转发至 [NormalPayOrderAdminService]
@Service
@RequiredArgsConstructor
public class AppAdminNormalPayOrderService {

    private final NormalPayOrderAdminService normalPayOrderAdminService;

    /// 分页查询
    public PageResult<NormalPayOrderResult> page(PageParam pageParam, NormalPayOrderQuery query) {
        return normalPayOrderAdminService.page(pageParam, query);
    }

    /// 详情查询
    public NormalPayOrderResult findById(Long id) {
        return normalPayOrderAdminService.findById(id);
    }

    /// 同步支付状态
    public NormalPaySyncResult sync(Long id) {
        return normalPayOrderAdminService.sync(id);
    }
}
