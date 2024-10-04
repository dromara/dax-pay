package org.dromara.daxpay.core.param.assist;

import org.dromara.daxpay.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询OpenId参数
 * @author xxm
 * @since 2024/9/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "查询OpenId参数")
public class QueryAuthParam extends PaymentCommonParam {

    @Schema(description = "标识码")
    private String queryCode;

}
