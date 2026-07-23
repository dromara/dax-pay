package cn.daxpay.open.payment.trade.order.service;

import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.payment.trade.order.convert.PayTradeResultConvert;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.order.param.PayTradeQuery;
import cn.daxpay.open.payment.trade.order.result.PayTradeResult;
import cn.daxpay.open.payment.trade.runtime.service.close.PayCloseService;
import cn.daxpay.open.payment.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 资金交易凭证共享服务
///
/// 运营端 / 商户端共用：分页、详情、同步、关闭。
/// 商户端强制清空入参 mchNo（行级隔离依赖 TenantLine）；运营端保留跨商户筛选并 translate mchName。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayTradeService {

    private final PayTradeManager payTradeManager;
    private final PaySyncService paySyncService;
    private final PayCloseService payCloseService;
    private final TransService transService;
    private final TradeOrderDetailAssembler tradeOrderDetailAssembler;
    private final ClientCodeService clientCodeService;

    /// 分页查询
    public PageResult<PayTradeResult> page(PageParam pageParam, PayTradeQuery query) {
        sanitizeQuery(query);
        Page<PayTrade> page = payTradeManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(PayTradeResultConvert.CONVERT::toResult)
                .toList();
        PageResult<PayTradeResult> pageResult = new PageResult<PayTradeResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        translateIfAdmin(pageResult);
        return pageResult;
    }

    /// 详情查询(按 tradeType 联表容器补充业务字段)
    public PayTradeResult findById(Long id) {
        PayTrade entity = payTradeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        PayTradeResult result = PayTradeResultConvert.CONVERT.toResult(entity);
        tradeOrderDetailAssembler.fillContainerOnTrade(result, entity);
        translateIfAdmin(result);
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

    /// 商户端忽略入参 mchNo，避免越权指定他商户
    private void sanitizeQuery(PayTradeQuery query) {
        if (query == null) {
            return;
        }
        if (ClientEnum.MERCHANT.getCode().equals(clientCodeService.getClientCode())) {
            query.setMchNo(null);
        }
    }

    private void translateIfAdmin(Object target) {
        if (ClientEnum.ADMIN.getCode().equals(clientCodeService.getClientCode())) {
            // 翻译商户名称(mchNo -> mchName)
            transService.translate(target);
        }
    }
}
