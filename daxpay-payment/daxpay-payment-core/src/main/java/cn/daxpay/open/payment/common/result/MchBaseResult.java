package cn.daxpay.open.payment.common.result;

import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 商户基础返回结果
///
/// 注意：mchName 由 mchNo 通过 @Trans 自动翻译，子类无需重复声明该字段及注解
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户基础返回结果")
public class MchBaseResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Trans(entity = MerchantInfo.class,source = MchBaseResult.Fields.mchNo,result = MerchantInfo.Fields.mchName)
    @Schema(description = "商户名称")
    private String mchName;
}
