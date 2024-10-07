package org.dromara.daxpay.service.param.allocation.receiver;

import cn.bootx.platform.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.dromara.daxpay.core.enums.AllocRelationTypeEnum;
import org.dromara.daxpay.service.common.param.MchAppQuery;

/**
 * 分账接收方查询条件
 * @author xxm
 * @since 2024/4/1
 */
@Getter
@Setter
public class AllocReceiverQuery extends MchAppQuery {

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "接收方编号")
    private String receiverNo;

    /**
     * @see org.dromara.daxpay.core.enums.ChannelEnum
     */
    @Schema(description = "所属通道")
    private String channel;

    /**
     * 分账接收方类型 个人/商户
     */
    @Schema(description = "分账接收方类型")
    private String receiverType;

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "接收方账号")
    private String receiverAccount;

    /** 接收方姓名 */
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "接收方姓名")
    private String receiverName;

    /**
     * 分账关系类型
     * @see AllocRelationTypeEnum
     */
    @Schema(description = "分账关系类型")
    private String relationType;
}
