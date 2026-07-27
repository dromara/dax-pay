package cn.daxpay.open.payment.unipay.result.open;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通用认证回调重定向参数
///
/// OAuth 回调处理完成后, 系统将用户标识(openId)和状态封装为本对象,
/// 拼接为 query string 重定向到对接方的 redirect_url, 并附加平台签名供对接方验签。
///
/// ## 参数说明
/// - 成功时: code=0, msg=success, openid 填充, sign 签名
/// - 失败时: code=1, msg=错误描述, sign 签名
///
/// ## 验签方式
/// 对接方使用平台公钥验签, 规则与支付接口一致(字段 ASCII 字典序排序, 空值不参与签名)。
@Data
@Accessors(chain = true)
@Schema(title = "通用认证回调参数")
public class OpenAuthRedirectResult {

    /// 状态码: 0=成功, 1=失败(对齐 CommonCode.SUCCESS_CODE/FAIL_CODE)
    @Schema(description = "状态码")
    private int code;

    /// 状态描述
    @Schema(description = "状态描述")
    private String msg;

    /// 用户标识(微信openId / 支付宝userId / 抖音openId, 由各通道 Provider 统一映射)
    @Schema(description = "用户标识(openId)")
    private String openId;

    /// 平台签名(对接方用平台公钥验签)
    @Schema(description = "签名")
    private String sign;
}