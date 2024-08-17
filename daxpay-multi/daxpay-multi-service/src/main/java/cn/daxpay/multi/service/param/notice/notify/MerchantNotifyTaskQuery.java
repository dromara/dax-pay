package cn.daxpay.multi.service.param.notice.notify;

import cn.bootx.platform.core.annotation.QueryParam;
import cn.daxpay.multi.service.enums.NotifyContentTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 客户订阅通知任务
 * @author xxm
 * @since 2024/8/5
 */
@QueryParam
@Data
@Accessors(chain = true)
@Schema(title = "客户订阅通知任务查询参数")
public class MerchantNotifyTaskQuery {

    /** 平台交易号 */
    @QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
    @Schema(description = "平台交易号")
    private String tradeNo;

    /**
     * 通知类型
     * @see NotifyContentTypeEnum
     */
    @Schema(description = "通知类型")
    private String notifyType;

    /** 是否发送成功 */
    @Schema(description = "是否发送成功")
    private Boolean success;

}
