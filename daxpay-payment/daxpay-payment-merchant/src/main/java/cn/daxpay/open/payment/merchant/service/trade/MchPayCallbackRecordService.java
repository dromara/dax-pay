package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.record.convert.PayCallbackRecordConvert;
import cn.daxpay.open.payment.trade.record.dao.PayCallbackRecordManager;
import cn.daxpay.open.payment.trade.record.entity.PayCallbackRecord;
import cn.daxpay.open.payment.trade.record.param.PayCallbackRecordQuery;
import cn.daxpay.open.payment.trade.record.result.PayCallbackRecordResult;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 通道入站回调记录(商户端)
///
/// 强制按 [PaymentContext#getMchNo] 过滤；行级隔离另由 TenantLine 兜底。
@Slf4j
@Service
@RequiredArgsConstructor
public class MchPayCallbackRecordService {

    private final PaymentContext paymentContext;
    private final PayCallbackRecordManager callbackRecordManager;

    /// 分页查询
    public PageResult<PayCallbackRecordResult> page(PageParam pageParam, PayCallbackRecordQuery query) {
        forceMchNo(query);
        Page<PayCallbackRecord> page = callbackRecordManager.page(pageParam, query);
        var records = page.getRecords().stream().map(PayCallbackRecordConvert.CONVERT::toResult).toList();
        return new PageResult<PayCallbackRecordResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情
    public PayCallbackRecordResult findById(Long id) {
        PayCallbackRecord record = callbackRecordManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.order.callbackRecordNotExist"));
        return PayCallbackRecordConvert.CONVERT.toResult(record);
    }

    private void forceMchNo(PayCallbackRecordQuery query) {
        String mchNo = requireMchNo();
        if (query == null) {
            return;
        }
        query.setMchNo(mchNo);
    }

    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        return mchNo;
    }
}
