package cn.daxpay.open.demo.callback.controller;

import cn.daxpay.open.demo.callback.result.TestCallbackRecord;
import cn.daxpay.open.demo.callback.service.TestCallbackStore;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.type.TypeReference;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/// # 测试回调接受控制器
///
/// 模拟商户端接收平台出站通知([DaxNoticeResult] 签名 JSON),
/// 用于支付/退款回调链路的联调与验证。
///
/// 平台出站通知经 [SystemHttpSignedSender] 用平台私钥 RSA 签名后 POST 到商户配置的 notifyUrl,
/// 本端用平台公钥验签并暂存记录, 供 `GET /test/callback/list` 查看联调结果。
///
/// Ack 规则: 接收端点(notify/pay/refund)返回固定 "SUCCESS"
/// (平台要求 HTTP 2xx 且 body 等于 SUCCESS, 忽略大小写)。
/// 管理端点(list/clear)走统一 [Result] 格式, 供人工查看。
///
/// 鉴权: `/test/**` 已在白名单, 类上叠加 `@IgnoreAuth` 双保险。
@Slf4j
@IgnoreAuth
@Tag(name = "测试商户回调接收控制器")
@RestController
@RequestMapping("/test/callback")
@RequiredArgsConstructor
public class TestCallbackController {

    private final PlatformConfigProperties platformConfigProperties;
    private final TestCallbackStore store;

    /// 通用通知接收入口(支付/退款均可)
    @Operation(summary = "通用通知接收")
    @PostMapping("/notify")
    public String notify(@RequestBody String body) {
        return handle(body);
    }

    /// 支付通知接收(语义端点, 商户可将应用 notifyUrl 指向此)
    @Operation(summary = "支付通知接收")
    @PostMapping("/pay")
    public String pay(@RequestBody String body) {
        return handle(body);
    }

    /// 退款通知接收(语义端点)
    @Operation(summary = "退款通知接收")
    @PostMapping("/refund")
    public String refund(@RequestBody String body) {
        return handle(body);
    }

    /// 拉取最近的回调接收记录(最新在前)
    @Operation(summary = "拉取回调接收记录")
    @GetMapping("/list")
    public Result<List<TestCallbackRecord>> list() {
        return Res.ok(store.list());
    }

    /// 清空全部回调接收记录
    @Operation(summary = "清空调回接收记录")
    @PostMapping("/clear")
    public Result<Void> clear() {
        store.clear();
        return Res.ok();
    }

    /// 统一处理: 验签 → 解析 → 暂存 → 返回 SUCCESS
    private String handle(String body) {
        log.info("收到测试回调通知: {}", body);
        // 用平台公钥验签
        String publicKey = platformConfigProperties.getKeyConfig().getPublicKey();
        boolean verifyResult = PaySignUtil.verify(body, publicKey);
        log.info("回调签名验证结果: {}", verifyResult);

        // 解析报文, 提取关键字段(event 区分支付/退款, data 内业务号等)
        String event = null;
        String mchNo = null;
        String appId = null;
        String bizNo = null;
        String amount = null;
        try {
            Map<String, Object> map = JacksonUtil.toBean(body, new TypeReference<Map<String, Object>>() {});
            event = (String) map.get("event");
            mchNo = (String) map.get("mchNo");
            appId = (String) map.get("appId");
            Object dataObj = map.get("data");
            if (dataObj instanceof Map<?, ?> dataMap) {
                bizNo = pickFirst(dataMap, "tradeNo", "outTradeNo", "refundNo", "outRefundNo", "orderNo");
                amount = pickFirst(dataMap, "amount", "totalAmount", "payAmount", "refundAmount");
            }
        } catch (Exception e) {
            // 解析失败不影响 Ack, 仅记录原始报文供排查
            log.warn("解析通知报文失败, 仅记录原始报文", e);
        }

        TestCallbackRecord record = new TestCallbackRecord()
                .setId(UUID.randomUUID().toString(true))
                .setEvent(event)
                .setBizType(resolveBizType(event))
                .setMchNo(mchNo)
                .setAppId(appId)
                .setBizNo(bizNo)
                .setAmount(amount)
                .setVerifyResult(verifyResult)
                .setReceiveTime(OffsetDateTime.now())
                .setRawBody(StrUtil.sub(body, 0, 2000));
        store.add(record);
        return "SUCCESS";
    }

    /// 按 key 顺序取首个非空值
    private String pickFirst(Map<?, ?> map, String... keys) {
        for (String key : keys) {
            Object val = map.get(key);
            if (val != null) {
                return String.valueOf(val);
            }
        }
        return null;
    }

    /// 由事件码推断业务类型(pay / refund / unknown)
    private String resolveBizType(String event) {
        if (StrUtil.isBlank(event)) {
            return "unknown";
        }
        if (event.startsWith("pay")) {
            return "pay";
        }
        if (event.startsWith("refund")) {
            return "refund";
        }
        return "unknown";
    }
}
