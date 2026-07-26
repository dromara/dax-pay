package cn.daxpay.open.payment.app.admin.service.trade;

import cn.daxpay.open.payment.admin.service.trade.MchNoticeAdminService;
import cn.daxpay.open.payment.trade.notice.param.MchNoticeRecordQuery;
import cn.daxpay.open.payment.trade.notice.param.MchNoticeTaskQuery;
import cn.daxpay.open.payment.trade.notice.result.MchNoticeRecordResult;
import cn.daxpay.open.payment.trade.notice.result.MchNoticeTaskResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-商户出站通知服务
///
/// 转发至 [MchNoticeAdminService]
@Service
@RequiredArgsConstructor
public class AppAdminMchNoticeService {

    private final MchNoticeAdminService mchNoticeAdminService;

    /// 通知任务分页
    public PageResult<MchNoticeTaskResult> pageTask(PageParam pageParam, MchNoticeTaskQuery query) {
        return mchNoticeAdminService.pageTask(pageParam, query);
    }

    /// 通知任务详情
    public MchNoticeTaskResult findTaskById(Long id) {
        return mchNoticeAdminService.findTaskById(id);
    }

    /// 手动重发通知
    public void resend(Long id) {
        mchNoticeAdminService.resend(id);
    }

    /// 发送记录分页
    public PageResult<MchNoticeRecordResult> pageRecord(PageParam pageParam, MchNoticeRecordQuery query) {
        return mchNoticeAdminService.pageRecord(pageParam, query);
    }
}
