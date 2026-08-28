package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.record.convert.PayCallbackRecordConvert;
import cn.daxpay.open.payment.trade.record.dao.PayCallbackRecordManager;
import cn.daxpay.open.payment.trade.record.entity.PayCallbackRecord;
import cn.daxpay.open.payment.trade.record.param.PayCallbackRecordQuery;
import cn.daxpay.open.payment.trade.record.result.PayCallbackRecordResult;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
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
/// 原始回调报文(notifyInfo)/冗余字段不下发, 异常状态错误信息受控化, 详见 [#sanitizeForMerchant]。
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
        // 原始报文/冗余字段裁剪, 异常状态错误信息受控化
        var records = page.getRecords().stream()
                .map(PayCallbackRecordConvert.CONVERT::toResult)
                .map(this::sanitizeForMerchant)
                .toList();
        return new PageResult<PayCallbackRecordResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情
    public PayCallbackRecordResult findById(Long id) {
        String mchNo = requireMchNo();
        PayCallbackRecord record = callbackRecordManager.findByIdAndMchNo(id, mchNo)
                .orElseThrow(() -> new DataNotExistException("pay.error.order.callbackRecordNotExist"));
        return sanitizeForMerchant(PayCallbackRecordConvert.CONVERT.toResult(record));
    }

    /// 商户端字段裁剪: 原始报文/冗余字段不下发, 异常状态错误信息受控化
    private PayCallbackRecordResult sanitizeForMerchant(PayCallbackRecordResult result) {
        // 原始回调报文含买家PII/通道签名/请求头等, 不下发
        result.setNotifyInfo(null);
        // 冗余字段不下发(最小披露原则); mchNo 在父类单独赋值, 避免链式返回类型陷阱
        result.setMchName(null);
        result.setAppId(null);
        result.setMchNo(null);
        // 异常状态错误信息可能含内部堆栈/路径, 替换为受控文案
        if (CallbackStatusEnum.EXCEPTION.getCode().equals(result.getStatus())) {
            result.setErrorMsg(I18nUtil.get("pay.error.callback.abnormal"));
        }
        return result;
    }

    private void forceMchNo(PayCallbackRecordQuery query) {
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
