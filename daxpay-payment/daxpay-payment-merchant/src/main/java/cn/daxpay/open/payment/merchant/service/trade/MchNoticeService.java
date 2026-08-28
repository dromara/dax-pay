package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.notice.convert.MchNoticeRecordConvert;
import cn.daxpay.open.payment.trade.notice.convert.MchNoticeTaskConvert;
import cn.daxpay.open.payment.trade.notice.dao.MchNoticeRecordManager;
import cn.daxpay.open.payment.trade.notice.dao.MchNoticeTaskManager;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeRecord;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.param.MchNoticeRecordQuery;
import cn.daxpay.open.payment.trade.notice.param.MchNoticeTaskQuery;
import cn.daxpay.open.payment.trade.notice.result.MchNoticeRecordResult;
import cn.daxpay.open.payment.trade.notice.result.MchNoticeTaskResult;
import cn.daxpay.open.payment.trade.notice.service.NoticeSendEngine;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 商户出站通知(商户端)
///
/// 强制按 [PaymentContext#getMchNo] 过滤；行级隔离另由 TenantLine 兜底。
@Slf4j
@Service
@RequiredArgsConstructor
public class MchNoticeService {

    private final PaymentContext paymentContext;
    private final MchNoticeTaskManager taskManager;
    private final MchNoticeRecordManager recordManager;
    private final NoticeSendEngine noticeSendEngine;

    /// 任务分页
    public PageResult<MchNoticeTaskResult> pageTask(PageParam pageParam, MchNoticeTaskQuery query) {
        forceMchNo(query);
        Page<MchNoticeTask> page = taskManager.page(pageParam, query);
        var records = page.getRecords().stream().map(MchNoticeTaskConvert.CONVERT::toResult).toList();
        return new PageResult<MchNoticeTaskResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 任务详情
    public MchNoticeTaskResult findTaskById(Long id) {
        MchNoticeTask task = taskManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.order.callbackTaskNotExist"));
        return MchNoticeTaskConvert.CONVERT.toResult(task);
    }

    /// 手动重发
    public void resend(Long id) {
        taskManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.order.callbackTaskNotExist"));
        noticeSendEngine.sendManual(id);
    }

    /// 发送记录分页
    public PageResult<MchNoticeRecordResult> pageRecord(PageParam pageParam, MchNoticeRecordQuery query) {
        forceMchNo(query);
        Page<MchNoticeRecord> page = recordManager.page(pageParam, query);
        var records = page.getRecords().stream().map(MchNoticeRecordConvert.CONVERT::toResult).toList();
        return new PageResult<MchNoticeRecordResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    private void forceMchNo(MchNoticeTaskQuery query) {
        query.setMchNo(requireMchNo());
    }

    private void forceMchNo(MchNoticeRecordQuery query) {
        query.setMchNo(requireMchNo());
    }

    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户: 数据错误未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        return mchNo;
    }
}
