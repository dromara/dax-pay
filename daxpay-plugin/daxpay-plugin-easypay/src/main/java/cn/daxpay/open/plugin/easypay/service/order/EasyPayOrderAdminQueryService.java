package cn.daxpay.open.plugin.easypay.service.order;

import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.close.PayCloseService;
import cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.param.order.EasyPayOrderQuery;
import cn.daxpay.open.plugin.easypay.result.order.EasyPayOrderResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 易支付协议订单管理端查询服务(运营端)
///
/// 承接运营后台的易支付订单管理入口：分页、详情、同步、关单。跨商户查询，按 mchNo 翻译商户名称。
/// 同步/关单直达内核 [PaySyncService]/[PayCloseService]（等价原共享查询服务的透传行为）。
/// 生命周期回写(支付成功/退款/关单钩子) 见 [EasyPayOrderService], 与本类解耦以避免循环依赖。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayOrderAdminQueryService {

    private final EasyPayOrderManager easyPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final PaySyncService paySyncService;
    private final PayCloseService payCloseService;
    private final TransService transService;

    /// 分页查询
    public PageResult<EasyPayOrderResult> page(PageParam pageParam, EasyPayOrderQuery query) {
        Page<EasyPayOrder> page = easyPayOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(EasyPayOrder::toResult)
                .toList();
        var pageResult = new PageResult<EasyPayOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        // 翻译商户名称
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情查询
    public EasyPayOrderResult findById(Long id) {
        EasyPayOrder entity = easyPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        EasyPayOrderResult result = entity.toResult();
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 同步支付状态(直达内核资金凭证同步)
    public NormalPaySyncResult sync(Long id) {
        EasyPayOrder entity = easyPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        if (entity.getOrderId() == null) {
            throw new DataNotExistException("pay.error.payOrderNotExist");
        }
        PayTrade trade = payTradeManager.findByContainerId(entity.getOrderId(), PayTradeTypeEnum.NORMAL.getCode())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        return paySyncService.syncPayOrder(trade);
    }

    /// 关闭/撤销订单(直达内核资金凭证关单)
    public void close(Long id, boolean useCancel) {
        EasyPayOrder entity = easyPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        if (entity.getOrderId() == null) {
            throw new DataNotExistException("pay.error.payOrderNotExist");
        }
        PayTrade trade = payTradeManager.findByContainerId(entity.getOrderId(), PayTradeTypeEnum.NORMAL.getCode())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        payCloseService.closeOrder(trade, useCancel);
    }
}
