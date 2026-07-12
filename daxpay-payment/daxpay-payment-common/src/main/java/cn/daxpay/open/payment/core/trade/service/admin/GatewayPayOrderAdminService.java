package cn.daxpay.open.payment.core.trade.service.admin;

import cn.daxpay.open.payment.common.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.core.trade.convert.GatewayPayOrderConvert;
import cn.daxpay.open.payment.core.trade.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.core.trade.dao.PayTradeManager;
import cn.daxpay.open.payment.core.trade.entity.GatewayPayOrder;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.param.GatewayPayOrderQuery;
import cn.daxpay.open.payment.core.trade.result.GatewayPayOrderResult;
import cn.daxpay.open.payment.core.trade.service.PayCloseService;
import cn.daxpay.open.payment.core.trade.service.PaySyncService;
import cn.daxpay.open.payment.core.trade.service.PayUniHandleService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/// # 网关支付业务单管理服务
@Service
@RequiredArgsConstructor
public class GatewayPayOrderAdminService {

    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final PaySyncService paySyncService;
    private final PayCloseService payCloseService;
    private final PayUniHandleService payUniHandleService;

    public PageResult<GatewayPayOrderResult> page(PageParam pageParam, GatewayPayOrderQuery query) {
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

    public GatewayPayOrderResult findById(Long id) {
        GatewayPayOrder entity = gatewayPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        GatewayPayOrderResult result = GatewayPayOrderConvert.CONVERT.toResult(entity);
        PayTrade trade = payTradeManager.findByContainerId(id, PayTradeTypeEnum.GATEWAY.getCode()).orElse(null);
        if (Objects.nonNull(trade)) {
            result.setTradeNo(trade.getTradeNo());
            result.setOutOrderNo(trade.getOutOrderNo());
            result.setFundStatus(trade.getStatus());
            result.setRefundableBalance(trade.getRefundableBalance());
        }
        return result;
    }

    public NormalPaySyncResult sync(Long id) {
        PayTrade trade = payTradeManager.findByContainerId(id, PayTradeTypeEnum.GATEWAY.getCode())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        return paySyncService.syncPayOrder(trade);
    }

    public void close(Long id, boolean useCancel) {
        GatewayPayOrder order = gatewayPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        if (!List.of(GatewayOrderStatusEnum.WAIT_PAY.getCode(), GatewayOrderStatusEnum.PAYING.getCode())
                .contains(order.getStatus())) {
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
