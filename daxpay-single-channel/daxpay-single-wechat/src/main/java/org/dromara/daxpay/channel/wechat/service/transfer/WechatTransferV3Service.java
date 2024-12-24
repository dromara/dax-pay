package org.dromara.daxpay.channel.wechat.service.transfer;

import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateRequest;
import com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.MerchantTransferService;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.param.transfer.TransferCreateV3Request;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.trade.TransferResultBo;
import org.dromara.daxpay.service.entity.order.transfer.TransferOrder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 微信转账到零钱 v3
 * @author xxm
 * @since 2024/6/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatTransferV3Service {

    private final WechatPayConfigService wechatPayConfigService;
    /**
     * 单笔转账转账接口
     * 微信转账是批量转账形式, 所以商家批次单号和商家明细单号使用同一个值
     */
    public TransferResultBo transfer(TransferOrder order, WechatPayConfig config) {
        TransferResultBo result = new TransferResultBo();

        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        MerchantTransferService merchantTransferService = wxPayService.getMerchantTransferService();

        // 原因
        String reason = order.getReason();
        if (StrUtil.isBlank(reason)){
            reason = order.getTitle();
        }
        // 明细
        var transferDetail = new TransferCreateRequest.TransferDetailList();
        transferDetail.setOutDetailNo(String.valueOf(order.getId()))
                .setTransferAmount(PayUtil.convertCentAmount(order.getAmount()))
                .setTransferRemark(reason)
                .setOpenid(order.getPayeeAccount())
                .setUserName(order.getPayeeName());
        // 设置回调地址
        var request = new TransferCreateV3Request()
                .setNotifyUrl(wechatPayConfigService.getTransferNotifyUrl(false));
        request.setAppid(config.getWxAppId())
                .setOutBatchNo(String.valueOf(order.getId()))
                .setBatchName(order.getTitle())
                .setBatchRemark(reason)
                .setTotalNum(1)
                .setTotalAmount(PayUtil.convertCentAmount(order.getAmount()))
                .setTransferDetailList(Collections.singletonList(transferDetail));
        try {
            TransferCreateResult transfer = merchantTransferService.createTransfer(request);
            result.setOutTransferNo(transfer.getBatchId());
        } catch (WxPayException e) {
            log.error("微信转账失败", e);
            throw new TradeFailException("微信转账失败: "+e.getErrCodeDes());
        }
        return result;
    }
}
