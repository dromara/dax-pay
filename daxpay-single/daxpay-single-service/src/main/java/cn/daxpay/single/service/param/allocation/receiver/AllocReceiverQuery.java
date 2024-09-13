package cn.daxpay.single.service.param.allocation.receiver;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.daxpay.single.core.code.AllocRelationTypeEnum;
import cn.daxpay.single.core.code.PayChannelEnum;
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
public class AllocReceiverQuery {

    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "接收方编号")
    private String receiverNo;

    /**
     * @see PayChannelEnum
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
