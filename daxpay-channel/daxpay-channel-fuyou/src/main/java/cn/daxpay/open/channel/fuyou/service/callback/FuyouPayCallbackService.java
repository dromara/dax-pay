package cn.daxpay.open.channel.fuyou.service.callback;

import cn.daxpay.open.channel.fuyou.dao.isv.FuyouIsvKeyConfigManager;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvKeyConfig;
import cn.daxpay.open.payment.common.callback.CallbackData;
import cn.daxpay.open.payment.core.trade.dao.PayTradeManager;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.service.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/// # 富友支付回调处理服务
///
/// 富友异步通知 → 主应用接收 `req` 参数(URL编码的XML) → GBK 解码 → xmlToMap →
/// 用全局服务商公钥 MD5withRSA 验签 → 凭 mchnt_order_no(关联订单号) 反查 PayTrade →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 富友回调签名机制(与响应签名一致):
/// - 整个回调 Map(去 sign 与 reserved 开头字段)按 key 字典序拼接 `k1=v1&k2=v2`
/// - 用富友公钥 MD5withRSA + GBK 验证 sign(Base64)
///
/// 成功响应: 返回 "1"(富友约定); 验签/解析失败返回 "0"。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouPayCallbackService {

    /// 富友回调成功响应
    public static final String RESP_SUCCESS = "1";
    /// 富友回调失败响应
    public static final String RESP_FAIL = "0";

    /// 富友字符集(GBK)
    private static final Charset CHARSET = Charset.forName("GBK");
    /// 东八区
    private static final ZoneOffset CST = ZoneOffset.ofHours(8);
    /// 富友回调时间格式(yyyyMMddHHmmss)
    private static final DateTimeFormatter PAY_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN);

    private final FuyouIsvKeyConfigManager fuyouIsvKeyConfigManager;
    private final PayTradeManager payTradeManager;
    private final PayCallbackService payCallbackService;

    /// 支付回调处理
    public String payHandle(String reqParam) {
        if (StrUtil.isBlank(reqParam)) {
            log.error("富友支付回调: req 参数为空");
            return RESP_FAIL;
        }

        // 1. GBK 解码 + XML 转 Map
        Map<String, String> callbackParam;
        try {
            String xml = URLDecoder.decode(reqParam, CHARSET);
            callbackParam = xmlToMap(xml);
        } catch (Exception e) {
            log.error("富友支付回调解析失败", e);
            return RESP_FAIL;
        }

        // 2. 获取全局服务商公钥(只读查询)
        FuyouIsvKeyConfig keyConfig = fuyouIsvKeyConfigManager.findByProduct(ProductEnum.FUYOU_PAY.getCode())
                .orElse(null);
        if (keyConfig == null || StrUtil.isBlank(keyConfig.getPublicKey())) {
            log.error("富友支付回调: 服务商密钥未配置, 无法验签");
            return RESP_FAIL;
        }

        // 3. 验签(MD5withRSA + GBK)
        String sign = callbackParam.get("sign");
        if (StrUtil.isBlank(sign) || !verifySign(callbackParam, sign, keyConfig.getPublicKey())) {
            log.error("富友支付回调验签失败");
            return RESP_FAIL;
        }

        // 4. 凭关联订单号(mchnt_order_no)反查平台交易号
        String relationOrderNo = MapUtil.getStr(callbackParam, "mchnt_order_no");
        PayTrade trade = payTradeManager.lambdaQuery()
                .eq(PayTrade::getRelationOrderNo, relationOrderNo)
                .oneOpt()
                .orElse(null);
        if (trade == null) {
            log.error("富友支付回调: 未找到关联订单 relationOrderNo={}", relationOrderNo);
            return RESP_FAIL;
        }

        // 5. 构建 CallbackData
        CallbackData callbackData = new CallbackData();
        callbackData.setCallbackData(callbackParam);
        callbackData.setTradeNo(trade.getTradeNo());
        callbackData.setOutTradeNo(MapUtil.getStr(callbackParam, "transaction_id"));
        // 富友支付回调通知仅出现在支付成功时
        callbackData.setTradeStatus("SUCCESS");
        // 完成时间(yyyyMMddHHmmss → OffsetDateTime)
        String finishTime = MapUtil.getStr(callbackParam, "txn_fin_ts");
        if (StrUtil.isNotBlank(finishTime)) {
            try {
                callbackData.setFinishTime(LocalDateTime.parse(finishTime.trim(), PAY_TIME_FORMATTER).atOffset(CST));
            } catch (Exception e) {
                log.warn("富友回调时间解析失败: txn_fin_ts={}", finishTime);
            }
        }

        try {
            payCallbackService.payCallback(callbackData);
            return RESP_SUCCESS;
        } catch (Exception e) {
            log.error("富友支付回调业务处理失败: tradeNo={}", trade.getTradeNo(), e);
            return RESP_FAIL;
        }
    }

    /// 验证回调签名: 去 sign 与 reserved 开头字段, 字典序拼接, MD5withRSA + GBK 验签
    private boolean verifySign(Map<String, String> params, String sign, String publicKeyStr) {
        try {
            // 过滤 sign 与 reserved 开头字段
            Map<String, String> filtered = new TreeMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                if (key.equalsIgnoreCase("sign") || (key.length() >= 8 && key.substring(0, 8).equalsIgnoreCase("reserved"))) {
                    continue;
                }
                filtered.put(key, entry.getValue() == null ? "" : entry.getValue());
            }
            // 字典序拼接 k1=v1&k2=v2
            ArrayList<String> keys = new ArrayList<>(filtered.keySet());
            Collections.sort(keys);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < keys.size(); i++) {
                if (i > 0) {
                    sb.append("&");
                }
                sb.append(keys.get(i)).append("=").append(filtered.get(keys.get(i)));
            }
            String signSource = sb.toString();

            // MD5withRSA + GBK 验签
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr.replaceAll("[\\s*\t\n\r]", ""));
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(publicKey);
            signature.update(signSource.getBytes(CHARSET));
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            log.error("富友回调验签异常", e);
            return false;
        }
    }

    /// XML 字符串转 Map(值统一转 String)
    private Map<String, String> xmlToMap(String xml) {
        Map<String, Object> raw = XmlUtil.xmlToMap(xml);
        Map<String, String> result = new LinkedHashMap<>(raw.size());
        for (Map.Entry<String, Object> entry : raw.entrySet()) {
            result.put(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
        }
        return result;
    }
}
