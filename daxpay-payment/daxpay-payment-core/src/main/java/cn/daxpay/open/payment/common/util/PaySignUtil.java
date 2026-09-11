package cn.daxpay.open.payment.common.util;

import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.util.RsaSignUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

/// # 如果需要进行签名,
///
/// 1. 参数名ASCII码从小到大排序（字典序）
/// 2. 如果参数的值为空不参与签名
/// 3. 嵌套参数进行平铺
///
/// ## 不变量: 签名串 == 报文规范字面量
///
/// 出参与入参**共用同一条序列化链路**([JacksonUtil], 与 HTTP 报文同一个 ObjectMapper):
/// - 出参签名: 对象 → 同源序列化 → 规范化 → 签名, 故调用方按收到的报文即可验签;
/// - 入参验签: 对象 → 同源序列化 → 规范化 → 比对, 即校验调用方按**平台规范字面量**构造的签名串。
///
/// 禁止改回 hutool `JsonUtil` 重序列化: 它的时间格式与报文序列化器不一致
/// (无法处理无时区时间, 且会改写时间精度), 是历史上验签失败的根因。
@UtilityClass
public class PaySignUtil {

    private final String FIELD_SIGN  = "sign";

    /// 签名操作
    /// @param param 签名对象
    /// @param privateKeyContent 私钥(文本格式)
    /// @return 签名
    public String sign(Object param, String privateKeyContent){
        String signStr = buildSignStr(param);
        return RsaSignUtil.sign(signStr, privateKeyContent);
    }

    /// 生成待签名字符串(对象 → 同源序列化 → 扁平化排序拼接)
    ///
    /// 与报文使用同一个 ObjectMapper, 故字段级 @JsonFormat(如 unipay 契约的北京时间)
    /// 对签名串同样生效, 无需再维护第二套格式化逻辑。
    /// @param param 签名对象
    public String buildSignStr(Object param) {
        // 与 HTTP 出参/通知报文同一个 mapper(非忽略空值版本), 保证序列化行为完全一致
        String json = JacksonUtil.toJson(param, false);
        return JsonSignStrUtil.buildSignStr(json);
    }

    /// 验签操作(对象, 按平台规范字面量)
    ///
    /// 与 [#sign] 使用同一条序列化链路, 因此调用方必须按其自身语言的等价格式构造签名串
    /// (如时间字段用东八区 `yyyy-MM-dd HH:mm:ss` 字面量), 详见接口文档「签名机制」。
    /// @param param 验签对象(其中的 sign 字段不参与签名计算)
    /// @param sign 签名值(Base64)
    /// @param publicKeyContent 公钥(文本格式)
    public boolean verify(Object param, String sign, String publicKeyContent) {
        if (StrUtil.isBlank(sign)) {
            // 签名缺失视同验签失败, 由调用方抛出统一的验签异常
            return false;
        }
        String signStr = buildSignStr(param);
        return RsaSignUtil.verify(signStr, sign, publicKeyContent);
    }

    /// 验签操作(原始报文)
    ///
    /// 用于调用方原样转发的报文场景(如回调测试), 按报文字面量验签。
    /// @param json 调用方发送的原始报文(未做任何改写)
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
