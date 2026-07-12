package cn.daxpay.open.channel.leshua.service.callback;

import cn.daxpay.open.channel.leshua.client.LeshuaChannelClient;
import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.client.req.LeshuaCallbackParseReq;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaCallbackParseResp;
import cn.daxpay.open.channel.leshua.code.LeshuaCode;
import cn.daxpay.open.channel.leshua.dao.isv.LeshuaIsvKeyConfigManager;
import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvKeyConfig;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 乐刷支付回调处理服务
///
/// 乐刷异步通知(XML) → 主应用接收原始 body → 转发子应用用 tradeKey 做 MD5/SM3 验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 主应用不直接依赖乐刷 SDK(签名工具在子应用侧), 验签/解析由子应用 dax-pay-channel-two 承担。
/// 乐刷回调成功响应约定为 `000000`。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaPayCallbackService {

    private static final String NOTIFY_SUCCESS = "000000";
    private static final String NOTIFY_FAIL = "500";

    private final LeshuaChannelClient leshuaChannelClient;
    private final LeshuaIsvKeyConfigManager leshuaIsvKeyConfigManager;
    private final PayCallbackService payCallbackService;

    /// 支付回调处理
    public String payHandle(HttpServletRequest request) {
        // 1. 提取回调原始报文(XML)
        String body = JakartaServletUtil.getBody(request);

        // 2. 获取服务商密钥(用于子应用验签, 只读查询)
        LeshuaIsvKeyConfig keyConfig = leshuaIsvKeyConfigManager
                .findByProduct(ProductEnum.LESHUA_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || StrUtil.isBlank(keyConfig.getTradeKey())) {
            log.error("乐刷支付回调: 服务商密钥未配置, 无法验签");
            return NOTIFY_FAIL;
        }

        // 3. 组装凭证(只需 tradeKey + signType)并转发子应用验签解析
        LeshuaSdkCredential credential = new LeshuaSdkCredential();
        credential.setTradeKey(keyConfig.getTradeKey());
        credential.setSignType(keyConfig.getSignType());
        LeshuaCallbackParseReq req = new LeshuaCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setCallbackType("PAY");

        DaxResult<LeshuaCallbackParseResp> result = leshuaChannelClient.parsePayCallback(req);
        if (result.getCode() != 0 || result.getData() == null
                || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("乐刷支付回调验签/解析失败");
            return NOTIFY_FAIL;
        }
        LeshuaCallbackParseResp resp = result.getData();

        // 4. 构建 CallbackData 并交由框架更新订单
        CallbackData callbackData = new CallbackData();
        // 平台订单号(乐刷回调 third_order_id 回显 = 下单时传入的 tradeNo)
        callbackData.setTradeNo(resp.getOutTradeNo());
        // 乐刷订单号
        callbackData.setOutTradeNo(resp.getLeshuaOrderId());
        if (Objects.equals(resp.getTradeStatus(), LeshuaCode.PAY_STATUS_SUCCESS)) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeStatus(resp.getTradeStatus());
            callbackData.setCallbackErrorMsg("乐刷回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());

        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("乐刷支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            return NOTIFY_FAIL;
        }
        // 5. 返回乐刷约定的成功回执
        return NOTIFY_SUCCESS;
    }
}
