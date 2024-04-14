package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.daxpay.code.AllocationReceiverTypeEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.WeChatPayCode;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrder;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrderDetail;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.ProfitSharingModel;
import com.ijpay.wxpay.model.ReceiverModel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 微信分账服务
 * @author xxm
 * @since 2024/4/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPayAllocationService {

    /**
     * 发起分账
     */
    @SneakyThrows
    public void allocation(AllocationOrder allocationOrder, List<AllocationOrderDetail> orderDetails, WeChatPayConfig config){
        String description = allocationOrder.getDescription();
        if (StrUtil.isBlank(description)){
            description = "分账";
        }
        String finalDescription = description;
        List<ReceiverModel> list = orderDetails.stream().map(o->{
            AllocationReceiverTypeEnum receiverTypeEnum = AllocationReceiverTypeEnum.findByCode(o.getReceiverType());
            return ReceiverModel.builder()
                    .type(receiverTypeEnum.getOutCode())
                    .account(o.getReceiverAccount())
                    .amount(o.getAmount())
                    .description(finalDescription)
                    .build();
        }).collect(Collectors.toList());

        Map<String, String> params = ProfitSharingModel.builder()
                .mch_id(config.getWxMchId())
                .appid(config.getWxAppId())
                .nonce_str(WxPayKit.generateStr())
                .transaction_id(allocationOrder.getGatewayPayOrderNo())
                .out_order_no(WxPayKit.generateStr())
                .receivers(JSON.toJSONString(list))
                .build()
                .createSign(config.getApiKeyV2(), SignType.HMACSHA256);

        byte[] fileBytes = Base64.decode(config.getP12());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
        String xmlResult = WxPayApi.multiProfitSharing(params, inputStream, config.getWxMchId());
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
    }

    /**
     * 完成分账
     */
    public void finish(AllocationOrder allocationOrder, WeChatPayConfig config){
        Map<String, String> params = ProfitSharingModel.builder()
                .mch_id(config.getWxMchId())
                .appid(config.getWxAppId())
                .nonce_str(WxPayKit.generateStr())
                .transaction_id(allocationOrder.getGatewayPayOrderNo())
                .out_order_no(allocationOrder.getOrderNo())
                .description("分账完成")
                .build()
                .createSign(config.getApiKeyV2(), SignType.HMACSHA256);

        byte[] fileBytes = Base64.decode(config.getP12());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
        String xmlResult = WxPayApi.profitSharingFinish(params, inputStream, config.getWxMchId());
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        this.verifyErrorMsg(result);
    }

    /**
     * 查询分账状态
     */
    public void queryStatus(AllocationOrder allocationOrder,  WeChatPayConfig config){
        // 不要传输AppId参数, 否则会失败
        Map<String, String> params = ProfitSharingModel.builder()
                .mch_id(config.getWxMchId())
                .nonce_str(WxPayKit.generateStr())
                .transaction_id(allocationOrder.getGatewayPayOrderNo())
                .out_order_no(allocationOrder.getOrderNo())
                .build()
                .createSign(config.getApiKeyV2(), SignType.HMACSHA256);
        String xmlResult = WxPayApi.profitSharingQuery(params);
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
            log.error("分账操作失败 {}", errorMsg);
            throw new PayFailureException(errorMsg);
        }
    }
}
