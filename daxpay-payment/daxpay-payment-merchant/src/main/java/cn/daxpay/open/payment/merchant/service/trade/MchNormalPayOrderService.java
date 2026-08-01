package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.convert.NormalPayOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.param.NormalPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.NormalPayOrderResult;
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

/// # 普通支付业务单(商户端)
///
/// 强制按 [PaymentContext#getMchNo] 过滤；行级隔离另由 TenantLine 兜底。
@Service
@RequiredArgsConstructor
public class MchNormalPayOrderService {

    private final PaymentContext paymentContext;
    private final NormalPayOrderManager normalPayOrderManager;
    private final PaySyncService paySyncService;
    private final TradeOrderDetailAssembler tradeOrderDetailAssembler;

    /// 分页查询(强制当前商户)
    public PageResult<NormalPayOrderResult> page(PageParam pageParam, NormalPayOrderQuery query) {
        forceMchNo(query);
        Page<NormalPayOrder> page = normalPayOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(NormalPayOrderConvert.CONVERT::toResult)
                .toList();
        return new PageResult<NormalPayOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情查询(联表资金凭证补充交易字段)
    public NormalPayOrderResult findById(Long id) {
        NormalPayOrder entity = normalPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        NormalPayOrderResult result = NormalPayOrderConvert.CONVERT.toResult(entity);
        tradeOrderDetailAssembler.fillFundOnNormal(result, id);
        return result;
    }

    /// 同步支付状态(传入业务订单ID)
    public NormalPaySyncResult sync(Long id) {
        return paySyncService.syncByContainer(id, PayTradeTypeEnum.NORMAL.getCode());
    }

    /// 解析并强制写入当前商户号
    private void forceMchNo(NormalPayOrderQuery query) {
        String mchNo = requireMchNo();
        if (query == null) {
            return;
        }
        query.setMchNo(mchNo);
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
