package cn.daxpay.single.service.core.notice.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.core.result.order.AllocOrderResult;
import cn.daxpay.single.service.code.ClientNoticeTypeEnum;
import cn.daxpay.single.service.core.notice.convert.ClientNoticeConvert;
import cn.daxpay.single.service.core.payment.notice.result.PayNoticeResult;
import cn.daxpay.single.service.core.payment.notice.result.RefundNoticeResult;
import cn.daxpay.single.service.dto.record.notice.ClientNoticeTaskDto;
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

    /** 本地交易ID */
    @DbColumn(comment = "本地交易ID", isNull = false)
    private Long tradeId;

    /** 本地交易号 */
    @DbMySqlIndex(comment = "本地交易号索引")
    @DbColumn(comment = "本地交易号", length = 32, isNull = false)
    private String tradeNo;

    /**
     * 消息类型
     * @see ClientNoticeTypeEnum
     */
    @DbColumn(comment = "消息类型", length = 20, isNull = false)
    private String noticeType;

    /**
     * 交易状态
     * @see PayStatusEnum
     * @see RefundStatusEnum
     */
    @DbColumn(comment = "交易状态", length = 20, isNull = false)
    private String tradeStatus;

    /**
     * 消息内容
     * @see PayNoticeResult
     * @see RefundNoticeResult
     * @see AllocOrderResult
     */
    @DbColumn(comment = "消息内容", isNull = false)
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    private String content;

    /** 是否发送成功 */
    @DbColumn(comment = "是否发送成功", isNull = false)
    private boolean success;

    /** 发送次数 */
    @DbColumn(comment = "发送次数", length = 3, isNull = false)
    private Integer sendCount;

    /** 发送地址 */
    @DbColumn(comment = "发送地址", length = 150, isNull = false)
    private String url;

    /** 最后发送时间 */
    @DbColumn(comment = "最后发送时间", isNull = false)
    private LocalDateTime latestTime;

    /**
     * 转换
     */
    @Override
    public ClientNoticeTaskDto toDto() {
        return ClientNoticeConvert.CONVERT.convert(this);
    }
}
