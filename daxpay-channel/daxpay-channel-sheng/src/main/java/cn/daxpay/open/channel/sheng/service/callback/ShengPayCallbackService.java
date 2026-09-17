package cn.daxpay.open.channel.sheng.service.callback;

import cn.daxpay.open.channel.sheng.client.ShengChannelClient;
import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.client.req.ShengCallbackParseReq;
import cn.daxpay.open.channel.sheng.client.resp.ShengCallbackParseResp;
import cn.daxpay.open.channel.sheng.dao.ShengIsvChannelMerchantManager;
import cn.daxpay.open.channel.sheng.dao.ShengIsvKeyConfigManager;
import cn.daxpay.open.channel.sheng.dao.ShengKeyConfigManager;
import cn.daxpay.open.channel.sheng.entity.ShengIsvChannelMerchant;
import cn.daxpay.open.channel.sheng.entity.ShengIsvKeyConfig;
import cn.daxpay.open.channel.sheng.entity.ShengKeyConfig;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/// # 盛付通支付回调处理服务
///
/// 盛付通异步通知(JSON) → 主应用接收原始 body → 携凭证转发子应用验签与解析
/// (SHA1withRSA + 盛付通公钥) →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 验签凭证按产品分流([#resolveVerifyCredential]): 通过通道商户号反查通用通道商户主表的产品编码,
/// sheng_isv(服务商模式)走产品级全局密钥 + 子商户绑定行, 其余(商户模式 sheng_pay)走通道商户维度密钥。
/// 凭 out_trade_no 反查 PayTrade。主应用零加密代码, 验签/解析集中在子应用 dax-pay-channel-two。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengPayCallbackService {

    private static final String NOTIFY_SUCCESS = "success";
    private static final String NOTIFY_FAIL = "fail";

    private final ShengChannelClient shengChannelClient;
    private final ShengKeyConfigManager shengKeyConfigManager;
    private final ShengIsvKeyConfigManager shengIsvKeyConfigManager;
    private final ShengIsvChannelMerchantManager shengIsvChannelMerchantManager;
    private final ChannelMerchantManager channelMerchantManager;
    private final PayCallbackService payCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 支付回调处理
    public String payHandle(String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);

        // 2. 按产品分流解析验签凭证(服务商模式走产品级密钥+绑定行, 商户模式走通道商户密钥, 只读查询)
        ShengSdkCredential credential = resolveVerifyCredential(channelMchNo).orElse(null);
        if (Objects.isNull(credential) || Objects.isNull(credential.getShengpayPublicKey())) {
            log.error("盛付通支付回调: 通道密钥未配置, 无法验签, channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("盛付通支付回调: 通道密钥未配置");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        // 3. 转发子应用验签解析
        ShengCallbackParseResp resp = parse(body, credential, false);
        if (Objects.isNull(resp) || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("盛付通支付回调验签失败");
            CallbackData failData = new CallbackData();
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("盛付通支付回调验签失败");
            payCallbackRecordService.savePay(channelMchNo, failData);
            return NOTIFY_FAIL;
        }

        // 4. 构建 CallbackData 交框架更新订单状态
        CallbackData callbackData = new CallbackData();
        callbackData.setTradeNo(resp.getOutTradeNo());
        callbackData.setOutTradeNo(resp.getTradeNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setCallbackErrorMsg("盛付通回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        notify.put("outTradeNo", resp.getOutTradeNo());
        notify.put("tradeNo", resp.getTradeNo());
        notify.put("amount", resp.getAmount());
        notify.put("tradeStatus", resp.getTradeStatus());
        callbackData.setCallbackData(notify);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("盛付通支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.savePay(channelMchNo, callbackData);
            return NOTIFY_FAIL;
        }
        payCallbackRecordService.savePay(channelMchNo, callbackData);
        return NOTIFY_SUCCESS;
    }

    /// 按产品分流解析回调验签凭证(支付/退款回调共用)
    ///
    /// 通过通道商户号反查通用通道商户主表的产品编码分流:
    /// - sheng_isv(服务商模式): 产品级全局密钥(sheng_isv_key_config) + 子商户绑定行(sheng_isv_channel_merchant)合并组装;
    /// - 其余(商户模式 sheng_pay): 通道商户维度密钥(sheng_key_config)直接组装(不携带子商户号)。
    ///
    /// 凭证来源任一缺失或验签公钥为空时返回空 Optional, 调用方走"密钥未配置"失败分支。
    Optional<ShengSdkCredential> resolveVerifyCredential(String channelMchNo) {
        String product = channelMerchantManager.findProductByChannelMchNo(channelMchNo);
        if (ProductEnum.SHENG_ISV.getCode().equals(product)) {
            // 服务商模式: 产品级密钥 + 绑定行, 任一缺失视为未配置
            ShengIsvKeyConfig keyConfig = shengIsvKeyConfigManager.findByProduct(ProductEnum.SHENG_ISV.getCode())
                    .orElse(null);
            ShengIsvChannelMerchant bindRow = shengIsvChannelMerchantManager.findByChannelMchNo(channelMchNo)
                    .orElse(null);
            if (Objects.isNull(keyConfig) || Objects.isNull(bindRow) || Objects.isNull(keyConfig.getShengpayPublicKey())) {
                return Optional.empty();
            }
            // 组装凭证(服务商身份与密钥 + 子商户身份)
            ShengSdkCredential credential = new ShengSdkCredential();
            credential.setMchId(keyConfig.getShengMchId());
            credential.setSubMchId(bindRow.getSubMchId());
            credential.setSdpAppId(bindRow.getSdpAppId());
            credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
            credential.setShengpayPublicKey(keyConfig.getShengpayPublicKey());
            return Optional.of(credential);
        }
        // 商户模式: 通道商户维度密钥(子商户号字段已从密钥表移除, 凭证不再携带)
        ShengKeyConfig keyConfig = shengKeyConfigManager.findByChannelMchNo(channelMchNo)
                .orElse(null);
        if (Objects.isNull(keyConfig) || Objects.isNull(keyConfig.getShengpayPublicKey())) {
            return Optional.empty();
        }
        // 组装凭证(验签只需盛付通公钥, 其余字段一并下发保持凭证完整)
        ShengSdkCredential credential = new ShengSdkCredential();
        credential.setMchId(keyConfig.getShengMchId());
        credential.setSdpAppId(keyConfig.getSdpAppId());
        credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
        credential.setShengpayPublicKey(keyConfig.getShengpayPublicKey());
        return Optional.of(credential);
    }

    /// 转发子应用验签解析(支付/退款共用)
    ///
    /// 凭证由 [#resolveVerifyCredential] 按产品分流组装后传入,
    /// 验签只需盛付通公钥, 其余字段一并下发保持凭证完整。
    ShengCallbackParseResp parse(String rawBody, ShengSdkCredential credential, boolean refund) {
        ShengCallbackParseReq req = new ShengCallbackParseReq();
        req.setCredential(credential);
        req.setRawBody(rawBody);
        var result = refund
                ? shengChannelClient.parseRefundCallback(req)
                : shengChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("盛付通回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }
}
