package cn.bootx.platform.daxpay.service.param.record;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import cn.bootx.platform.daxpay.service.code.ClientNoticeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 发送通知任务查询
 * @author xxm
 * @since 2024/2/24
 */
@QueryParam
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "发送通知任务查询")
public class ClientNoticeTaskQuery extends QueryOrder {

    /** 本地订单ID */
    @Schema(description ="本地订单ID")
    private Long orderId;

    /**
     * 通知类型
     * @see ClientNoticeTypeEnum
     */
    @Schema(description ="通知类型")
    private String type;

    /** 消息内容 */
    @Schema(description ="消息内容")
    private String content;

    /** 是否发送成功 */
    @Schema(description ="是否发送成功")
    private Boolean success;

    /** 发送次数 */
    @Schema(description ="发送次数")
    private Integer sendCount;

    /** 发送地址 */
    @Schema(description ="发送地址")
    private String url;
}
