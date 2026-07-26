package cn.daxpay.open.payment.app.admin.service.trade;

import cn.daxpay.open.payment.admin.service.trade.PayCallbackRecordAdminService;
import cn.daxpay.open.payment.trade.record.param.PayCallbackRecordQuery;
import cn.daxpay.open.payment.trade.record.result.PayCallbackRecordResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-通道入站回调记录服务
///
/// 转发至 [PayCallbackRecordAdminService]
@Service
@RequiredArgsConstructor
public class AppAdminPayCallbackRecordService {

    private final PayCallbackRecordAdminService payCallbackRecordAdminService;

    /// 回调记录分页
    public PageResult<PayCallbackRecordResult> page(PageParam pageParam, PayCallbackRecordQuery query) {
        return payCallbackRecordAdminService.page(pageParam, query);
    }

    /// 回调记录详情
    public PayCallbackRecordResult findById(Long id) {
        return payCallbackRecordAdminService.findById(id);
    }
}
