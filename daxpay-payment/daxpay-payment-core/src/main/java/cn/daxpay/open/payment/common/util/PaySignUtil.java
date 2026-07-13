package cn.daxpay.open.payment.common.util;

import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.common.json.util.JsonUtil;
import cn.daxpay.open.platform.core.util.RsaSignUtil;
import lombok.experimental.UtilityClass;

/// # 如果需要进行签名,
///
/// 1. 参数名ASCII码从小到大排序（字典序）
/// 2. 如果参数的值为空不参与签名
/// 3. 嵌套参数进行平铺
@UtilityClass
public class PaySignUtil {

    private final String FIELD_SIGN  = "sign";

    /// 签名操作
    /// @param param 签名对象
    /// @param privateKeyContent 私钥(文本格式)
    /// @return 签名
    public String sign(Object param, String privateKeyContent){
        String signStr = ObjectSignStrUtil.buildSignStr(param);
        return RsaSignUtil.sign(signStr, privateKeyContent);
    }

    /// 验签操作
    /// @param param 验签对象
    /// @param  publicKeyContent 公钥(文本格式)
    public boolean verify(Object param, String publicKeyContent) {
        String json = JsonUtil.toJsonStr(param);
        return verify(json, publicKeyContent);
    }

    /// 验签操作
    /// @param json 验签json
    /// @param publicKeyContent 公钥(文本格式)
    public boolean verify(String json, String publicKeyContent) {
        var map = JsonSignStrUtil.buildSortedMap(json);
        var sign = map.remove(FIELD_SIGN);
        String signStr = JsonSignStrUtil.buildSignStr(map);
        try {
            return RsaSignUtil.verify(signStr, sign, publicKeyContent);
        } catch (Exception e){
            // 验签失败
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.assist.signVerifyFailDetail", e.getMessage());
        }
    }
}

