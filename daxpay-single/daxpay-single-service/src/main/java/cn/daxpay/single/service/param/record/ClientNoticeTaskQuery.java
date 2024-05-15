package cn.daxpay.single.service.param.record;

import cn.bootx.platform.common.core.annotation.QueryParam;
import cn.bootx.platform.common.core.rest.param.QueryOrder;
import cn.daxpay.single.service.code.ClientNoticeTypeEnum;
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

    /** 本地交易号 */
    @Schema(description = "本地交易号")
    private String tradeNo;

    /**
     * 回调类型
     * @see ClientNoticeTypeEnum
     */
    @Schema(description = "回调类型")
    private String noticeType;
}
