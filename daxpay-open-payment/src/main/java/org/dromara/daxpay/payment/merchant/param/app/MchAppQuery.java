package org.dromara.daxpay.payment.merchant.param.app;

import cn.bootx.platform.core.annotation.QueryParam;
import org.dromara.daxpay.payment.merchant.enums.MchAppStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户应用
 * @author xxm
 * @since 2024/6/24
 */
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "商户应用查询参数")
public class MchAppQuery {

    /** 商户号 */
    @Schema(description = "商户号")
    private String mchNo;

    /** 应用名称 */
    @Schema(description = "应用号")
    private String appId;

    /** 应用名称 */
    @Schema(description = "应用名称")
    private String appName;

    /**
     * 应用状态
     * @see MchAppStatusEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "应用状态")
    private String status;

}
