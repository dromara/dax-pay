package cn.daxpay.open.payment.unipay.trade.service;

import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.transfer.dao.AlipayTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.dao.DouyinTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.dao.WechatTransferOrderManager;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.unipay.param.trade.transfer.TransferOrderQueryParam;
import cn.daxpay.open.payment.unipay.result.trade.transfer.TransferOrderResult;
import cn.daxpay.open.payment.unipay.trade.convert.UnipayTransferConvert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 转账订单查询服务(对外)
///
/// 纯查本地转账凭证, 不调用通道; 需要实时通道状态请走转账同步接口。
/// 定位与归属校验复用 [TransferOrderLocateService](平台转账单号 或 通道+商户转账号)。
/// 失败单会从通道转账单(容器)回填错误信息——公共凭证只保留资金固有属性, 错误信息权威在各通道容器。
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferOrderQueryService {

    private final TransferOrderLocateService transferOrderLocateService;
    private final WechatTransferOrderManager wechatTransferOrderManager;
    private final AlipayTransferOrderManager alipayTransferOrderManager;
    private final DouyinTransferOrderManager douyinTransferOrderManager;

    /// 查询转账订单
    public TransferOrderResult query(TransferOrderQueryParam param) {
        TransferTrade trade = transferOrderLocateService.locate(
                param.getMchNo(), param.getTransferNo(), param.getChannel(), param.getBizTransferNo());
        TransferOrderResult result = UnipayTransferConvert.CONVERT.toResult(trade);
        // 失败单回填通道容器上的错误信息
        if (PayFundStatusEnum.FAIL.getCode().equals(trade.getStatus())) {
            result.setErrorMsg(findContainerErrorMsg(trade));
        }
        return result;
    }

    /// 从通道转账单(容器)取错误信息(按容器通道分流)
    private String findContainerErrorMsg(TransferTrade trade) {
        return switch (trade.getContainerChannel()) {
            case "wechat" -> wechatTransferOrderManager.findById(trade.getContainerId())
                    .map(entity -> entity.getErrorMsg()).orElse(null);
            case "alipay" -> alipayTransferOrderManager.findById(trade.getContainerId())
                    .map(entity -> entity.getErrorMsg()).orElse(null);
            case "douyin" -> douyinTransferOrderManager.findById(trade.getContainerId())
                    .map(entity -> entity.getErrorMsg()).orElse(null);
            default -> null;
        };
    }
}
