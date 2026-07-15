package cn.daxpay.open.plugin.easypay.util;

import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.util.RsaSignUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import tools.jackson.core.type.TypeReference;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.TreeMap;

/// # 易支付签名与金额工具
///
@Slf4j
@UtilityClass
public class EasyPayUtil {

    public String signByMd5(Object param, String md5Key) {
        return MD5.create().digestHex(signStrByMd5(param, md5Key)).toLowerCase();
    }

    public String signStrByMd5(Object param, String md5Key) {
        var map = toSignMap(param);
        var sb = new StringBuilder();
        map.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        return sb.deleteCharAt(sb.length() - 1).append(md5Key).toString();
    }

    public boolean verifySignByMd5(Object param, String sign, String md5Key) {
        String calculated = MD5.create().digestHex(signStrByMd5(param, md5Key));
        return calculated.equalsIgnoreCase(sign);
    }

    public String signByRsa(Object param, String rsaPrivateKey) {
        return RsaSignUtil.sign(signStrByRsa(param), rsaPrivateKey);
    }

    public String signStrByRsa(Object param) {
        var map = toSignMap(param);
        var sb = new StringBuilder();
        map.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public boolean verifySignByRsa(Object param, String sign, String rsaPublicKey) {
        return RsaSignUtil.verify(signStrByRsa(param), sign, rsaPublicKey);
    }

    private TreeMap<String, String> toSignMap(Object param) {
        String json = JacksonUtil.toJson(param);
        var map = JacksonUtil.toBean(json, new TypeReference<TreeMap<String, String>>() {});
        map.remove("sign");
        map.remove("sign_type");
        map.entrySet().removeIf(e -> e.getValue() == null || e.getValue().isEmpty());
        return map;
    }

    /// 元转分
    public long yuanToFen(String money) {
        return new BigDecimal(money).multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).longValueExact();
    }

    public long yuanToFen(BigDecimal money) {
        return money.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).longValueExact();
    }

    /// 分转元字符串
    public String fenToYuanString(long fen) {
        return BigDecimal.valueOf(fen).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).toPlainString();
    }

    /// 平台/商户公钥展示用：去掉 PEM 头尾与空白，得到可直接复制的纯 Base64
    ///
    /// 易支付对接方通常粘贴纯 Base64，不需要 PEM 注释与换行
    public String stripPemPublicKey(String pem) {
        if (StrUtil.isBlank(pem)) {
            return pem;
        }
        return pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
    }
}
