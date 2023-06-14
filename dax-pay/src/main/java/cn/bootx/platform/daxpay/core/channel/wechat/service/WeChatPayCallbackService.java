package cn.bootx.platform.daxpay.core.channel.wechat.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.code.paymodel.WeChatPayCode;
import cn.bootx.platform.daxpay.core.notify.dao.PayNotifyRecordManager;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayCallbackStrategy;
import cn.bootx.platform.daxpay.core.pay.service.PayCallbackService;
import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPayConfigManager;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static cn.bootx.platform.daxpay.code.paymodel.WeChatPayCode.APPID;

/**
 * 微信支付回调
 *
 * @author xxm
 * @date 2021/6/21
 */
@Slf4j
@Service
public class WeChatPayCallbackService extends AbsPayCallbackStrategy {

    private final WeChatPayConfigManager weChatPayConfigManager;

    public WeChatPayCallbackService(RedisClient redisClient, PayNotifyRecordManager payNotifyRecordManager,
            PayCallbackService payCallbackService, WeChatPayConfigManager weChatPayConfigManager) {
        super(redisClient, payNotifyRecordManager, payCallbackService);
        this.weChatPayConfigManager = weChatPayConfigManager;
    }

    @Override
    public PayChannelEnum getPayChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 获取支付单id
     */
    @Override
    public Long getPaymentId() {
        Map<String, String> params = PARAMS.get();
        String paymentId = params.get(WeChatPayCode.OUT_TRADE_NO);
        return Long.valueOf(paymentId);
    }

    /**
     * 获取支付状态
     */
    @Override
    public String getTradeStatus() {
        Map<String, String> params = PARAMS.get();
        if (WxPayKit.codeIsOk(params.get(WeChatPayCode.RESULT_CODE))) {
            return PayStatusCode.NOTIFY_TRADE_SUCCESS;
        }
        else {
            return PayStatusCode.NOTIFY_TRADE_FAIL;
        }
    }

    /**
     * 验证回调消息
     */
    @Override
    public boolean verifyNotify(String mchAppCode) {
        Map<String, String> params = PARAMS.get();
        String callReq = JSONUtil.toJsonStr(params);
        log.info("微信发起回调 报文: {}", callReq);
        String appId = params.get(APPID);

        if (StrUtil.isBlank(appId)) {
            log.warn("微信回调报文 appId 为空 {}", callReq);
            return false;
        }

        WeChatPayConfig weChatPayConfig = weChatPayConfigManager.findByMchAppCode(mchAppCode).orElseThrow(DataNotExistException::new);
        if (weChatPayConfig == null) {
            log.warn("微信支付配置不存在: {}", callReq);
            return false;
        }
        return WxPayKit.verifyNotify(params, weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256, null);
    }

    @Override
    public String getReturnMsg() {
        Map<String, String> xml = new HashMap<>(4);
        xml.put(WeChatPayCode.RETURN_CODE, "SUCCESS");
        xml.put(WeChatPayCode.RETURN_MSG, "OK");
        return WxPayKit.toXml(xml);
    }

}
