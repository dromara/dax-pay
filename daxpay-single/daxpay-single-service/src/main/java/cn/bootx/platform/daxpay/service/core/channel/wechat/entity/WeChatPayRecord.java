package cn.bootx.platform.daxpay.service.core.channel.wechat.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信支付记录
 * @author xxm
 * @since 2024/2/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "微信支付记录")
@Accessors(chain = true)
@TableName("pay_wechat_pay_record")
public class WeChatPayRecord extends MpCreateEntity {
}
