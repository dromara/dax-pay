package cn.bootx.platform.daxpay.core.pay.func;

import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.core.notify.dao.PayNotifyRecordManager;
import cn.bootx.platform.daxpay.core.notify.entity.PayNotifyRecord;
import cn.bootx.platform.daxpay.core.pay.result.PayCallbackResult;
import cn.bootx.platform.daxpay.core.pay.service.PayCallbackService;
import cn.hutool.json.JSONUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 支付回调处理抽象接口
 *
 * @author xxm
 * @date 2021/6/21
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbsPayCallbackStrategy {

    protected static final ThreadLocal<Map<String, String>> PARAMS = new TransmittableThreadLocal<>();

    private final RedisClient redisClient;

    private final PayNotifyRecordManager payNotifyRecordManager;

    private final PayCallbackService payCallbackService;

    /**
     * 支付回调
     */
    public String payCallback(String mchCode,String appCode, Map<String, String> params) {
        PARAMS.set(params);
        try {
            log.info("支付回调处理: {}", params);
            // 验证消息
            if (!this.verifyNotify(appCode)) {
                return null;
            }
            // 去重处理
            if (!this.duplicateChecker()) {
                return this.getReturnMsg();
            }
            // 调用统一回调处理
            PayCallbackResult result = payCallbackService.callback(this.getPaymentId(), this.getTradeStatus(),
                    params);
            // 记录回调记录
            this.saveNotifyRecord(mchCode,appCode, result);
        }
        finally {
            PARAMS.remove();
        }
        return this.getReturnMsg();
    }

    /**
     * 支付类型
     * @see PayChannelEnum
     */
    public abstract PayChannelEnum getPayChannel();

    /**
     * 去重处理
     */
    public boolean duplicateChecker() {
        // 判断10秒内是否已经回调处理
        String key = "payment:callback:duplicate:" + this.getPaymentId();
        return redisClient.setIfAbsent(key, "", 10 * 1000);
    }

    /**
     * 验证信息格式
     * @param mchAppCode 商户应用编码
     */
    public abstract boolean verifyNotify(String mchAppCode);

    /**
     * 获取paymentId
     */
    public abstract Long getPaymentId();

    /**
     * 获取支付状态
     * @see PayStatusCode
     */
    public abstract String getTradeStatus();

    /**
     * 返回响应结果
     */
    public abstract String getReturnMsg();

    /**
     * 保存回调记录
     */
    public void saveNotifyRecord(String mchCode,String appCode, PayCallbackResult result) {
        PayNotifyRecord payNotifyRecord = new PayNotifyRecord().setNotifyInfo(JSONUtil.toJsonStr(PARAMS.get()))
                .setNotifyTime(LocalDateTime.now())
                .setPaymentId(this.getPaymentId())
                .setMchAppCode(appCode)
                .setMchCode(mchCode)
                .setPayChannel(this.getPayChannel().getCode())
                .setStatus(result.getCode())
                .setMsg(result.getMsg());
        payNotifyRecordManager.save(payNotifyRecord);
    }

}
