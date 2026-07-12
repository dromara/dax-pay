package cn.daxpay.open.payment.admin.trade.service;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.core.trade.order.convert.NormalPayOrderConvert;
import cn.daxpay.open.payment.core.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.core.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.order.param.NormalPayOrderQuery;
import cn.daxpay.open.payment.core.trade.order.result.NormalPayOrderResult;
import cn.daxpay.open.payment.core.trade.runtime.close.service.PayCloseService;
import cn.daxpay.open.payment.core.trade.runtime.service.sync.PaySyncService;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 普通支付业务单管理服务(管理端)
///
/// 提供业务订单(容器)的分页/详情查询, 以及状态同步、关闭/撤销管理操作。
/// 详情场景联表资金凭证(PayTrade)补充交易字段; 同步/关闭复用 [PaySyncService] / [PayCloseService]。
@Slf4j
@Service
@RequiredArgsConstructor
public class NormalPayOrderAdminService {

    private final NormalPayOrderManager normalPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final PaySyncService paySyncService;
    private final PayCloseService payCloseService;

    /// 分页查询
    public PageResult<NormalPayOrderResult> page(PageParam pageParam, NormalPayOrderQuery query) {
        Page<NormalPayOrder> page = normalPayOrderManager.page(pageParam, query);
        // 手动转换, 避免侵入核心实体(不实现 ToResult)
        var records = page.getRecords().stream()
                .map(NormalPayOrderConvert.CONVERT::toResult)
                .toList();
        return new PageResult<NormalPayOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情查询(联表资金凭证补充交易字段)
    public NormalPayOrderResult findById(Long id) {
        NormalPayOrder entity = normalPayOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        NormalPayOrderResult result = NormalPayOrderConvert.CONVERT.toResult(entity);
        // 联表查询资金凭证, 补充交易字段
        PayTrade trade = payTradeManager.findByContainerId(id, cn.daxpay.open.payment.common.enums.PayTradeTypeEnum.NORMAL.getCode()).orElse(null);
        if (Objects.nonNull(trade)) {
            result.setTradeNo(trade.getTradeNo());
            result.setOutOrderNo(trade.getOutOrderNo());
            result.setFundStatus(trade.getStatus());
            result.setRefundableBalance(trade.getRefundableBalance());
            result.setPayBody(trade.getPayBody());
            result.setPayBodyType(trade.getPayBodyType());
        }
        return result;
    }

    /// 同步支付状态(传入业务订单ID)
    public NormalPaySyncResult sync(Long id) {
        PayTrade trade = payTradeManager.findByContainerId(id, cn.daxpay.open.payment.common.enums.PayTradeTypeEnum.NORMAL.getCode())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        return paySyncService.syncPayOrder(trade);
    }

    /// 关闭/撤销订单(传入业务订单ID)
    public void close(Long id, boolean useCancel) {
        PayTrade trade = payTradeManager.findByContainerId(id, cn.daxpay.open.payment.common.enums.PayTradeTypeEnum.NORMAL.getCode())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        payCloseService.closeOrder(trade, useCancel);
    }
}
