package org.dromara.daxpay.payment.pay.param.masterdata.capability;

import org.dromara.daxpay.platform.common.mybatisplus.query.entity.SortParam;
import org.dromara.daxpay.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付能力查询参数
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付能力查询参数")
public class PayCapabilityQuery extends SortParam {

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "支付能力编码")
    private String code;
}