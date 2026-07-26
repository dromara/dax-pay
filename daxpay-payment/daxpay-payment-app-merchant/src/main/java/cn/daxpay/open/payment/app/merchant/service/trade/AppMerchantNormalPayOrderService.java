package cn.daxpay.open.payment.app.merchant.service.trade;

import cn.daxpay.open.payment.trade.order.param.NormalPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.NormalPayOrderResult;
import cn.daxpay.open.payment.trade.order.service.NormalPayOrderService;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-普通支付业务单服务
///
/// 转发至 core [NormalPayOrderService]
@Service
@RequiredArgsConstructor
public class AppMerchantNormalPayOrderService {

    private final NormalPayOrderService normalPayOrderService;

    /// 分页查询
    public PageResult<NormalPayOrderResult> page(PageParam pageParam, NormalPayOrderQuery query) {
        return normalPayOrderService.page(pageParam, query);
    }

    /// 详情查询
    public NormalPayOrderResult findById(Long id) {
        return normalPayOrderService.findById(id);
    }
}
