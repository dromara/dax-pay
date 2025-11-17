package org.dromara.daxpay.payment.isv.param.isv;

import cn.bootx.platform.core.annotation.QueryParam;
import org.dromara.daxpay.payment.isv.enums.IsvStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 服务商查询参数
 * @author xxm
 * @since 2024/6/24
 */
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Accessors(chain = true)
@Schema(title = "服务商查询参数")
public class IsvInfoQuery {

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /**
     * 状态
     * @see IsvStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /** 服务商号 */
    @Schema(description = "服务商号")
    private String isvNo;
}
