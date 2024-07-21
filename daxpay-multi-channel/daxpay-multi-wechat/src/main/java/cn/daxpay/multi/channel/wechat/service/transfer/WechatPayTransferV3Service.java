package cn.daxpay.multi.channel.wechat.service.transfer;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.core.exception.TradeFailException;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateRequest;
import com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.MerchantTransferService;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class WechatPayTransferV3Service {

    private final WechatPayConfigService wechatPayConfigService;
    /**
     * 单笔转账转账接口
     */
    public void transfer(TransferOrder order, WechatPayConfig config) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        MerchantTransferService merchantTransferService = wxPayService.getMerchantTransferService();

        // 原因
        String reason = order.getReason();
        if (StrUtil.isBlank(reason)){
            reason = order.getTitle();
        }
        // 明细
        var transferDetail = new TransferCreateRequest.TransferDetailList();
        transferDetail.setOutDetailNo(order.getTransferNo())
                .setTransferAmount(PayUtil.convertCentAmount(order.getAmount()))
                .setTransferRemark(reason)
                .setOpenid(order.getPayeeAccount())
                .setUserName(order.getPayeeName());
        TransferCreateRequest request = new TransferCreateRequest()
                .setAppid(config.getWxAppId())
                .setBatchName(order.getTitle())
                .setOutBatchNo(order.getOutTransferNo())
                .setBatchRemark(reason)
                .setTotalAmount(PayUtil.convertCentAmount(order.getAmount()))
                .setTransferDetailList(Collections.singletonList(transferDetail));
        try {
            TransferCreateResult transfer = merchantTransferService.createTransfer(request);
            transfer.getBatchId();
        } catch (WxPayException e) {
            log.error("微信转账失败", e);
            throw new TradeFailException("微信转账失败: "+e.getErrCodeDes());
        }
    }
}
