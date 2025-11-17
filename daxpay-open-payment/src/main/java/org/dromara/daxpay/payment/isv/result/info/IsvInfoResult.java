package org.dromara.daxpay.payment.isv.result.info;

import cn.bootx.platform.core.result.BaseResult;
import org.dromara.daxpay.payment.isv.enums.IsvStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 服务商信息
 * @author xxm
 * @since 2024/10/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@Schema(title = "服务商信息")
public class IsvInfoResult extends BaseResult {

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 简称 */
    @Schema(description = "简称")
    private String shortName;

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

