package cn.daxpay.open.payment.admin.service.trade;

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
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 商户出站通知管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MchNoticeAdminService {

    private final MchNoticeTaskManager taskManager;
    private final MchNoticeRecordManager recordManager;
    private final NoticeSendEngine noticeSendEngine;
    private final TransService transService;

    /// 任务分页
    public PageResult<MchNoticeTaskResult> pageTask(PageParam pageParam, MchNoticeTaskQuery query) {
        Page<MchNoticeTask> page = taskManager.page(pageParam, query);
        var records = page.getRecords().stream().map(MchNoticeTaskConvert.CONVERT::toResult).toList();
        PageResult<MchNoticeTaskResult> pageResult = new PageResult<MchNoticeTaskResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        transService.translate(pageResult);
        return pageResult;
    }

    /// 任务详情
    public MchNoticeTaskResult findTaskById(Long id) {
        MchNoticeTask task = taskManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.order.callbackTaskNotExist"));
        MchNoticeTaskResult result = MchNoticeTaskConvert.CONVERT.toResult(task);
        transService.translate(result);
        return result;
    }

    /// 手动重发
    public void resend(Long id) {
        taskManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.order.callbackTaskNotExist"));
        noticeSendEngine.sendManual(id);
    }

    /// 发送记录分页
    public PageResult<MchNoticeRecordResult> pageRecord(PageParam pageParam, MchNoticeRecordQuery query) {
        Page<MchNoticeRecord> page = recordManager.page(pageParam, query);
        var records = page.getRecords().stream().map(MchNoticeRecordConvert.CONVERT::toResult).toList();
        return new PageResult<MchNoticeRecordResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }
}
