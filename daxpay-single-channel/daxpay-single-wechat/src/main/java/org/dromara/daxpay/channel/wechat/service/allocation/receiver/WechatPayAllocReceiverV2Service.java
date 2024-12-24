package org.dromara.daxpay.channel.wechat.service.allocation.receiver;

import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.exception.ConfigErrorException;
import org.dromara.daxpay.core.exception.OperationFailException;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;
import com.github.binarywang.wxpay.bean.profitsharing.Receiver;
import com.github.binarywang.wxpay.bean.profitsharing.request.ProfitSharingReceiverRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.ProfitSharingService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

import static org.dromara.daxpay.core.enums.AllocReceiverTypeEnum.MERCHANT_NO;
import static org.dromara.daxpay.core.enums.AllocReceiverTypeEnum.OPEN_ID;

/**
 * 微信分账接收方服务
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayAllocReceiverV2Service {

    private final WechatPayConfigService wechatPayConfigService;

    private static final Gson GSON = new GsonBuilder().create();

    /**
     * 绑定
     */
    public void bind(AllocReceiver allocReceiver, WechatPayConfig config){
        AllocReceiverTypeEnum receiverTypeEnum = AllocReceiverTypeEnum.findByCode(allocReceiver.getReceiverType());
        String receiverType = this.getReceiverType(receiverTypeEnum);

        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();

        ProfitSharingReceiverRequest request = new ProfitSharingReceiverRequest();
        Receiver receiver = new Receiver(receiverType,allocReceiver.getReceiverAccount(),allocReceiver.getReceiverName(),getRelationType(allocReceiver.getRelationType()),allocReceiver.getRelationName());
        request.setReceiver(GSON.toJson(receiver));

        try {
            sharingService.addReceiver(request);
        } catch (WxPayException e) {
            throw new OperationFailException("微信添加分账方V2失败: "+e.getMessage());
        }
    }

    /**
     * 解除绑定
     */
    public void unbind(AllocReceiver allocReceiver, WechatPayConfig config){
        AllocReceiverTypeEnum receiverTypeEnum = AllocReceiverTypeEnum.findByCode(allocReceiver.getReceiverType());
        String receiverType = this.getReceiverType(receiverTypeEnum);

        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        ProfitSharingService sharingService = wxPayService.getProfitSharingService();

        ProfitSharingReceiverRequest request = new ProfitSharingReceiverRequest();
        Receiver receiver = new Receiver(receiverType,allocReceiver.getReceiverAccount());
        request.setReceiver(GSON.toJson(receiver));
        try {
            sharingService.removeReceiver(request);
        } catch (WxPayException e) {
            throw new OperationFailException("微信删除分账方V2失败: "+e.getMessage());
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

    /**
     * 获取分账关系类型编码
     */
    private String getRelationType(String relationType){
        if (Objects.isNull(relationType)){
            return null;
        }
        return relationType.toUpperCase(Locale.ROOT);
    }
}
