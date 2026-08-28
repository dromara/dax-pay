package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.convert.GatewayPayOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.param.GatewayPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
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

/// # 网关支付业务单(商户端)
///
/// 强制按 [PaymentContext#getMchNo] 过滤；行级隔离另由 TenantLine 兜底。
@Service
@RequiredArgsConstructor
public class MchGatewayPayOrderService {

    private final PaymentContext paymentContext;
    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PaySyncService paySyncService;
    private final TradeOrderDetailAssembler tradeOrderDetailAssembler;

    /// 分页查询(强制当前商户)
    public PageResult<GatewayPayOrderResult> page(PageParam pageParam, GatewayPayOrderQuery query) {
        forceMchNo(query);
        Page<GatewayPayOrder> page = gatewayPayOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(GatewayPayOrderConvert.CONVERT::toResult)
                .toList();
        return new PageResult<GatewayPayOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情(联表资金凭证)
    public GatewayPayOrderResult findById(Long id) {
        GatewayPayOrder entity = gatewayPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        GatewayPayOrderResult result = GatewayPayOrderConvert.CONVERT.toResult(entity);
        tradeOrderDetailAssembler.fillFundOnGateway(result, id);
        return result;
    }

    /// 同步支付状态
    public NormalPaySyncResult sync(Long id) {
        return paySyncService.syncByContainer(id, PayTradeTypeEnum.GATEWAY.getCode());
    }

    /// 解析并强制写入当前商户号
    private void forceMchNo(GatewayPayOrderQuery query) {
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
