package org.dromara.daxpay.payment.merchant.param.onboarded;

import cn.bootx.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 进件商户信息查询参数
 * @author xxm
 * @since 2025/11/11
 */
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "进件商户信息查询参数")
public class OnbMchInfoQuery {

    /** 商户号 */
    @Schema(description = "商户号")
    private String mchNo;

    /** 进件商户号 */
    @Schema(description = "进件商户号")
    private String onbMchNo;

    /** 商户名称 */
    @Schema(description = "商户名称")
    private String onbMchName;

    /** 所属通道 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "所属通道")
    private String onbChannel;
}