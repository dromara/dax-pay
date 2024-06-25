package cn.daxpay.multi.service.entity.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通道常量
 * @author xxm
 * @since 2024/6/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_channel_const")
public class ChannelConst extends MpIdEntity {

    /** 通道编码 */
    private String channel;

    /** 通道名称 */
    private String name;
}
