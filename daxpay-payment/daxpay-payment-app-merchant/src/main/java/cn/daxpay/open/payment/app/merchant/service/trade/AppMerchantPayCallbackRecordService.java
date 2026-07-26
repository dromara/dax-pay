package cn.daxpay.open.payment.app.merchant.service.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchPayCallbackRecordService;
import cn.daxpay.open.payment.trade.record.param.PayCallbackRecordQuery;
import cn.daxpay.open.payment.trade.record.result.PayCallbackRecordResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-通道回调记录服务
///
/// 转发至 [MchPayCallbackRecordService]
@Service
@RequiredArgsConstructor
public class AppMerchantPayCallbackRecordService {

    private final MchPayCallbackRecordService mchPayCallbackRecordService;

    /// 分页查询
    public PageResult<PayCallbackRecordResult> page(PageParam pageParam, PayCallbackRecordQuery query) {
        return mchPayCallbackRecordService.page(pageParam, query);
    }

    /// 详情查询
    public PayCallbackRecordResult findById(Long id) {
        return mchPayCallbackRecordService.findById(id);
    }
}
