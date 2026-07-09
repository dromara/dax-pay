package cn.daxpay.open.payment.merchant.result.wxverify;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 微信域名验证文件
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "微信域名验证文件")
public class WxDomainVerifyResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "是否平台级")
    private boolean platform;

    @Schema(description = "完整文件名")
    private String fileName;

    @Schema(description = "验证码")
    private String verifyCode;

    @Schema(description = "文件内容")
    private String fileContent;

    @Schema(description = "备注")
    private String remark;

}
