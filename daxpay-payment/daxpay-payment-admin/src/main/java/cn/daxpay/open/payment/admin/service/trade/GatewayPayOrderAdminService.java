package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.convert.GatewayPayOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.param.GatewayPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
import cn.daxpay.open.payment.trade.order.service.TradeOrderDetailAssembler;
import cn.daxpay.open.payment.trade.runtime.service.close.PayCloseService;
import cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.trade.runtime.service.pay.common.PayUniHandleService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 网关支付业务单管理服务
@Service
@RequiredArgsConstructor
public class GatewayPayOrderAdminService {

    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final PaySyncService paySyncService;
    private final PayCloseService payCloseService;
    private final PayUniHandleService payUniHandleService;
    private final TransService transService;
    private final TradeOrderDetailAssembler tradeOrderDetailAssembler;

    public PageResult<GatewayPayOrderResult> page(PageParam pageParam, GatewayPayOrderQuery query) {
        Page<GatewayPayOrder> page = gatewayPayOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(GatewayPayOrderConvert.CONVERT::toResult)
                .toList();
        PageResult<GatewayPayOrderResult> pageResult = new PageResult<GatewayPayOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        // 翻译商户名称
        transService.translate(pageResult);
        return pageResult;
    }

    public GatewayPayOrderResult findById(Long id) {
        GatewayPayOrder entity = gatewayPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        GatewayPayOrderResult result = GatewayPayOrderConvert.CONVERT.toResult(entity);
        tradeOrderDetailAssembler.fillFundOnGateway(result, id);
        transService.translate(result);
        return result;
    }

    public NormalPaySyncResult sync(Long id) {
        PayTrade trade = payTradeManager.findByContainerId(id, PayTradeTypeEnum.GATEWAY.getCode())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        return paySyncService.syncPayOrder(trade);
    }

    public void close(Long id, boolean useCancel) {
        GatewayPayOrder order = gatewayPayOrderManager.findById(id)
                // 支付: 支付订单不存在
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        if (!List.of(GatewayOrderStatusEnum.WAIT_PAY.getCode(), GatewayOrderStatusEnum.PAYING.getCode())
                .contains(order.getStatus())) {
            // 支付: 订单不是支付中无法进行关闭订单
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.closeNotPaying");
        }
        PayTrade trade = payTradeManager.findByContainerId(id, PayTradeTypeEnum.GATEWAY.getCode()).orElse(null);
        if (trade != null) {
            payCloseService.closeOrder(trade, useCancel);
        } else {
            payUniHandleService.gatewayOrderClose(order);
        }
    }
}
