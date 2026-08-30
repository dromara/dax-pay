package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.flow.convert.FundFlowConvert;
import cn.daxpay.open.payment.trade.flow.dao.FundFlowManager;
import cn.daxpay.open.payment.trade.flow.entity.FundFlow;
import cn.daxpay.open.payment.trade.flow.param.FundFlowQuery;
import cn.daxpay.open.payment.trade.flow.result.FundFlowResult;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 资金流水(商户端)
///
/// 强制按 [PaymentContext#getMchNo] 过滤；行级隔离另由 TenantLine 兜底。只读。
@Service
@RequiredArgsConstructor
public class MchFundFlowService {

    private final PaymentContext paymentContext;
    private final FundFlowManager fundFlowManager;

    /// 分页查询(强制当前商户)
    public PageResult<FundFlowResult> page(PageParam pageParam, FundFlowQuery query) {
        query.setMchNo(requireMchNo());
        Page<FundFlow> page = fundFlowManager.page(pageParam, query);
        var records = page.getRecords().stream().map(FundFlowConvert.CONVERT::toResult).toList();
        return new PageResult<FundFlowResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情
    public FundFlowResult findById(Long id) {
        FundFlow flow = fundFlowManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.fundFlow.notFound"));
        return FundFlowConvert.CONVERT.toResult(flow);
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
