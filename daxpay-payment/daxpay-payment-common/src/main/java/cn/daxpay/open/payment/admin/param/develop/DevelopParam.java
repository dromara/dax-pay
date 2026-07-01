package cn.daxpay.open.payment.admin.param.develop;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 开发调试参数包装
///
/// 包装业务参数与调试用手填私钥, 私钥仅用于本次签名生成, 不入库
@Data
@Accessors(chain = true)
@Schema(title = "开发调试参数包装")
public class DevelopParam<T extends MerchantPaymentCommonParam> {

    /// 业务参数
    @Schema(description = "业务参数")
    private T param;

    /// 生成签名使用的私钥(PEM 格式, 调试页手填, 不入库)
    @Schema(description = "生成签名使用的私钥(PEM格式)")
    private String privateKey;
}
