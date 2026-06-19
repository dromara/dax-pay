package cn.daxpay.open.payment.old.pay.service.develop;

import cn.daxpay.open.payment.common.util.JsonSignStrUtil;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.platform.core.util.RsaSignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 参数签名开发调试服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopSignService {

    /// 生成签名字符串
    public String genSignStr(String param){
        return JsonSignStrUtil.buildSignStr(param);
    }

    /// 生成签名
    public String genSign(String param, String privateKey){
        return RsaSignUtil.sign(JsonSignStrUtil.buildSignStr(param), privateKey);
    }

    /// 验证签名
    public boolean verifySign(String param, String publicKey){
        return PaySignUtil.verify(param, publicKey);
    }
}
