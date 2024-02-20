package cn.bootx.platform.daxpay.service.core.channel.alipay.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.service.code.AliPayRecordTypeEnum;
import cn.bootx.platform.daxpay.service.core.channel.alipay.convert.AlipayConvert;
import cn.bootx.platform.daxpay.service.dto.channel.alipay.AliPayRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付宝流水记录
 * @author xxm
 * @since 2024/2/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付宝流水记录")
@TableName("pay_alipay_record")
public class AliPayRecord extends MpCreateEntity implements EntityBaseFunction<AliPayRecordDto> {

    /** 标题 */
    @DbColumn(comment = "标题")
    private String title;

    /** 金额 */
    @DbColumn(comment = "金额")
    private Integer amount;

    /**
     * 业务类型
     * @see AliPayRecordTypeEnum
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
    public AliPayRecordDto toDto() {
        return AlipayConvert.CONVERT.convert(this);
    }
}
