package cn.daxpay.open.payment.pay.service.admin;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.pay.convert.PayTradeAdminConvert;
import cn.daxpay.open.payment.pay.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.pay.order.dao.PayTradeManager;
import cn.daxpay.open.payment.pay.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.pay.param.order.PayTradeQuery;
import cn.daxpay.open.payment.pay.result.order.PayTradeResult;
import cn.daxpay.open.payment.pay.service.PayCloseService;
import cn.daxpay.open.payment.pay.service.PaySyncService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 资金交易凭证管理服务(管理端)
///
/// 提供资金交易(凭证)的分页/详情查询, 以及状态同步、关闭/撤销管理操作。
/// 详情场景联表容器(NormalPayOrder)补充业务字段; 同步/关闭复用 [PaySyncService] / [PayCloseService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayTradeAdminService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager normalPayOrderManager;
    private final PaySyncService paySyncService;
    private final PayCloseService payCloseService;

    /// 分页查询
    public PageResult<PayTradeResult> page(PageParam pageParam, PayTradeQuery query) {
        Page<PayTrade> page = payTradeManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(PayTradeAdminConvert.CONVERT::toResult)
                .toList();
        return new PageResult<PayTradeResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情查询(联表容器补充业务字段)
    public PayTradeResult findById(Long id) {
        PayTrade entity = payTradeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        PayTradeResult result = PayTradeAdminConvert.CONVERT.toResult(entity);
        // 联表查询容器, 补充业务字段
        NormalPayOrder normalOrder = normalPayOrderManager.findById(entity.getContainerId()).orElse(null);
        if (Objects.nonNull(normalOrder)) {
            result.setBizOrderNo(normalOrder.getBizOrderNo());
            result.setTitle(normalOrder.getTitle());
            result.setContainerStatus(normalOrder.getStatus());
        }
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
