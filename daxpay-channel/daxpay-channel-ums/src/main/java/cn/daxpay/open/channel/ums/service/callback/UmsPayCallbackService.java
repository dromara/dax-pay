package cn.daxpay.open.channel.ums.service.callback;

import cn.daxpay.open.channel.ums.client.UmsChannelClient;
import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.req.UmsCallbackParseReq;
import cn.daxpay.open.channel.ums.client.resp.UmsCallbackParseResp;
import cn.daxpay.open.channel.ums.code.UmsCode;
import cn.daxpay.open.channel.ums.service.direct.UmsDirectConfigAssembler;
import cn.daxpay.open.channel.ums.util.UmsDateUtil;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/// # 银联商务支付回调处理服务
///
/// 银联商务异步通知 → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用验签(MD5/SHA256 字典序) →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 银联商务回调为 JSON POST body, 嵌套对象(如 billPayment)转为 JSON 字符串参与验签。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsPayCallbackService {

    private final UmsChannelClient umsChannelClient;
    private final UmsDirectConfigAssembler configAssembler;
    private final PayCallbackService payCallbackService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调参数(JSON body → Map<String,String>, 嵌套对象转 JSON 字符串)
        Map<String, String> params = this.extractParams(request);

        // 2. 组装凭证
        UmsSdkCredential credential = configAssembler.buildConfig(mchNo, channelMchNo, null);

        // 3. 转发到子应用验签
        UmsCallbackParseReq req = new UmsCallbackParseReq();
        req.setCredential(credential);
        req.setParams(params);
        DaxResult<UmsCallbackParseResp> result = umsChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || result.getData() == null || !result.getData().isVerified()) {
            log.error("银联商务支付回调验签失败: channelMchNo={}", channelMchNo);
            return UmsCode.NOTIFY_FAIL;
        }

        // 4. 构建回调数据交由框架处理
        UmsCallbackParseResp resp = result.getData();
        CallbackData callbackData = this.buildCallbackData(resp);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("银联商务支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            return UmsCode.NOTIFY_FAIL;
        }
        return UmsCode.NOTIFY_SUCCESS;
    }

    /// 从 HttpServletRequest 提取回调参数
    ///
    /// 银联商务回调为 JSON POST body, 嵌套对象(如 billPayment)的值转为 JSON 字符串,
    /// 保证验签时所有参数都参与拼接。
    private Map<String, String> extractParams(HttpServletRequest request) {
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> params = new HashMap<>();
        if (StrUtil.isBlank(body)) {
            return params;
        }
        JSONObject json = JSONUtil.parseObj(body);
        for (String key : json.keySet()) {
            Object value = json.get(key);
            if (value != null) {
                if (value instanceof CharSequence) {
                    params.put(key, value.toString());
                } else {
                    params.put(key, JSONUtil.toJsonStr(value));
                }
            }
        }
        return params;
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(UmsCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是支付时传入的 billNo/merOrderId = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.targetOrderId 是第三方交易号
        data.setOutTradeNo(resp.getTargetOrderId());
        // 完成时间(银联商务返回东八区本地时间, 由 UmsDateUtil 解析为带偏移的 OffsetDateTime)
        data.setFinishTime(UmsDateUtil.parseCst(resp.getFinishTime()));
        // 交易状态映射
        data.setTradeStatus(this.mapStatus(resp.getTradeStatus()));
        return data;
    }

    /// 统一状态码 → CallbackStatusEnum
    ///
    /// CallbackStatusEnum 只有 SUCCESS / FAIL, 非成功状态直接传原始值,
    /// 框架按非 SUCCESS 处理(不会误判为支付成功)。
    private String mapStatus(String tradeStatus) {
        if (UmsCode.TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
            return CallbackStatusEnum.SUCCESS.getCode();
        }
        // 非成功状态直接传原始值
        return tradeStatus;
    }
}
