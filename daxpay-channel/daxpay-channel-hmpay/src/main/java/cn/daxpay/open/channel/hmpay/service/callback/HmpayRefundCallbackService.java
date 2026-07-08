package cn.daxpay.open.channel.hmpay.service.callback;

import cn.daxpay.open.channel.hmpay.client.HmpayChannelClient;
import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.client.req.HmpayCallbackParseReq;
import cn.daxpay.open.channel.hmpay.client.resp.HmpayCallbackParseResp;
import cn.daxpay.open.channel.hmpay.dao.isv.HmpayIsvKeyConfigManager;
import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvKeyConfig;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 河马付退款回调处理服务
///
/// 杉德退款异步通知 → 主应用接收原始 body → 转发子应用用杉德公钥 RSA 验签 → 记录退款回调结果。
///
/// TODO 退款单状态更新待接入退款回调框架(平台目前无独立 RefundCallbackService,
///      后续可通过 PayRefundService 或新增退款回调入口完成状态流转)。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayRefundCallbackService {

    private static final String NOTIFY_FAIL = "FAIL";
    private static final String NOTIFY_SUCCESS = "success";

    private final HmpayChannelClient hmpayChannelClient;
    private final HmpayIsvKeyConfigManager hmpayIsvKeyConfigManager;

    /// 退款回调处理
    public String refundHandle(HttpServletRequest request) {
        // 1. 提取回调原始报文
        String body = JakartaServletUtil.getBody(request);

        // 2. 获取服务商公钥(用于子应用验签)
        HmpayIsvKeyConfig keyConfig = hmpayIsvKeyConfigManager
                .findByProduct(ProductEnum.HM_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || StrUtil.isBlank(keyConfig.getPublicKey())) {
            log.error("河马付退款回调: 服务商公钥未配置, 无法验签");
            return NOTIFY_FAIL;
        }

        // 3. 转发子应用验签解析
        HmpaySdkCredential credential = new HmpaySdkCredential();
        credential.setPublicKey(keyConfig.getPublicKey());
        HmpayCallbackParseReq req = new HmpayCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);

        DaxResult<HmpayCallbackParseResp> result = hmpayChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0 || result.getData() == null
                || !Boolean.TRUE.equals(result.getData().getSuccess())) {
            log.error("河马付退款回调验签/解析失败");
            return NOTIFY_FAIL;
        }
        HmpayCallbackParseResp resp = result.getData();

        // 4. 记录退款回调结果, TODO 接入退款单状态更新框架
        log.info("河马付退款回调: outRefundNo={}, tradeNo={}, amount={}, finishTime={}",
                resp.getOutTradeNo(), resp.getTradeNo(), resp.getAmount(), resp.getFinishTime());
        // TODO 退款成功, 更新退款单状态(待接入退款回调框架)
        log.info("河马付退款成功: outRefundNo={}", resp.getOutTradeNo());
        // 杉德要求返回 success 表示接收成功
        return NOTIFY_SUCCESS;
    }
}
