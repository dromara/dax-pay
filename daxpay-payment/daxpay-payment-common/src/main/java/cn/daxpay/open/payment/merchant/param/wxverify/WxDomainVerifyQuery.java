package cn.daxpay.open.payment.merchant.param.wxverify;

import cn.daxpay.open.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信域名验证文件查询参数
///
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "微信域名验证文件查询参数")
public class WxDomainVerifyQuery {

    /// 商户号
    @Schema(description = "商户号")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String mchNo;

    /// 是否平台级
    @Schema(description = "是否平台级")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private Boolean platform;

    /// 文件名
    @Schema(description = "文件名")
    private String fileName;

    /// 验证码
    @Schema(description = "验证码")
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    private String verifyCode;

}
