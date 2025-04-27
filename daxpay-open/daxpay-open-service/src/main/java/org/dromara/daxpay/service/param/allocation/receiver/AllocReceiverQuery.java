package org.dromara.daxpay.service.param.allocation.receiver;

import cn.bootx.platform.core.annotation.QueryParam;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.common.param.MchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 分账接收方查询条件
 * @author xxm
 * @since 2024/4/1
 */
@Getter
@Setter
public class AllocReceiverQuery extends MchQuery {

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "接收方编号")
    private String receiverNo;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "接收方名称")
    private String receiverName;

    /**
     * 分账通道
     * @see ChannelEnum
     */
    @Schema(description = "分账通道")
    private String channel;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "接收方账号")
    private String receiverAccount;

    @QueryParam(type = QueryParam.CompareTypeEnum.EQ)
    @Schema(description = "接收方类型")
    private String receiverType;

}
