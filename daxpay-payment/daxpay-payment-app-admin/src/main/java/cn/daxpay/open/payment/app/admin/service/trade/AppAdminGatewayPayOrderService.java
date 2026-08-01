package cn.daxpay.open.payment.app.admin.service.trade;

import cn.daxpay.open.payment.admin.service.trade.GatewayPayOrderAdminService;
import cn.daxpay.open.payment.trade.order.param.GatewayPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-网关支付业务单服务
///
/// 转发至 [GatewayPayOrderAdminService]
@Service
@RequiredArgsConstructor
public class AppAdminGatewayPayOrderService {

    private final GatewayPayOrderAdminService gatewayPayOrderAdminService;

    /// 分页查询
    public PageResult<GatewayPayOrderResult> page(PageParam pageParam, GatewayPayOrderQuery query) {
        return gatewayPayOrderAdminService.page(pageParam, query);
    }

    /// 详情查询
    public GatewayPayOrderResult findById(Long id) {
        return gatewayPayOrderAdminService.findById(id);
    }

    /// 同步支付状态
    public NormalPaySyncResult sync(Long id) {
        return gatewayPayOrderAdminService.sync(id);
    }
}
