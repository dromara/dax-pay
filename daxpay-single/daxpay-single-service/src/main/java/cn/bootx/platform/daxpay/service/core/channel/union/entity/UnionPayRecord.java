package cn.bootx.platform.daxpay.service.core.channel.union.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.service.code.UnionPayRecordTypeEnum;
import cn.bootx.platform.daxpay.service.core.channel.union.convert.UnionPayConvert;
import cn.bootx.platform.daxpay.service.dto.channel.union.UnionPayRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 云闪付流水记录
 * @author xxm
 * @since 2024/2/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "云闪付流水记录")
@Accessors(chain = true)
@TableName("pay_union_pay_record")
public class UnionPayRecord extends MpCreateEntity implements EntityBaseFunction<UnionPayRecordDto> {

    /** 标题 */
    @DbColumn(comment = "标题")
    private String title;

    /** 金额 */
    @DbColumn(comment = "金额")
    private Integer amount;

    /**
     * 业务类型
     * @see UnionPayRecordTypeEnum
     */
    @DbColumn(comment = "业务类型")
    private String type;

    /** 本地订单号 */
    @DbColumn(comment = "本地订单号")
    private Long orderId;

    /** 网关订单号 */
    @DbColumn(comment = "网关订单号")
    private String gatewayOrderNo;

    /** 网关完成时间 */
    @DbColumn(comment = "网关完成时间")
    private LocalDateTime gatewayTime;

    /**
     * 转换
     */
    @Override
    public UnionPayRecordDto toDto() {
        return UnionPayConvert.CONVERT.convert(this);
    }
}
