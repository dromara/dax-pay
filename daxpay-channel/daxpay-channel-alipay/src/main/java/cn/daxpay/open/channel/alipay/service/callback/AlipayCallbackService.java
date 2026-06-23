package cn.daxpay.open.channel.alipay.service.callback;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.dto.AlipayCallbackVerifyReq;
import cn.daxpay.open.channel.alipay.dto.AlipayCallbackVerifyResp;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectConfigAssembler;
import cn.daxpay.open.payment.common.context.CallbackInfo;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.pay.order.dao.PayTradeManager;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/// # 支付宝回调验签业务服务
///
/// 主应用收到支付宝异步通知后的处理流程:
/// 1. 凭 `out_trade_no`(下单时透传的 tradeNo) 反查 [PayTrade]
/// 2. 复用 [AlipayDirectConfigAssembler] 组装通道配置(含验签所需支付宝公钥)
/// 3. 通过 [AlipayChannelClient] 转发原始参数到子应用完成 RSA2 验签与字段解析
/// 4. 验签通过则填充 [PaymentContext] 的回调上下文, 供 [cn.daxpay.open.payment.pay.service.PayCallbackService] 使用
///
/// 订单状态不在本类更新(由主应用 [cn.daxpay.open.payment.pay.service.PayCallbackService] 统一维护)。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayCallbackService {

    private final PayTradeManager payTradeManager;
    private final AlipayDirectConfigAssembler alipayDirectConfigAssembler;
    private final AlipayChannelClient alipayChannelClient;
    private final PaymentContext paymentContext;

    /// 处理支付宝异步通知: 反查订单 -> 组装配置 -> 调子应用验签 -> 填充回调上下文
    ///
    /// @param rawParams 支付宝 POST 过来的原始 form 参数
    /// @return 验签是否通过(通过则回调上下文已填充)
    public boolean verifyAndFillContext(Map<String, String> rawParams) {
        // 下单时透传给支付宝的 out_trade_no = 本系统的支付交易号 tradeNo
        String tradeNo = rawParams.get("out_trade_no");
        if (StrUtil.isBlank(tradeNo)) {
            log.warn("支付宝回调缺少 out_trade_no 参数");
            return false;
        }
        // 回调为系统级调用, 无商户上下文, 忽略租户查询订单
        PayTrade trade = payTradeManager.findByTradeNoNotTenant(tradeNo).orElse(null);
        if (trade == null) {
            log.warn("支付宝回调对应的订单不存在: tradeNo={}", tradeNo);
            return false;
        }
        // 组装通道配置(复用下单配置, 含验签所需的支付宝公钥)
        Map<String, Object> config = alipayDirectConfigAssembler.buildConfig(trade.getMchNo());

        // 调子应用验签
        AlipayCallbackVerifyReq req = new AlipayCallbackVerifyReq();
        req.setCallbackType("pay");
        req.setRawParams(rawParams);
        req.setConfig(config);
        DaxResult<AlipayCallbackVerifyResp> result = alipayChannelClient.callbackVerify(req);
        if (result.getCode() != 0) {
            log.warn("子应用回调验签调用失败: tradeNo={}, msg={}", tradeNo, result.getMsg());
            return false;
        }
        AlipayCallbackVerifyResp resp = result.getData();
        if (!Boolean.TRUE.equals(resp.getVerified())) {
            log.warn("支付宝回调验签未通过: tradeNo={}", tradeNo);
            return false;
        }

        // 填充回调上下文, 供 PayCallbackService 使用
        CallbackInfo callbackInfo = paymentContext.getCallbackInfo();
        callbackInfo.setTradeNo(resp.getBizOrderNo());
        callbackInfo.setOutTradeNo(resp.getOutOrderNo());
        callbackInfo.setTradeStatus(resp.getStatus());
        // 完成时间(支付宝 gmt_payment 格式 yyyy-MM-dd HH:mm:ss, GMT+8)
        if (StrUtil.isNotBlank(resp.getFinishTime())) {
            callbackInfo.setFinishTime(parseAlipayTime(resp.getFinishTime()));
        }
        return true;
    }

    /// 解析支付宝时间(yyyy-MM-dd HH:mm:ss, GMT+8) 为 OffsetDateTime
    private OffsetDateTime parseAlipayTime(String time) {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return OffsetDateTime.parse(time, fmt.withZone(ZoneOffset.ofHours(8)));
        } catch (Exception e) {
            log.warn("解析支付宝时间失败: {}", time, e);
            return OffsetDateTime.now();
        }
    }
}
