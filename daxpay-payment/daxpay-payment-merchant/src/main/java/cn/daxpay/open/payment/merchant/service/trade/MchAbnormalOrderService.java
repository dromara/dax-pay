package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.abnormal.convert.AbnormalOrderConvert;
import cn.daxpay.open.payment.trade.abnormal.dao.AbnormalOrderManager;
import cn.daxpay.open.payment.trade.abnormal.entity.AbnormalOrder;
import cn.daxpay.open.payment.trade.abnormal.param.AbnormalOrderQuery;
import cn.daxpay.open.payment.trade.abnormal.result.AbnormalOrderResult;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 异常订单(商户端)
///
/// 强制按 [PaymentContext#getMchNo] 过滤；行级隔离另由 TenantLine 兜底。
/// 商户端只读(2026-08-29 决策): 处置动作(确认成功/忽略)仍由运营端操作。
@Service
@RequiredArgsConstructor
public class MchAbnormalOrderService {

    private final PaymentContext paymentContext;
    private final AbnormalOrderManager abnormalOrderManager;

    /// 分页查询(强制当前商户)
    public PageResult<AbnormalOrderResult> page(PageParam pageParam, AbnormalOrderQuery query) {
        query.setMchNo(requireMchNo());
        Page<AbnormalOrder> page = abnormalOrderManager.page(pageParam, query);
        var records = page.getRecords().stream().map(AbnormalOrderConvert.CONVERT::toResult).toList();
        return new PageResult<AbnormalOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情
    public AbnormalOrderResult findById(Long id) {
        AbnormalOrder abnormal = abnormalOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.abnormal.notFound"));
        return AbnormalOrderConvert.CONVERT.toResult(abnormal);
    }

    /// 解析并强制写入当前商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户: 数据错误未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        return mchNo;
    }
}
