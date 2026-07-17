package cn.daxpay.open.payment.merchant.result.wxverify;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
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

    /// 商户名称(由 mchNo 翻译, 走系统 @Trans 机制; 平台级记录无商户号则为空)
    @Trans(
            entity = MerchantInfo.class,
            source = MchBaseResult.Fields.mchNo,
            result = MerchantInfo.Fields.mchName)
    @Schema(description = "商户名称")
    private String mchName;

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
