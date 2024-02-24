package cn.bootx.platform.daxpay.service.core.task.notice.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.service.code.ClientNoticeTypeEnum;
import cn.bootx.platform.daxpay.service.core.task.notice.convert.ClientNoticeConvert;
import cn.bootx.platform.daxpay.service.dto.record.notice.ClientNoticeTaskDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 消息通知任务
 * @author xxm
 * @since 2024/2/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "消息通知任务")
@TableName("pay_client_notice_task")
public class ClientNoticeTask extends MpBaseEntity implements EntityBaseFunction<ClientNoticeTaskDto> {

    /** 本地订单ID */
    @DbColumn(comment = "本地订单ID")
    private Long orderId;

    /**
     * 消息类型
     * @see ClientNoticeTypeEnum
     */
    @DbColumn(comment = "消息类型")
    private String type;

    /** 消息内容 */
    @DbColumn(comment = "消息内容")
    private String content;

    /** 是否发送成功 */
    @DbColumn(comment = "是否发送成功")
    private boolean success;

    /** 发送次数 */
    @DbColumn(comment = "发送次数")
    private Integer sendCount;

    /** 发送地址 */
    @DbColumn(comment = "发送地址")
    private String url;

    /** 最后发送时间 */
    @DbColumn(comment = "最后发送时间")
    private LocalDateTime latestTime;

    /**
     * 转换
     */
    @Override
    public ClientNoticeTaskDto toDto() {
        return ClientNoticeConvert.CONVERT.convert(this);
    }
}
