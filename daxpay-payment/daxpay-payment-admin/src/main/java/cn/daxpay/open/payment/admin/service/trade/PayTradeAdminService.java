package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.admin.convert.trade.PayTradeAdminConvert;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
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
    private final TransService transService;

    /// 分页查询
    public PageResult<PayTradeResult> page(PageParam pageParam, PayTradeQuery query) {
        Page<PayTrade> page = payTradeManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(PayTradeAdminConvert.CONVERT::toResult)
                .toList();
        PageResult<PayTradeResult> pageResult = new PageResult<PayTradeResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        // 翻译商户名称(mchNo -> mchName)
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情查询(联表容器补充业务字段)
    public PayTradeResult findById(Long id) {
        PayTrade entity = payTradeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        PayTradeResult result = PayTradeAdminConvert.CONVERT.toResult(entity);
        // 联表查询容器, 补充业务字段与回执
        NormalPayOrder normalOrder = normalPayOrderManager.findById(entity.getContainerId()).orElse(null);
        if (Objects.nonNull(normalOrder)) {
            result.setBizOrderNo(normalOrder.getBizOrderNo());
            result.setTitle(normalOrder.getTitle());
            result.setContainerStatus(normalOrder.getStatus());
            result.setProduct(normalOrder.getProduct());
            result.setChannel(normalOrder.getChannel());
            result.setMethod(normalOrder.getMethod());
            result.setChannelAppId(normalOrder.getChannelAppId());
            result.setBuyerId(normalOrder.getBuyerId());
            result.setOpenid(normalOrder.getOpenid());
            result.setAuthCode(normalOrder.getAuthCode());
            result.setTradeProduct(normalOrder.getTradeProduct());
            result.setTradeWay(normalOrder.getTradeWay());
            result.setBankType(normalOrder.getBankType());
            result.setErrorMsg(normalOrder.getErrorMsg());
        }
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
