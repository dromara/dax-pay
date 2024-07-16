package cn.daxpay.single.service.dto.channel.wechat;

import cn.daxpay.single.core.code.AllocReceiverTypeEnum;
import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信分账单状态
 * @author xxm
 * @since 2024/4/16
 */
@Data
@Accessors(chain = true)
public class WeChatPayAllocReceiver {

    /** 分账金额 */
    private Integer amount;

    /** 分账描述 */
    private String description;

    /**
     * 分账接收方类型
     * @see AllocReceiverTypeEnum
     */
    private String type;

    /**
     * 分账接收方账号
     */
    private String account;

    /**
     * 分账结果
     */
    private String result;

    /**
     * 分账完成时间 yyyyMMddHHmmss
     */
    @Alias("finish_time")
    @JsonProperty("finish_time")
    private String finishTime;

    /**
     * 分账失败原因
     */
    @Alias("fail_reason")
    @JsonProperty("fail_reason")
    private String failReason;

    /**
     * 分账明细单号
     */
    @Alias("detail_id")
    @JsonProperty("detail_id")
    private String detailId;

}
