package cn.bootx.platform.daxpay.service.core.channel.wechat.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.service.code.WechatPayRecordTypeEnum;
import cn.bootx.platform.daxpay.service.core.channel.wechat.convert.WeChatConvert;
import cn.bootx.platform.daxpay.service.dto.channel.wechat.WeChatPayRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
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
public class WeChatPayRecord extends MpCreateEntity implements EntityBaseFunction<WeChatPayRecordDto> {

    /** 标题 */
    @DbColumn(comment = "标题")
    private String title;

    /** 金额 */
    @DbColumn(comment = "金额")
    private Integer amount;

    /**
     * 业务类型
     * @see WechatPayRecordTypeEnum
     */
    @DbColumn(comment = "业务类型")
    private String type;

    /** 本地订单号 */
    @DbColumn(comment = "本地订单号")
    private Long orderId;

    /** 网关订单号 */
    @DbColumn(comment = "网关订单号")
    private String gatewayOrderNo;

    @Override
    public WeChatPayRecordDto toDto() {
        return WeChatConvert.CONVERT.convert(this);
    }
}
