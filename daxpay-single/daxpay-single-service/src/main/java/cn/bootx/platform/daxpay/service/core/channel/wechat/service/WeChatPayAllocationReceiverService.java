package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.WeChatPayCode;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationReceiver;
import cn.hutool.core.util.StrUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.ReceiverModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cn.bootx.platform.daxpay.code.AllocationReceiverTypeEnum.WX_MERCHANT;

/**
 *
 * @author xxm
 * @since 2024/4/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayAllocationReceiverService {

    /**
     * 校验参数是否合法
     */
    public boolean validation(AllocationReceiver allocationReceiver){
        List<String> list = Arrays.asList(WX_MERCHANT.getOutCode(), WX_MERCHANT.getOutCode());
        String receiverType = allocationReceiver.getReceiverType();
        return !list.contains(receiverType);
    }

    /**
     * 绑定
     */
    public void bind(AllocationReceiver allocationReceiver, WeChatPayConfig weChatPayConfig){

        Map<String, String> param = ReceiverModel.builder()
                .type(allocationReceiver.getReceiverType())
                .account(allocationReceiver.getReceiverAccount())
                .name(allocationReceiver.getReceiverName())
                .relation_type(allocationReceiver.getRelationType())
                .custom_relation(allocationReceiver.getRelationType())
                .custom_relation(allocationReceiver.getRelationName())
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.profitSharingAddReceiver(param);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
    }

    /**
     * 解除绑定
     */
    public void unbind(AllocationReceiver allocationReceiver, WeChatPayConfig weChatPayConfig){
        Map<String, String> param = ReceiverModel.builder()
                .type(allocationReceiver.getReceiverType())
                .account(allocationReceiver.getReceiverAccount())
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.profitSharingRemoveReceiver(param);
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
            log.error("支付失败 {}", errorMsg);
            throw new PayFailureException(errorMsg);
        }
    }
}
