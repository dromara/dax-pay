package org.dromara.daxpay.channel.wechat.service.sync.transfer;

import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.core.enums.TransferStatusEnum;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.sync.TransferSyncResultBo;
import org.dromara.daxpay.service.entity.order.transfer.TransferOrder;
import com.github.binarywang.wxpay.bean.merchanttransfer.MerchantDetailsQueryRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.MerchantTransferService;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信转账订单同步
 * @author xxm
 * @since 2024/7/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatTransferSyncV3Service {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 同步
     */
    public TransferSyncResultBo sync(TransferOrder transferOrder, WechatPayConfig wechatPayConfig) {
        TransferSyncResultBo syncResult = new TransferSyncResultBo();

        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(wechatPayConfig);
        MerchantTransferService merchantTransferService = wxPayService.getMerchantTransferService();

        MerchantDetailsQueryRequest request = new MerchantDetailsQueryRequest();
        request.setOutBatchNo(transferOrder.getTransferNo())
                .setOutDetailNo(transferOrder.getTransferNo());
        try {
            var result = merchantTransferService.queryMerchantDetails(request);

            syncResult.setOutTransferNo(result.getBatchId())
                    .setAmount(PayUtil.conversionAmount(result.getTransferAmount()));

            // 成功 SUCCESS:转账成功
            if ("SUCCESS".equals(result.getDetailStatus())){
                syncResult.setTransferStatus(TransferStatusEnum.SUCCESS);
            }
            // FAIL:转账失败。需要确认失败原因后，再决定是否重新发起对该笔明细单的转账（并非整个转账批次单）
            if ("FAIL".equals(result.getDetailStatus())){
                syncResult.setTransferStatus(TransferStatusEnum.FAIL);
            }
            // INIT: 初始态。 系统转账校验中 WAIT_PAY: 待确认。待商户确认, 符合免密条件时, 系统会自动扭转为转账中
            // PROCESSING:转账中。正在处理中，转账结果尚未明确
            if (List.of("INIT", "WAIT_PAY", "PROCESSING").contains(result.getDetailStatus())){
                syncResult.setTransferStatus(TransferStatusEnum.PROGRESS);
            }

        } catch (WxPayException e) {
            log.error("微信转账订单查询V3失败", e);
            syncResult.setSyncErrorMsg(e.getCustomErrorMsg()).setSyncSuccess(false);
        }
        return syncResult;
    }
}
