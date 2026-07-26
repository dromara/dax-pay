package cn.daxpay.open.payment.app.merchant.service.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchGatewayPayOrderService;
import cn.daxpay.open.payment.trade.order.param.GatewayPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-网关支付业务单服务
///
/// 转发至 [MchGatewayPayOrderService]
@Service
@RequiredArgsConstructor
public class AppMerchantGatewayPayOrderService {

    private final MchGatewayPayOrderService mchGatewayPayOrderService;

    /// 分页查询
    public PageResult<GatewayPayOrderResult> page(PageParam pageParam, GatewayPayOrderQuery query) {
        return mchGatewayPayOrderService.page(pageParam, query);
    }

    /// 详情查询
    public GatewayPayOrderResult findById(Long id) {
        return mchGatewayPayOrderService.findById(id);
    }
}
