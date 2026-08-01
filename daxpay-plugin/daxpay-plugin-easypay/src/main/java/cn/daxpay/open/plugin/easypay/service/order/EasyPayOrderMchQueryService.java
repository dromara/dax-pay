package cn.daxpay.open.plugin.easypay.service.order;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.close.PayCloseService;
import cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
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

/// # 易支付协议订单查询服务(商户端)
///
/// 承接商户端的易支付订单管理入口：分页、详情、同步、关单。
/// 强制按 [PaymentContext#getMchNo] 过滤；行级隔离另由 TenantLine 兜底。
/// 同步/关单直达内核 [PaySyncService]/[PayCloseService]（等价原共享查询服务的透传行为）。
/// 生命周期回写(支付成功/退款/关单钩子) 见 [EasyPayOrderService], 与本类解耦以避免循环依赖。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayOrderMchQueryService {

    private final PaymentContext paymentContext;
    private final EasyPayOrderManager easyPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final PaySyncService paySyncService;
    private final PayCloseService payCloseService;

    /// 分页查询(强制当前商户)
    public PageResult<EasyPayOrderResult> page(PageParam pageParam, EasyPayOrderQuery query) {
        forceMchNo(query);
        Page<EasyPayOrder> page = easyPayOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(EasyPayOrder::toResult)
                .toList();
        return new PageResult<EasyPayOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情查询
    public EasyPayOrderResult findById(Long id) {
        EasyPayOrder entity = easyPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        return entity.toResult();
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

    /// 关闭订单(直达内核资金凭证关单)
    public void close(Long id) {
        EasyPayOrder entity = easyPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        if (entity.getOrderId() == null) {
            throw new DataNotExistException("pay.error.payOrderNotExist");
        }
        PayTrade trade = payTradeManager.findByContainerId(entity.getOrderId(), PayTradeTypeEnum.NORMAL.getCode())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        payCloseService.closeOrder(trade, false);
    }

    /// 解析并强制写入当前商户号
    private void forceMchNo(EasyPayOrderQuery query) {
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
