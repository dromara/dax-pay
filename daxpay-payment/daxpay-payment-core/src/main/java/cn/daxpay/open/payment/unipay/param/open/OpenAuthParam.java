package cn.daxpay.open.payment.unipay.param.open;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 通用认证请求参数(对外开放认证 OPEN 场景)
///
/// 对接方通过 GET 重定向方式请求 DaxPay 获取用户标识(openId/userId)。
/// 继承 [MerchantPaymentCommonParam] 以复用商户签名验证机制: 所有字段(含继承字段)参与签名。
///
/// ## 签名规则
/// 与支付接口一致: 参数名 ASCII 字典序排序, 空值不参与, 使用商户私钥签名, 平台用商户公钥验签。
/// 详见 [cn.daxpay.open.payment.common.util.PaySignUtil]。
///
/// ## 请求方式
/// GET 重定向, 参数通过 query string 传递。Controller 通过 `@Valid` 对象绑定
/// (Spring `@ModelAttribute`) 自动组装, 校验由字段级 `@NotBlank/@Size/@NotNull` 注解触发。
/// `reqTime` 字段的时间格式解析由基类 [PaymentCommonParam] 的 `@DateTimeFormat` 支撑。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "通用认证请求参数")
public class OpenAuthParam extends MerchantPaymentCommonParam {

    /// 认证类型: wechat / alipay / douyin
    /// @see cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum
    @Schema(description = "认证类型 wechat/alipay/douyin")
    @NotBlank(message = "{validation.field.authType.notBlank}")
    @Size(max = 32, message = "{validation.field.authType.size}")
    private String authType;

    /// 回调地址(获取到用户标识后重定向的目标地址)
    ///
    /// RESTful 风格, 不要在地址后面拼接 query 参数(系统会在后面追加 code/openid/sign 等参数)。
    /// 验签通过后即信任(商户自己指定的回调地址)。
    @Schema(description = "回调地址")
    @NotBlank(message = "{validation.field.redirectUrl.notBlank}")
    @Size(max = 500, message = "{validation.field.redirectUrl.size}")
    private String redirectUrl;
}