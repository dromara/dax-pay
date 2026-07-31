package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.payment.trade.order.convert.PayTradeResultConvert;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import cn.daxpay.open.payment.trade.order.service.TradeOrderDetailAssembler;
import cn.daxpay.open.payment.trade.runtime.service.close.PayCloseService;
import cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 资金交易凭证管理服务
///
/// 运营端专属。跨商户查询，按 mchNo 翻译商户名称。
@Service
@RequiredArgsConstructor
public class PayTradeAdminService {

    private final PayTradeManager payTradeManager;
    private final PaySyncService paySyncService;
    private final PayCloseService payCloseService;
    private final TransService transService;
    private final TradeOrderDetailAssembler tradeOrderDetailAssembler;

    /// 分页查询
    public PageResult<PayTradeResult> page(PageParam pageParam, PayTradeQuery query) {
        Page<PayTrade> page = payTradeManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(PayTradeResultConvert.CONVERT::toResult)
                .toList();
        PageResult<PayTradeResult> pageResult = new PageResult<PayTradeResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        // 翻译商户名称
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情查询(按 tradeType 联表容器补充业务字段)
    public PayTradeResult findById(Long id) {
        PayTrade entity = payTradeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        PayTradeResult result = PayTradeResultConvert.CONVERT.toResult(entity);
        tradeOrderDetailAssembler.fillContainerOnTrade(result, entity);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 同步支付状态(传入资金交易ID)
    public NormalPaySyncResult sync(Long id) {
        PayTrade trade = payTradeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        return paySyncService.syncPayOrder(trade);
    }

    /// 关闭/撤销订单(传入资金交易ID)
    public void close(Long id, boolean useCancel) {
        PayTrade trade = payTradeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        payCloseService.closeOrder(trade, useCancel);
    }
}
