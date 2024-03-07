package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.platform.daxpay.service.common.context.CallbackLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.func.AbsCallbackStrategy;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static cn.bootx.platform.daxpay.service.code.UnionPayCode.*;

/**
 * 云闪付回调处理
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayCallbackService extends AbsCallbackStrategy {

    @Resource
    private UnionPayConfigService unionPayConfigService;

    /**
     * 验证信息格式
     */
    @Override
    public boolean verifyNotify() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> params = callbackInfo.getCallbackParam();
        String callReq = JSONUtil.toJsonStr(params);
        log.info("云闪付发起回调 报文: {}", callReq);

        String status = params.get(STATUS);
        String returnCode = params.get(RESULT_CODE);

        // 处理失败
        if (!Objects.equals(SUCCESS, status)||!Objects.equals(SUCCESS, returnCode)){
            return false;
        }

        // 支付回调信息校验
        UnionPayConfig config = unionPayConfigService.getConfig();
        if (Objects.isNull(config)) {
            log.warn("云闪付支付配置不存在");
            return false;
        }

        // 注意此处签名方式需与统一下单的签名类型一致
        return WxPayKit.verifyNotify(params, config.getAppKey(), SignType.MD5);
    }

    /**
     * 判断类型 支付回调/退款回调, 云闪付只有支付回调
     *
     * @see PaymentTypeEnum
     */
    @Override
    public PaymentTypeEnum getCallbackType() {
        return PaymentTypeEnum.PAY;
    }

    /**
     * 解析支付回调数据并放到上下文中
     */
    @Override
    public void resolvePayData() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        Map<String, String> callbackParam = callbackInfo.getCallbackParam();

        // 网关订单号
        callbackInfo.setGatewayOrderNo(callbackParam.get(TRANSACTION_ID));
        // 支付订单ID
        callbackInfo.setOrderId(Long.valueOf(callbackParam.get(OUT_TRANSACTION_ID)));
        // 支付结果
        PayStatusEnum payStatus = WxPayKit.codeIsOk(callbackParam.get(PAY_RESULT)) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
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
     * 解析退款回调数据并放到上下文中
     */
    @Override
    public void resolveRefundData() {

    }

    /**
     * 返回响应结果
     */
    @Override
    public String getReturnMsg() {
        return "success";
    }

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.UNION_PAY;
    }
}
