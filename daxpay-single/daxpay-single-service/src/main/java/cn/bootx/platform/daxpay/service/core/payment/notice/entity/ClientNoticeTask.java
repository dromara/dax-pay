package cn.bootx.platform.daxpay.service.core.payment.notice.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息通知任务
 * @author xxm
 * @since 2024/2/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "消息通知任务")
@TableName("tb_client_notice_task")
public class ClientNoticeTask extends MpCreateEntity {

    /** 本地订单ID */
    @DbColumn(comment = "本地订单ID")
    private Long orderId;

    /**
     * 回调类型
     * @see
     */
    @DbColumn(comment = "回调类型")
    private String type;

    /** 消息内容 */
    @DbColumn(comment = "消息内容")
    private String content;

    /** 是否发送成功 */
    @DbColumn(comment = "是否发送成功")
    private boolean success;

    /** 发送次数 */
    @DbColumn(comment = "发送次数")
    private Integer sendTimes;

    /** 发送地址 */
    @DbColumn(comment = "发送地址")
    private String url;
}
