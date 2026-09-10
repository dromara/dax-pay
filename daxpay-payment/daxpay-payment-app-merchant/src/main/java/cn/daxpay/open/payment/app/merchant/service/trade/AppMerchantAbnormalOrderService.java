package cn.daxpay.open.payment.app.merchant.service.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchAbnormalOrderService;
import cn.daxpay.open.payment.trade.abnormal.param.AbnormalOrderQuery;
import cn.daxpay.open.payment.trade.abnormal.result.AbnormalOrderResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-异常订单服务
///
/// 转发至 [MchAbnormalOrderService]
@Service
@RequiredArgsConstructor
public class AppMerchantAbnormalOrderService {

    private final MchAbnormalOrderService mchAbnormalOrderService;

    /// 分页查询
    public PageResult<AbnormalOrderResult> page(PageParam pageParam, AbnormalOrderQuery query) {
        return mchAbnormalOrderService.page(pageParam, query);
    }

    /// 详情查询
    public AbnormalOrderResult findById(Long id) {
        return mchAbnormalOrderService.findById(id);
    }
}
