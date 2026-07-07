package cn.daxpay.open.channel.vbill.service.callback;

import cn.daxpay.open.channel.vbill.dao.isv.VbillIsvKeyConfigManager;
import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvKeyConfig;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.core.trade.service.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/// # 随行付支付回调处理服务
///
/// 随行付(天阙科技)异步通知 → 主应用接收 JSON body → 用全局服务商公钥 SHA1withRSA 验签 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 随行付回调签名机制(与响应签名一致):
/// - 整个回调 JSON(去 sign 字段)按 key 字典序拼接 `k1=v1&k2=v2`
/// - 用天阙公钥 SHA1withRSA 验证 sign(Base64)
///
/// 成功响应: 返回 `{"code":"success","msg":"成功"}` JSON; 验签/解析失败返回 code=fail。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillPayCallbackService {

    /// 回调成功响应 code
    private static final String RESP_CODE_SUCCESS = "success";
    private static final String RESP_CODE_FAIL = "fail";

    /// 随行付属国内通道, 回调时间字段(payTime)为东八区本地时间字面量(yyyyMMddHHmmss),
    /// 显式钉死 +08:00 偏移, 避免 OffsetDateTime.parse 因缺 OFFSET_SECONDS 解析失败
    private static final DateTimeFormatter PAY_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN).withZone(ZoneOffset.ofHours(8));

    private final VbillIsvKeyConfigManager vbillIsvKeyConfigManager;
    private final PayCallbackService payCallbackService;

    /// 支付回调处理
    public Map<String, String> payHandle(HttpServletRequest request) {
        Map<String, String> resp = new HashMap<>(4);
        // 1. 读取回调原始 JSON
        String body = readBody(request);
        if (StrUtil.isBlank(body)) {
            log.error("随行付支付回调: body 为空");
            resp.put("code", RESP_CODE_FAIL);
            resp.put("msg", "body 为空");
            return resp;
        }

        // 2. 获取全局服务商公钥(只读查询, 不创建记录)
        VbillIsvKeyConfig keyConfig = vbillIsvKeyConfigManager.findByProduct(ProductEnum.VBILL_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || keyConfig.getPublicKey() == null) {
            log.error("随行付支付回调: 服务商密钥未配置, 无法验签");
            resp.put("code", RESP_CODE_FAIL);
            resp.put("msg", "密钥未配置");
            return resp;
        }

        // 3. 验签(整个回调 JSON 去 sign 后字典序签名)
        if (!verifyCallbackSign(body, keyConfig.getPublicKey())) {
            log.error("随行付支付回调验签失败");
            resp.put("code", RESP_CODE_FAIL);
            resp.put("msg", "验签失败");
            return resp;
        }

        // 4. 解析回调数据并构建 CallbackData
        CallbackData callbackData = parseCallback(body);
        if (callbackData == null) {
            log.error("随行付支付回调解析失败: body={}", body);
            resp.put("code", RESP_CODE_FAIL);
            resp.put("msg", "解析失败");
            return resp;
        }

        try {
            // 5. 交由框架更新订单状态
            payCallbackService.payCallback(callbackData);
            resp.put("code", RESP_CODE_SUCCESS);
            resp.put("msg", "成功");
        } catch (Exception e) {
            log.error("随行付支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            resp.put("code", RESP_CODE_FAIL);
            resp.put("msg", "业务处理失败");
        }
        return resp;
    }

    /// 验证回调签名: 整个 JSON 去 sign, 字典序拼接, SHA1withRSA 验签
    private boolean verifyCallbackSign(String body, String publicKeyStr) {
        try {
            JSONObject json = JSONUtil.parseObj(body);
            String sign = json.getStr("sign");
            if (StrUtil.isBlank(sign)) {
                return false;
            }
            // 构建待验签字符串(去 sign, 其余按字典序拼接; 嵌套对象保留 JSON 字符串)
            Map<String, String> flatMap = new TreeMap<>();
            for (String key : json.keySet()) {
                if ("sign".equals(key)) {
                    continue;
                }
                Object value = json.get(key);
                if (value != null && !value.toString().isEmpty()) {
                    flatMap.put(key, value.toString());
                }
            }
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : flatMap.entrySet()) {
                if (!first) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }
            String signSource = sb.toString();

            // SHA1withRSA 验签
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr.replaceAll("[\\s*\t\n\r]", ""));
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            signature.update(signSource.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            log.error("随行付回调验签异常", e);
            return false;
        }
    }

    /// 解析随行付回调 JSON 为 CallbackData
    private CallbackData parseCallback(String body) {
        try {
            JSONObject json = JSONUtil.parseObj(body);
            CallbackData callbackData = new CallbackData();
            // ordNo = 下单时传入的平台 tradeNo
            callbackData.setTradeNo(json.getStr("ordNo"));
            // uuid = 随行付网关订单号
            callbackData.setOutTradeNo(json.getStr("uuid"));
            // 支付状态(随行付支付回调恒为成功才会通知)
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
            // 完成时间(yyyyMMddHHmmss → OffsetDateTime)
            String payTime = json.getStr("payTime");
            if (StrUtil.isNotBlank(payTime)) {
                try {
                    callbackData.setFinishTime(OffsetDateTime.parse(payTime.trim(), PAY_TIME_FORMATTER));
                } catch (Exception e) {
                    log.warn("随行付回调时间解析失败: payTime={}", payTime);
                }
            }
            return callbackData;
        } catch (Exception e) {
            log.error("随行付回调 JSON 解析失败", e);
            return null;
        }
    }

    /// 元(BigDecimal 字符串) → 分(Long) [备用, 当前 CallbackData 不含金额字段]
    private Long yuanToFen(String yuan) {
        try {
            return new BigDecimal(yuan.trim()).multiply(new BigDecimal("100")).longValue();
        } catch (Exception e) {
            return null;
        }
    }

    /// 读取 request body
    private String readBody(HttpServletRequest request) {
        return JakartaServletUtil.getBody(request);
    }
}
