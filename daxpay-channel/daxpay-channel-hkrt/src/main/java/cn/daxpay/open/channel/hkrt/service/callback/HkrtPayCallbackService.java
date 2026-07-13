package cn.daxpay.open.channel.hkrt.service.callback;

import cn.daxpay.open.channel.hkrt.client.HkrtChannelClient;
import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.client.req.HkrtCallbackParseReq;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtCallbackParseResp;
import cn.daxpay.open.channel.hkrt.dao.isv.HkrtIsvKeyConfigManager;
import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvKeyConfig;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 海科融通支付回调处理服务
///
/// 海科融通异步通知 → 主应用接收原始 JSON body → 转发子应用验签与解析 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 验签与字段解析统一由子应用 dax-pay-channel-two 的 HkrtCallbackParseService 完成
/// (MD5 大写签名, 参数字母升序 + 末尾拼 accessKey), 主应用不再自验签。
/// 子应用返回抽象态 tradeStatus(SUCCESS/FAIL), 主应用映射为 CallbackStatusEnum 后交框架处理。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtPayCallbackService {

    private static final String NOTIFY_SUCCESS = "success";
    private static final String NOTIFY_FAIL = "fail";

    private final HkrtIsvKeyConfigManager hkrtIsvKeyConfigManager;
    private final PayCallbackService payCallbackService;
    private final HkrtChannelClient hkrtChannelClient;

    /// 支付回调处理
    public String payHandle(HttpServletRequest request) {
        // 1. 提取回调原始数据(JSON body)
        String body = JakartaServletUtil.getBody(request);

        // 2. 获取全局服务商 accessKey(只读查询, 不创建记录)
        HkrtIsvKeyConfig keyConfig = hkrtIsvKeyConfigManager.findByProduct(ProductEnum.HKRT_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || keyConfig.getAccessKey() == null) {
            log.error("海科融通支付回调: 服务商密钥未配置, 无法验签");
            return NOTIFY_FAIL;
        }

        // 3. 转发子应用验签与解析
        HkrtCallbackParseResp resp = parseCallback(body, keyConfig.getAccessKey(), false);
        if (resp == null) {
            return NOTIFY_FAIL;
        }
        try {
            // 4. 构建 CallbackData 交框架更新订单状态
            CallbackData callbackData = toCallbackData(resp);
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("海科融通支付回调业务处理失败: tradeNo={}", resp.getOutTradeNo(), e);
            return NOTIFY_FAIL;
        }
        return NOTIFY_SUCCESS;
    }

    /// 转发子应用验签解析(支付回调)
    private HkrtCallbackParseResp parseCallback(String body, String accessKey, boolean refund) {
        HkrtSdkCredential credential = new HkrtSdkCredential();
        credential.setAccessKey(accessKey);
        HkrtCallbackParseReq req = new HkrtCallbackParseReq();
        req.setCredential(credential);
        req.setRawData(body);
        req.setRefund(refund);
        var result = hkrtChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("海科融通支付回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        HkrtCallbackParseResp resp = result.getData();
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("海科融通支付回调验签失败");
            return null;
        }
        return resp;
    }

    /// 子应用解析结果 → CallbackData(抽象态 → 框架状态码)
    ///
    /// 框架 [PayCallbackService] 以 `CallbackStatusEnum.SUCCESS.getCode()`(success) 比对 tradeStatus:
    /// - SUCCESS → success(走成功分支)
    /// - 其他 → 不设 tradeStatus(走 fail 分支), 记错误信息
    private CallbackData toCallbackData(HkrtCallbackParseResp resp) {
        CallbackData callbackData = new CallbackData();
        // out_trade_no = 下单时传入的平台 tradeNo
        callbackData.setTradeNo(resp.getOutTradeNo());
        // trade_no = 海科融通交易号
        callbackData.setOutTradeNo(resp.getTradeNo());
        // 抽象态 → 框架状态码
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setCallbackErrorMsg("海科融通回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        return callbackData;
    }
}
