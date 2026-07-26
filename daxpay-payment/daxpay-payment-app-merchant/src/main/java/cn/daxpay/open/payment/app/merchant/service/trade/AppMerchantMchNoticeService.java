package cn.daxpay.open.payment.app.merchant.service.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchNoticeService;
import cn.daxpay.open.payment.trade.notice.param.MchNoticeTaskQuery;
import cn.daxpay.open.payment.trade.notice.result.MchNoticeTaskResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-商户出站通知服务
///
/// 转发至 [MchNoticeService]
@Service
@RequiredArgsConstructor
public class AppMerchantMchNoticeService {

    private final MchNoticeService mchNoticeService;

    /// 通知任务分页
    public PageResult<MchNoticeTaskResult> pageTask(PageParam pageParam, MchNoticeTaskQuery query) {
        return mchNoticeService.pageTask(pageParam, query);
    }

    /// 通知任务详情
    public MchNoticeTaskResult findTaskById(Long id) {
        return mchNoticeService.findTaskById(id);
    }
}
