package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayCallbackTypeEnum;
import cn.bootx.platform.daxpay.service.common.context.CallbackLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.func.AbsCallbackStrategy;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.bootx.platform.daxpay.service.code.WeChatPayCode.*;


/**
 * 微信支付回调
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
@Service
public class WeChatPayCallbackService extends AbsCallbackStrategy {
    @Resource
    private WeChatPayConfigService weChatPayConfigService;


    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 验证回调消息
     */
    @Override
    public boolean verifyNotify() {
        Map<String, String> params = PaymentContextLocal.get().getCallbackInfo().getCallbackParam();
        String callReq = JSONUtil.toJsonStr(params);
        log.info("微信发起回调 报文: {}", callReq);
        String appId = params.get(APPID);

        if (StrUtil.isBlank(appId)) {
            log.warn("微信回调报文 appId 为空");
            return false;
        }

        // 退款回调不用进行校验
        PayCallbackTypeEnum callbackType = this.getCallbackType();
        if (callbackType == PayCallbackTypeEnum.REFUND){
            return true;
        }
        // 支付回调信息校验
        WeChatPayConfig weChatPayConfig = weChatPayConfigService.getConfig();
        if (Objects.isNull(weChatPayConfig)) {
            log.warn("微信支付配置不存在");
            return false;
        }
        return WxPayKit.verifyNotify(params, weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256, null);
    }

    /**
     * 解析支付数据放到上下文中
     */
    @Override
    public void resolvePayData() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callbackInfo.getCallbackParam();

        // 网关订单号
        callbackInfo.setGatewayOrderNo(callbackParam.get(TRANSACTION_ID));
        // 支付订单ID
        callbackInfo.setOrderId(Long.valueOf(callbackParam.get(OUT_TRADE_NO)));
        // 支付状态
        PayStatusEnum payStatus = WxPayKit.codeIsOk(callbackParam.get(RESULT_CODE)) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        callbackInfo.setGatewayStatus(payStatus.getCode());
        // 支付金额
        callbackInfo.setAmount(callbackParam.get(TOTAL_FEE));
        String timeEnd = callbackParam.get(TIME_END);
        if (StrUtil.isNotBlank(timeEnd)) {
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
            callbackInfo.setFinishTime(time);
        } else {
            callbackInfo.setFinishTime(LocalDateTime.now());
        }
    }

    /**
     * 解析退款回调数据并放到上下文中, 微信退款通知返回的数据需要进行解密
     */
    @Override
    public void resolveRefundData() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callbackInfo.getCallbackParam();

        // 解密返回数据
        WeChatPayConfig weChatPayConfig = weChatPayConfigService.getConfig();
        String reqInfo = callbackParam.get(REQ_INFO);
        String decryptData = WxPayKit.decryptData(reqInfo,weChatPayConfig.getApiKeyV2());

        // 同时覆盖上下文原有的的回调内容
        callbackParam = WxPayKit.xmlToMap(decryptData);
        callbackInfo.setCallbackParam(callbackParam);
        // 网关订单号
        callbackInfo.setGatewayOrderNo(callbackParam.get(REFUND_ID));
        // 退款订单Id
        callbackInfo.setOrderId(Long.valueOf(callbackParam.get(OUT_REFUND_NO)));
        // 退款金额
        callbackInfo.setAmount(callbackParam.get(REFUND_FEE));

        // 交易状态
        PayStatusEnum payStatus = Objects.equals(callbackParam.get(REFUND_STATUS), REFUND_USERPAYING) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        callbackInfo.setGatewayStatus(payStatus.getCode());

        // 退款时间
        String timeEnd = callbackParam.get(SUCCESS_TIME);
        if (StrUtil.isNotBlank(timeEnd)) {
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.NORM_DATETIME_PATTERN);
            callbackInfo.setFinishTime(time);
        } else {
            callbackInfo.setFinishTime(LocalDateTime.now());
        }
    }

    /**
     * 判断类型 支付回调/退款回调
     *
     * @see PayCallbackTypeEnum
     */
    @Override
    public PayCallbackTypeEnum getCallbackType() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callbackInfo.getCallbackParam();
        if (StrUtil.isNotBlank(callbackParam.get(REQ_INFO))){
            return PayCallbackTypeEnum.REFUND;
        } else {
            return PayCallbackTypeEnum.PAY;
        }
    }

    /**
     * 返回响应结果
     */
    @Override
    public String getReturnMsg() {
        Map<String, String> xml = new HashMap<>(4);
        xml.put(RETURN_CODE, "SUCCESS");
        xml.put(RETURN_MSG, "OK");
        return WxPayKit.toXml(xml);
    }
}
