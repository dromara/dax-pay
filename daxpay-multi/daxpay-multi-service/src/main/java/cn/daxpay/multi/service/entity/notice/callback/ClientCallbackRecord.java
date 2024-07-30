package cn.daxpay.multi.service.entity.notice.callback;

import cn.daxpay.multi.service.enums.SendTypeEnum;
import cn.daxpay.multi.service.common.entity.MchRecordEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 客户回调消息发送记录
 * @author xxm
 * @since 2024/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_client_callback_record")
public class ClientCallbackRecord extends MchRecordEntity {

    /** 任务ID */
    private Long taskId;

    /** 请求次数 */
    private Integer reqCount;

    /** 发送是否成功 */
    private boolean success;

    /**
     * 发送类型, 自动发送, 手动发送
     * @see SendTypeEnum
     */
    private String sendType;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;
}
