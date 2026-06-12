package org.dromara.daxpay.payment.unipay.param.assist;

import org.dromara.daxpay.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 查询OpenId参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "查询OpenId参数")
public class QueryAuthParam extends MerchantPaymentCommonParam {

    @Schema(description = "标识码")
    private String queryCode;

}
