package cn.daxpay.multi.core.param.assist;

import cn.daxpay.multi.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 获取OpenId参数
 * @author xxm
 * @since 2024/9/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "查询OpenId参数")
public class GetOpenIdParam extends PaymentCommonParam {

    @Schema(description = "标识码")
    private String authCode;

}
