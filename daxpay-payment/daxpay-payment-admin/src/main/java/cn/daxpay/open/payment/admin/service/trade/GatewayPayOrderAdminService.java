package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.convert.GatewayPayOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.param.GatewayPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.GatewayPayOrderResult;
import cn.daxpay.open.payment.trade.order.service.TradeOrderDetailAssembler;
import cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 网关支付业务单管理服务
@Service
@RequiredArgsConstructor
public class GatewayPayOrderAdminService {

    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PaySyncService paySyncService;
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
        return paySyncService.syncByContainer(id, PayTradeTypeEnum.GATEWAY.getCode());
    }
}
