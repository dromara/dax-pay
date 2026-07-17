package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.convert.NormalPayOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.param.NormalPayOrderQuery;
import cn.daxpay.open.payment.trade.order.result.NormalPayOrderResult;
import cn.daxpay.open.payment.trade.runtime.service.close.PayCloseService;
import cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 普通支付业务单管理服务(管理端)
///
/// 提供业务订单(容器)的分页/详情查询, 以及状态同步、关闭/撤销管理操作。
/// 详情场景由 [TradeOrderDetailAssembler] 联表资金凭证补充交易字段。
@Slf4j
@Service
@RequiredArgsConstructor
public class NormalPayOrderAdminService {

    private final NormalPayOrderManager normalPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final PaySyncService paySyncService;
    private final PayCloseService payCloseService;
    private final TransService transService;
    private final TradeOrderDetailAssembler tradeOrderDetailAssembler;

    /// 分页查询
    public PageResult<NormalPayOrderResult> page(PageParam pageParam, NormalPayOrderQuery query) {
        Page<NormalPayOrder> page = normalPayOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(NormalPayOrderConvert.CONVERT::toResult)
                .toList();
        PageResult<NormalPayOrderResult> pageResult = new PageResult<NormalPayOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        // 翻译商户名称(mchNo -> mchName)
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情查询(联表资金凭证补充交易字段)
    public NormalPayOrderResult findById(Long id) {
        NormalPayOrder entity = normalPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        NormalPayOrderResult result = NormalPayOrderConvert.CONVERT.toResult(entity);
        tradeOrderDetailAssembler.fillFundOnNormal(result, id);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 同步支付状态(传入业务订单ID)
    public NormalPaySyncResult sync(Long id) {
        PayTrade trade = payTradeManager.findByContainerId(id, PayTradeTypeEnum.NORMAL.getCode())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        return paySyncService.syncPayOrder(trade);
    }

    /// 关闭/撤销订单(传入业务订单ID)
    public void close(Long id, boolean useCancel) {
        PayTrade trade = payTradeManager.findByContainerId(id, PayTradeTypeEnum.NORMAL.getCode())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        payCloseService.closeOrder(trade, useCancel);
    }
}
