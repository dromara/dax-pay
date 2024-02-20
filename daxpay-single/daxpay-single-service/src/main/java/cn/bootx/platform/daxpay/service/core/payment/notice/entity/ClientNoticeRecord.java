package cn.bootx.platform.daxpay.service.core.payment.notice.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
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
public class ClientNoticeRecord extends MpCreateEntity {
}
