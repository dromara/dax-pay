package org.dromara.daxpay.payment.merchant.param.info;

import org.dromara.daxpay.platform.core.annotation.QueryParam;
import org.dromara.daxpay.platform.core.enums.merchant.MerchantStatusEnum;
import org.dromara.daxpay.platform.core.enums.subject.SubjectTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户查询参数
///
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "商户查询参数")
public class MerchantInfoQuery {

    /// 商户名称
    @Schema(description = "商户名称")
    private String mchName;

    /// 主体类型
    /// @see SubjectTypeEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "主体类型")
    private String subjectType;

    /// 状态
    /// @see MerchantStatusEnum
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "状态")
    private String status;

}

