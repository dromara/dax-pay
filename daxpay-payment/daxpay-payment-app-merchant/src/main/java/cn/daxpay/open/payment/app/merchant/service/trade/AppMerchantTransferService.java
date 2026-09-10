package cn.daxpay.open.payment.app.merchant.service.trade;

import cn.daxpay.open.payment.merchant.service.trade.MchTransferService;
import cn.daxpay.open.payment.trade.transfer.param.TransferTradeQuery;
import cn.daxpay.open.payment.trade.transfer.result.TransferTradeResult;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-转账单服务
///
/// 转发至 [MchTransferService]。仅镜像转账记录查询与同步/关闭操作, 不做发起转账。
@Service
@RequiredArgsConstructor
public class AppMerchantTransferService {

    private final MchTransferService mchTransferService;

    /// 转账记录分页(跨通道)
    public PageResult<TransferTradeResult> tradePage(PageParam pageParam, TransferTradeQuery query) {
        return mchTransferService.tradePage(pageParam, query);
    }

    /// 转账记录详情
    public TransferTradeResult tradeFindById(Long id) {
        return mchTransferService.tradeFindById(id);
    }

    /// 同步转账状态
    public void sync(String channel, Long id) {
        mchTransferService.sync(channel, id);
    }

    /// 关闭转账(仅通道支持场景有效)
    public void close(String channel, Long id) {
        mchTransferService.close(channel, id);
    }
}
