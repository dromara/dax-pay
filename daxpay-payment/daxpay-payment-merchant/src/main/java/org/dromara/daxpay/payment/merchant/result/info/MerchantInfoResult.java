package org.dromara.daxpay.payment.merchant.result.info;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.annotation.Trans;
import org.dromara.daxpay.platform.core.enums.merchant.MerchantStatusEnum;
import org.dromara.daxpay.platform.core.enums.subject.SubjectTypeEnum;
import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 商户信息
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "商户信息")
public class MerchantInfoResult extends BaseResult implements ToResult<MerchantInfoResult> {

    /// 商户号
    @Schema(description = "商户号")
    private String mchNo;

    /// 商户名称
    @Schema(description = "商户名称")
    private String mchName;

    /// 商户简称
    @Schema(description = "商户简称")
    private String mchShortName;

    /// 关联管理员用户
    @Schema(description = "关联管理员用户")
    private Long adminUserId;

    /// 主体类型
    /// @see SubjectTypeEnum
    @Schema(description = "主体类型")
    private String subjectType;



    /// 商户状态
    /// @see MerchantStatusEnum
    @Schema(description = "商户状态")
    private String status;

    @Override
    public MerchantInfoResult toResult() {
        return this;
    }
}

