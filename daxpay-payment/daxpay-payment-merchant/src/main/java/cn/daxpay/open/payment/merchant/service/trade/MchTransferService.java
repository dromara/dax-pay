package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.trade.transfer.convert.AlipayTransferOrderConvert;
import cn.daxpay.open.payment.trade.transfer.convert.DouyinTransferOrderConvert;
import cn.daxpay.open.payment.trade.transfer.convert.TransferTradeConvert;
import cn.daxpay.open.payment.trade.transfer.convert.WechatTransferOrderConvert;
import cn.daxpay.open.payment.trade.transfer.dao.AlipayTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.dao.DouyinTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.dao.TransferTradeManager;
import cn.daxpay.open.payment.trade.transfer.dao.WechatTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.entity.AlipayTransferOrder;
import cn.daxpay.open.payment.trade.transfer.entity.DouyinTransferOrder;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.transfer.entity.WechatTransferOrder;
import cn.daxpay.open.payment.trade.transfer.param.AlipayTransferOrderQuery;
import cn.daxpay.open.payment.trade.transfer.param.DouyinTransferOrderQuery;
import cn.daxpay.open.payment.trade.transfer.param.TransferParam;
import cn.daxpay.open.payment.trade.transfer.param.TransferTradeQuery;
import cn.daxpay.open.payment.trade.transfer.param.WechatTransferOrderQuery;
import cn.daxpay.open.payment.trade.transfer.result.AlipayTransferOrderResult;
import cn.daxpay.open.payment.trade.transfer.result.DouyinTransferOrderResult;
import cn.daxpay.open.payment.trade.transfer.result.TransferTradeResult;
import cn.daxpay.open.payment.trade.transfer.result.WechatTransferOrderResult;
import cn.daxpay.open.payment.trade.transfer.runtime.service.TransferCloseService;
import cn.daxpay.open.payment.trade.transfer.runtime.service.TransferStartService;
import cn.daxpay.open.payment.trade.transfer.runtime.service.TransferSyncService;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 转账单管理服务(商户端)
///
/// 商户端专属。强制当前商户隔离(租户过滤由 Manager 完成), 发起转账商户号由登录上下文决定。
@Service
@RequiredArgsConstructor
public class MchTransferService {

    private final WechatTransferOrderManager wechatTransferOrderManager;
    private final AlipayTransferOrderManager alipayTransferOrderManager;
    private final DouyinTransferOrderManager douyinTransferOrderManager;
    private final TransferTradeManager transferTradeManager;
    private final TransferStartService transferStartService;
    private final TransferSyncService transferSyncService;
    private final TransferCloseService transferCloseService;
    private final TransService transService;

    /// 微信转账单分页
    public PageResult<WechatTransferOrderResult> wechatPage(PageParam pageParam, WechatTransferOrderQuery query) {
        Page<WechatTransferOrder> page = wechatTransferOrderManager.page(pageParam, query);
        PageResult<WechatTransferOrderResult> pageResult = toPageResult(page, WechatTransferOrderConvert.CONVERT::toResult);
        transService.translate(pageResult);
        return pageResult;
    }

    /// 微信转账单详情
    public WechatTransferOrderResult wechatFindById(Long id) {
        WechatTransferOrder entity = wechatTransferOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.transfer.notFound"));
        WechatTransferOrderResult result = WechatTransferOrderConvert.CONVERT.toResult(entity);
        transService.translate(result);
        return result;
    }

    /// 支付宝转账单分页
    public PageResult<AlipayTransferOrderResult> alipayPage(PageParam pageParam, AlipayTransferOrderQuery query) {
        Page<AlipayTransferOrder> page = alipayTransferOrderManager.page(pageParam, query);
        PageResult<AlipayTransferOrderResult> pageResult = toPageResult(page, AlipayTransferOrderConvert.CONVERT::toResult);
        transService.translate(pageResult);
        return pageResult;
    }

    /// 支付宝转账单详情
    public AlipayTransferOrderResult alipayFindById(Long id) {
        AlipayTransferOrder entity = alipayTransferOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.transfer.notFound"));
        AlipayTransferOrderResult result = AlipayTransferOrderConvert.CONVERT.toResult(entity);
        transService.translate(result);
        return result;
    }

    /// 抖音转账单分页
    public PageResult<DouyinTransferOrderResult> douyinPage(PageParam pageParam, DouyinTransferOrderQuery query) {
        Page<DouyinTransferOrder> page = douyinTransferOrderManager.page(pageParam, query);
        PageResult<DouyinTransferOrderResult> pageResult = toPageResult(page, DouyinTransferOrderConvert.CONVERT::toResult);
        transService.translate(pageResult);
        return pageResult;
    }

    /// 抖音转账单详情
    public DouyinTransferOrderResult douyinFindById(Long id) {
        DouyinTransferOrder entity = douyinTransferOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.transfer.notFound"));
        DouyinTransferOrderResult result = DouyinTransferOrderConvert.CONVERT.toResult(entity);
        transService.translate(result);
        return result;
    }

    /// 发起转账(商户号由登录上下文强制)
    public void create(String channel, TransferParam param) {
        transferStartService.start(channel, param);
    }

    /// 同步转账状态
    public void sync(String channel, Long id) {
        transferSyncService.sync(channel, id);
    }

    /// 关闭转账(仅通道支持场景有效)
    public void close(String channel, Long id) {
        transferCloseService.close(channel, id);
    }

    /// 转账记录分页(跨通道)
    public PageResult<TransferTradeResult> tradePage(PageParam pageParam, TransferTradeQuery query) {
        Page<TransferTrade> page = transferTradeManager.page(pageParam, query);
        PageResult<TransferTradeResult> pageResult = toPageResult(page, TransferTradeConvert.CONVERT::toResult);
        transService.translate(pageResult);
        return pageResult;
    }

    /// 转账记录详情
    public TransferTradeResult tradeFindById(Long id) {
        TransferTrade entity = transferTradeManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.transfer.notFound"));
        TransferTradeResult result = TransferTradeConvert.CONVERT.toResult(entity);
        transService.translate(result);
        return result;
    }

    /// 分页实体 → 分页结果
    private <T, R> PageResult<R> toPageResult(Page<T> page, java.util.function.Function<T, R> mapper) {
        var records = page.getRecords().stream().map(mapper).toList();
        return new PageResult<R>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }
}
