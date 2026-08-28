package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.order.convert.PayTradeResultConvert;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import cn.daxpay.open.payment.trade.order.service.TradeOrderDetailAssembler;
import cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 资金交易凭证(商户端)
///
/// 强制按 [PaymentContext#getMchNo] 过滤；行级隔离另由 TenantLine 兜底。
@Service
@RequiredArgsConstructor
public class MchPayTradeService {

    private final PaymentContext paymentContext;
    private final PayTradeManager payTradeManager;
    private final PaySyncService paySyncService;
    private final TradeOrderDetailAssembler tradeOrderDetailAssembler;

    /// 分页查询(强制当前商户)
    public PageResult<PayTradeResult> page(PageParam pageParam, PayTradeQuery query) {
        forceMchNo(query);
        Page<PayTrade> page = payTradeManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(PayTradeResultConvert.CONVERT::toResult)
                .toList();
        return new PageResult<PayTradeResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情查询(按 tradeType 联表容器补充业务字段)
    public PayTradeResult findById(Long id) {
        PayTrade entity = payTradeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        PayTradeResult result = PayTradeResultConvert.CONVERT.toResult(entity);
        tradeOrderDetailAssembler.fillContainerOnTrade(result, entity);
        return result;
    }

    /// 同步支付状态(传入资金交易ID)
    public NormalPaySyncResult sync(Long id) {
        PayTrade trade = payTradeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        return paySyncService.syncPayOrder(trade);
    }

    /// 解析并强制写入当前商户号
    private void forceMchNo(PayTradeQuery query) {
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
