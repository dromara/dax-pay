package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.code.PayCallbackStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayCallbackTypeEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.common.context.CallbackLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.payment.callback.service.PayCallbackService;
import cn.bootx.platform.daxpay.service.core.payment.callback.service.PayRefundCallbackService;
import cn.bootx.platform.daxpay.service.core.record.callback.entity.PayCallbackRecord;
import cn.bootx.platform.daxpay.service.core.record.callback.service.PayCallbackRecordService;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 支付回调处理抽象类
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
public abstract class AbsPayCallbackStrategy implements PayStrategy {
    @Resource
    private PayCallbackRecordService callbackRecordService;
    @Resource
    private PayCallbackService payCallbackService;
    @Resource
    private PayRefundCallbackService refundCallbackService;

    /**
     * 支付回调
     */
    public String callback(Map<String, String> params) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        callbackInfo.getCallbackParam().putAll(params);
        log.info("支付回调处理: {}", params);
        // 验证消息
        if (!this.verifyNotify()) {
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.FAIL).setMsg("验证信息格式不通过");
            // 消息有问题, 保存记录并返回
            this.saveCallbackRecord();
            return null;
        }
        PaymentContextLocal.get().getRepairInfo().setSource(PayRepairSourceEnum.CALLBACK);

        // 判断回调类型
        PayCallbackTypeEnum callbackType = this.getCallbackType();
        if (callbackType == PayCallbackTypeEnum.PAY){
            // 解析数据并放处理
            this.resolvePayData();
            payCallbackService.payCallback();
        } else {
            // 解析数据并放处理
            this.resolveRefundData();
            refundCallbackService.refundCallback();
        }
        // 记录回调记录
        this.saveCallbackRecord();
        return this.getReturnMsg();
    }

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    public abstract PayChannelEnum getPayChannel();

    /**
     * 验证信息格式
     */
    public abstract boolean verifyNotify();

    /**
     * 判断类型 支付回调/退款回调
     * @see PayCallbackTypeEnum
     */
    public abstract PayCallbackTypeEnum getCallbackType();

    /**
     * 解析支付回调数据并放到上下文中
     */
    public abstract void resolvePayData();

    /**
     * 解析退款回调数据并放到上下文中
     */
    public abstract void resolveRefundData();

    /**
     * 返回响应结果
     */
    public abstract String getReturnMsg();

    /**
     * 保存回调记录
     */
    public void saveCallbackRecord() {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        PayCallbackRecord payNotifyRecord = new PayCallbackRecord()
                .setPayChannel(this.getPayChannel().getCode())
                .setNotifyInfo(JSONUtil.toJsonStr(callbackInfo.getCallbackParam()))
                .setNotifyTime(LocalDateTime.now())
                .setOrderId(callbackInfo.getOrderId())
                .setGatewayOrderNo(callbackInfo.getGatewayOrderNo())
                .setRepairOrderId(callbackInfo.getPayRepairOrderId())
                .setStatus(callbackInfo.getCallbackStatus().getCode())
                .setMsg(callbackInfo.getMsg());
        callbackRecordService.save(payNotifyRecord);
    }
}
