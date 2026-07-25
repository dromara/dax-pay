package cn.daxpay.open.payment.merchant.service.develop;

import cn.daxpay.open.payment.common.util.JsonSignStrUtil;
import cn.daxpay.open.payment.merchant.param.develop.DevelopSignParam;
import cn.daxpay.open.payment.merchant.param.develop.DevelopVerifyParam;
import cn.daxpay.open.payment.merchant.result.develop.DevelopSignResult;
import cn.daxpay.open.platform.core.util.RsaSignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 签名调试服务
///
/// 提供参数签名生成与验签功能, 便于商户对接联调
@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopSignService {

    /// 生成签名
    public DevelopSignResult sign(DevelopSignParam param) {
        // 待签名原文(扁平化排序后的键值拼接串)
        String signStr = JsonSignStrUtil.buildSignStr(param.getJson());
        // 使用私钥生成签名
        String sign = RsaSignUtil.sign(signStr, param.getPrivateKey());
        return new DevelopSignResult().setSignStr(signStr).setSign(sign);
    }

    /// 验签
    public boolean verify(DevelopVerifyParam param) {
        String signStr = JsonSignStrUtil.buildSignStr(param.getJson());
        try {
            return RsaSignUtil.verify(signStr, param.getSign(), param.getPublicKey());
        } catch (Exception e) {
            // 验签失败不抛异常, 返回 false 便于前端展示
            log.warn("验签失败: {}", e.getMessage());
            return false;
        }
    }
}
