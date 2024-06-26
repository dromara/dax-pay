package cn.daxpay.single.service.core.notice.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import cn.daxpay.single.service.code.ClientNoticeSendTypeEnum;
import cn.daxpay.single.service.core.notice.convert.ClientNoticeConvert;
import cn.daxpay.single.service.dto.record.notice.ClientNoticeRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息通知任务记录表
 * @author xxm
 * @since 2024/2/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "消息通知任务记录")
@TableName("pay_client_notice_record")
public class ClientNoticeRecord extends MpCreateEntity implements EntityBaseFunction<ClientNoticeRecordDto> {

    /** 任务ID */
    @DbMySqlIndex(comment = "任务ID索引")
    @DbColumn(comment = "任务ID", isNull = false)
    private Long taskId;

    /** 请求次数 */
    @DbColumn(comment = "请求次数", length = 3)
    private Integer reqCount;

    /** 发送是否成功 */
    @DbColumn(comment = "发送是否成功", isNull = false)
    private boolean success;

    /**
     * 发送类型, 自动发送, 手动发送
     * @see ClientNoticeSendTypeEnum
     */
    @DbColumn(comment = "发送类型", length = 20, isNull = false)
    private String sendType;

    /** 错误码 */
    @DbColumn(comment = "错误码", length = 10)
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息", length = 150)
    private String errorMsg;

    /**
     * 转换
     */
    @Override
    public ClientNoticeRecordDto toDto() {
        return ClientNoticeConvert.CONVERT.convert(this);
    }
}
