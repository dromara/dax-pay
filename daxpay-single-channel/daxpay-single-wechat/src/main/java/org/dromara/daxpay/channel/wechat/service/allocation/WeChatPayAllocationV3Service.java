package org.dromara.daxpay.channel.wechat.service.allocation;

import com.github.binarywang.wxpay.bean.profitsharing.request.ProfitSharingUnfreezeV3Request;
import com.github.binarywang.wxpay.bean.profitsharing.request.ProfitSharingV3Request;
import com.github.binarywang.wxpay.bean.profitsharing.result.ProfitSharingV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.ProfitSharingService;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.exception.ConfigErrorException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.bo.allocation.AllocStartResultBo;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocOrder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.dromara.daxpay.core.enums.AllocReceiverTypeEnum.MERCHANT_NO;
import static org.dromara.daxpay.core.enums.AllocReceiverTypeEnum.OPEN_ID;

/**
 * 微信分账V3版本接口
 * @author xxm
 * @since 2024/12/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayAllocationV3Service {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 发起分账
     */
    public AllocStartResultBo start(AllocOrder allocOrder, List<AllocDetail> orderDetails, WechatPayConfig config){

        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();

        ProfitSharingV3Request request = new ProfitSharingV3Request();
        request.setOutOrderNo(allocOrder.getAllocNo());
        request.setTransactionId(allocOrder.getOutOrderNo());
        request.setUnfreezeUnsplit(true);

        // 分账接收方信息
        var receivers = orderDetails.stream()
                .map(o -> {
                    AllocReceiverTypeEnum receiverTypeEnum = AllocReceiverTypeEnum.findByCode(o.getReceiverType());
                    String receiverType = this.getReceiverType(receiverTypeEnum);
                    var receiver = new ProfitSharingV3Request.Receiver();
                    receiver.setType(receiverType);
                    receiver.setAmount(PayUtil.convertCentAmount(o.getAmount()));
                    receiver.setAccount(o.getReceiverAccount());
                    receiver.setName(o.getReceiverName());
                    receiver.setDescription("订单分账");
                    receiver.setRelationType("PARTNER");
                    return receiver;
                })
                .toList();

        request.setReceivers(receivers);
        try {
            ProfitSharingV3Result result = sharingService.profitSharingV3(request);

            return new AllocStartResultBo().setOutAllocNo(result.getOrderId());
        } catch (WxPayException e) {
            throw new OperationFailException("微信分账V3失败: "+e.getMessage());
        }
    }

    /**
     * 分账完结
     */
    public void finish(AllocOrder allocOrder, List<AllocDetail> orderDetails, WechatPayConfig config){
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();
        var request = new ProfitSharingUnfreezeV3Request();
        request.setOutOrderNo(allocOrder.getAllocNo());
        request.setTransactionId(allocOrder.getOutOrderNo());
        request.setDescription("分账完结");
        try {
            sharingService.profitSharingUnfreeze(request);
        } catch (WxPayException e) {
            throw new OperationFailException("微信分账V3失败: "+e.getMessage());
        }
    }

    /**
     * 获取分账接收方类型编码
     */
    private String getReceiverType(AllocReceiverTypeEnum receiverTypeEnum){
        if (receiverTypeEnum == OPEN_ID){
            return "PERSONAL_OPENID";
        }
        if (receiverTypeEnum == MERCHANT_NO){
            return "MERCHANT_ID";
        }
        throw new ConfigErrorException("分账接收方类型错误");
    }
}
