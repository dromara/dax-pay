package org.dromara.daxpay.channel.alipay.param.allocation;

import cn.bootx.platform.core.annotation.QueryParam;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.service.common.param.MchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付宝分账接收方绑定查询
 * @author xxm
 * @since 2025/2/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝分账接收方绑定查询")
public class AlipayAllocReceiverBindQuery extends MchQuery {

    /**
     * 接收方类型
     * @see AllocReceiverTypeEnum
     */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "接收方类型")
    private String receiverType;

    /** 接收方账号 */
    @Schema(description = "接收方账号")
    private String receiverAccount;

    /** 接收方名称 */
    @Schema(description = "接收方名称")
    private String receiverName;

    /** 分账关系类型 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "分账关系类型")
    private String relationType;

    /** 分账关系名称 */
    @Schema(description = "分账关系名称")
    private String relationName;

    /** 是否服务商模式 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否服务商模式")
    private Boolean isv;

    /** 是否绑定 */
    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "是否绑定")
    private Boolean bind;

}
