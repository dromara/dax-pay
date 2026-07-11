package cn.daxpay.open.channel.alipay.service.callback;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.req.AlipayCallbackParseReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayCallbackParseResp;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectConfigAssembler;
import cn.daxpay.open.channel.alipay.service.isv.AlipayIsvConfigAssembler;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.common.callback.RefundCallbackData;
import cn.daxpay.open.payment.core.trade.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.dao.PayRefundOrderManager;
import cn.daxpay.open.payment.core.trade.dao.PayTradeManager;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.service.PayCallbackService;
import cn.daxpay.open.payment.core.trade.service.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/// # 支付宝回调处理服务(支付/退款统一入口)
///
/// 支付宝支付与退款共用同一回调端点 `/unipay/callback/{mchNo}/{appId}/alipay`(无 /pay 后缀,
/// 旧版约定, 见 AlipayPayService.buildNotifyUrl), 通过表单参数区分:
/// - 含 `out_request_no` → 退款回调
/// - 否则 → 支付回调
///
/// 流程: 主应用接收原始表单 → 凭 out_trade_no/out_request_no 反查订单 → 组装通道凭证(直连/服务商) →
/// 转发子应用验签解析 → 构建 CallbackData/RefundCallbackData 交框架更新订单状态。
/// 主应用零加密代码, 验签与字段解析集中在子应用 dax-pay-channel-one。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayCallbackService {

    private static final String NOTIFY_SUCCESS = "success";
    private static final String NOTIFY_FAIL = "fail";

    private final AlipayChannelClient alipayChannelClient;
    private final AlipayDirectConfigAssembler alipayDirectConfigAssembler;
    private final AlipayIsvConfigAssembler alipayIsvConfigAssembler;
    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager normalPayOrderManager;
    private final PayRefundOrderManager payRefundOrderManager;
    private final PayCallbackService payCallbackService;
    private final RefundCallbackService refundCallbackService;

    /// 回调统一处理(自动区分支付/退款)
    public String handle(HttpServletRequest request) {
        // 1. 提取全部表单参数
        Map<String, String> params = extractFormParams(request);
        if (params.isEmpty()) {
            log.error("支付宝回调: 表单参数为空");
            return NOTIFY_FAIL;
        }

        // 2. 区分支付/退款(退款回调含 out_request_no)
        boolean refund = params.containsKey("out_request_no");
        try {
            if (refund) {
                return handleRefund(params);
            }
            return handlePay(params);
        } catch (Exception e) {
            log.error("支付宝回调业务处理失败", e);
            return NOTIFY_FAIL;
        }
    }

    /// 支付回调处理
    private String handlePay(Map<String, String> params) {
        // 凭 out_trade_no(平台交易号) 反查凭证
        String tradeNo = params.get("out_trade_no");
        AlipaySdkCredential credential = resolveCredentialByTradeNo(tradeNo);
        if (credential == null) {
            log.error("支付宝支付回调: 无法解析通道凭证, tradeNo={}", tradeNo);
            return NOTIFY_FAIL;
        }
        AlipayCallbackParseResp resp = parse(params, credential, false);
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("支付宝支付回调验签失败: tradeNo={}", tradeNo);
            return NOTIFY_FAIL;
        }
        CallbackData callbackData = new CallbackData();
        callbackData.setCallbackData(params);
        callbackData.setTradeNo(resp.getOutTradeNo());
        callbackData.setOutTradeNo(resp.getTradeNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setCallbackErrorMsg("支付宝回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        payCallbackService.payCallback(callbackData);
        return NOTIFY_SUCCESS;
    }

    /// 退款回调处理
    private String handleRefund(Map<String, String> params) {
        // out_request_no 为平台退款号, 凭原支付订单号反查凭证
        String refundNo = params.get("out_request_no");
        PayRefundOrder refundOrder = payRefundOrderManager.findByRefundNo(refundNo).orElse(null);
        if (refundOrder == null) {
            log.error("支付宝退款回调: 退款单不存在 refundNo={}", refundNo);
            return NOTIFY_FAIL;
        }
        AlipaySdkCredential credential = resolveCredentialByTradeNo(refundOrder.getOrderNo());
        if (credential == null) {
            log.error("支付宝退款回调: 无法解析通道凭证, refundNo={}", refundNo);
            return NOTIFY_FAIL;
        }
        AlipayCallbackParseResp resp = parse(params, credential, true);
        if (resp == null || !Boolean.TRUE.equals(resp.getSuccess())) {
            log.error("支付宝退款回调验签失败: refundNo={}", refundNo);
            return NOTIFY_FAIL;
        }
        RefundCallbackData callbackData = new RefundCallbackData();
        callbackData.setCallbackData(params);
        callbackData.setRefundNo(refundNo);
        callbackData.setOutRefundNo(resp.getOutRefundNo());
        if (Objects.equals(resp.getTradeStatus(), "SUCCESS")) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("支付宝退款回调状态非成功: " + resp.getTradeStatus());
        }
        callbackData.setFinishTime(resp.getFinishTime());
        refundCallbackService.refundCallback(callbackData);
        return NOTIFY_SUCCESS;
    }

    /// 转发子应用验签解析
    private AlipayCallbackParseResp parse(Map<String, String> params, AlipaySdkCredential credential, boolean refund) {
        AlipayCallbackParseReq req = new AlipayCallbackParseReq();
        req.setCredential(credential);
        req.setParams(params);
        var result = refund
                ? alipayChannelClient.parseRefundCallback(req)
                : alipayChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("支付宝回调: 子应用解析失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }

    /// 凭原支付交易号反查通道凭证(直连/服务商自动分发)
    private AlipaySdkCredential resolveCredentialByTradeNo(String tradeNo) {
        if (tradeNo == null || tradeNo.isBlank()) {
            return null;
        }
        PayTrade trade = payTradeManager.findByTradeNo(tradeNo).orElse(null);
        if (trade == null) {
            return null;
        }
        // 按支付产品分发: 服务商 / 直连
        if (ProductEnum.ALIPAY_ISV.getCode().equals(trade.getProduct())) {
            return alipayIsvConfigAssembler.buildConfig(trade.getMchNo());
        }
        return alipayDirectConfigAssembler.buildConfig(
                trade.getMchNo(), trade.getChannelMchNo(), trade.getCapability());
    }

    /// 提取 request 全部表单参数为 Map<String,String>(多值取首项)
    private Map<String, String> extractFormParams(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, String> result = new HashMap<>(paramMap.size());
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String[] values = entry.getValue();
            if (values != null && values.length > 0) {
                result.put(entry.getKey(), values[0]);
            }
        }
        return result;
    }
}
