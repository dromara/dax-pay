package cn.bootx.platform.daxpay.service.func;

import cn.bootx.platform.daxpay.service.code.PayCallbackStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairSourceEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.platform.daxpay.service.common.context.CallbackLocal;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.payment.callback.service.PayCallbackService;
import cn.bootx.platform.daxpay.service.core.payment.callback.service.PayRefundCallbackService;
import cn.bootx.platform.daxpay.service.core.record.callback.entity.PayCallbackRecord;
import cn.bootx.platform.daxpay.service.core.record.callback.service.PayCallbackRecordService;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 回调处理抽象类, 处理支付回调和退款回调
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
public abstract class AbsCallbackStrategy implements PayStrategy {
    @Resource
    private PayCallbackRecordService callbackRecordService;
    @Resource
    private PayCallbackService payCallbackService;
    @Resource
    private PayRefundCallbackService refundCallbackService;

    /**
     * 回调处理入口
     * TODO 需要处理异常情况进行保存
     */
    public String callback(Map<String, String> params) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        try {
            // 将参数写入到上下文中
            callbackInfo.getCallbackParam().putAll(params);

            // 判断并保存回调类型
            PaymentTypeEnum callbackType = this.getCallbackType();
            callbackInfo.setCallbackType(callbackType);

            // 验证消息
            if (!this.verifyNotify()) {
                callbackInfo.setCallbackStatus(PayCallbackStatusEnum.FAIL).setMsg("验证信息格式不通过");
                // 消息有问题, 保存记录并返回
                this.saveCallbackRecord();
                return null;
            }
            // 提前设置订单修复的来源
            PaymentContextLocal.get().getRepairInfo().setSource(PayRepairSourceEnum.CALLBACK);

            if (callbackType == PaymentTypeEnum.PAY){
                // 解析支付数据并放处理
                this.resolvePayData();
                payCallbackService.payCallback();
            } else {
                // 解析退款数据并放处理
                this.resolveRefundData();
                refundCallbackService.refundCallback();
            }
            this.saveCallbackRecord();
            return this.getReturnMsg();
        } catch (Exception e) {
            log.error("回调处理失败", e);
            callbackInfo.setCallbackStatus(PayCallbackStatusEnum.FAIL).setMsg("回调处理失败: "+e.getMessage());
            this.saveCallbackRecord();
            throw e;
        }
    }

    /**
     * 验证信息格式
     */
    public abstract boolean verifyNotify();

    /**
     * 判断类型 支付回调/退款回调
     * @see PaymentTypeEnum
     */
    public abstract PaymentTypeEnum getCallbackType();

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
                .setChannel(this.getChannel().getCode())
                .setNotifyInfo(JSONUtil.toJsonStr(callbackInfo.getCallbackParam()))
                .setOrderId(callbackInfo.getOrderId())
                .setGatewayOrderNo(callbackInfo.getGatewayOrderNo())
                .setCallbackType(callbackInfo.getCallbackType().getCode())
                .setRepairOrderNo(callbackInfo.getPayRepairNo())
                .setStatus(callbackInfo.getCallbackStatus().getCode())
                .setMsg(callbackInfo.getMsg());
        callbackRecordService.save(payNotifyRecord);
    }
}
