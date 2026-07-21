package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.payment.trade.record.convert.PayCallbackRecordConvert;
import cn.daxpay.open.payment.trade.record.dao.PayCallbackRecordManager;
import cn.daxpay.open.payment.trade.record.entity.PayCallbackRecord;
import cn.daxpay.open.payment.trade.record.param.PayCallbackRecordQuery;
import cn.daxpay.open.payment.trade.record.result.PayCallbackRecordResult;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 通道入站回调记录管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCallbackRecordAdminService {

    private final PayCallbackRecordManager callbackRecordManager;
    private final TransService transService;

    /// 分页查询
    public PageResult<PayCallbackRecordResult> page(PageParam pageParam, PayCallbackRecordQuery query) {
        Page<PayCallbackRecord> page = callbackRecordManager.page(pageParam, query);
        var records = page.getRecords().stream().map(PayCallbackRecordConvert.CONVERT::toResult).toList();
        PageResult<PayCallbackRecordResult> pageResult = new PageResult<PayCallbackRecordResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情
    public PayCallbackRecordResult findById(Long id) {
        PayCallbackRecord record = callbackRecordManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.order.callbackRecordNotExist"));
        PayCallbackRecordResult result = PayCallbackRecordConvert.CONVERT.toResult(record);
        transService.translate(result);
        return result;
    }
}
