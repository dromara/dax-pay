package cn.daxpay.single.service.core.channel.wechat.service;

import cn.daxpay.single.core.code.AllocReceiverTypeEnum;
import cn.daxpay.single.core.exception.OperationFailException;
import cn.daxpay.single.service.code.WeChatPayCode;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocReceiver;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.ProfitSharingModel;
import com.ijpay.wxpay.model.ReceiverModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cn.daxpay.single.core.code.AllocReceiverTypeEnum.WX_MERCHANT;
import static cn.daxpay.single.core.code.AllocReceiverTypeEnum.WX_PERSONAL;

/**
 *
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayAllocReceiverService {

    /**
     * 校验参数是否合法
     */
    public boolean validation(AllocReceiver allocReceiver){
        List<String> list = Arrays.asList(WX_PERSONAL.getCode(), WX_MERCHANT.getCode());
        String receiverType = allocReceiver.getReceiverType();
        return list.contains(receiverType);
    }

    /**
     * 绑定
     */
    public void bind(AllocReceiver allocReceiver, WeChatPayConfig weChatPayConfig){
        AllocReceiverTypeEnum receiverTypeEnum = AllocReceiverTypeEnum.findByCode(allocReceiver.getReceiverType());
        // 接收者参数
        ReceiverModel receiver = ReceiverModel.builder()
                .type(receiverTypeEnum.getOutCode())
                .account(allocReceiver.getReceiverAccount())
                .name(allocReceiver.getReceiverName())
                .relation_type(allocReceiver.getRelationType())
                .custom_relation(allocReceiver.getRelationName())
                .build();
        // 请求参数
        Map<String, String> params = ProfitSharingModel.builder()
                .mch_id(weChatPayConfig.getWxMchId())
                .appid(weChatPayConfig.getWxAppId())
                .nonce_str(WxPayKit.generateStr())
                .receiver(JSONUtil.toJsonStr(receiver))
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.profitSharingAddReceiver(params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
    }

    /**
     * 解除绑定
     */
    public void unbind(AllocReceiver allocReceiver, WeChatPayConfig weChatPayConfig){
        AllocReceiverTypeEnum receiverTypeEnum = AllocReceiverTypeEnum.findByCode(allocReceiver.getReceiverType());
        // 原始参数
        ReceiverModel receiver = ReceiverModel.builder()
                .type(receiverTypeEnum.getOutCode())
                .account(allocReceiver.getReceiverAccount())
                .name(allocReceiver.getReceiverName())
                .relation_type(allocReceiver.getRelationType())
                .custom_relation(allocReceiver.getRelationName())
                .build();

        // 原始参数
        Map<String, String> params = ProfitSharingModel.builder()
                .mch_id(weChatPayConfig.getWxMchId())
                .appid(weChatPayConfig.getWxAppId())
                .nonce_str(WxPayKit.generateStr())
                .receiver(JSONUtil.toJsonStr(receiver))
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.profitSharingRemoveReceiver(params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
    }

    /**
     * 验证错误信息
     */
    private void verifyErrorMsg(Map<String, String> result) {
        String returnCode = result.get(WeChatPayCode.RETURN_CODE);
        String resultCode = result.get(WeChatPayCode.RESULT_CODE);
        if (!WxPayKit.codeIsOk(returnCode) || !WxPayKit.codeIsOk(resultCode)) {
            String errorMsg = result.get(WeChatPayCode.ERR_CODE_DES);
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = result.get(WeChatPayCode.RETURN_MSG);
            }
            log.error("分账绑定或解绑失败 {}", errorMsg);
            throw new OperationFailException("分账绑定或解绑失败");
        }
    }
}
