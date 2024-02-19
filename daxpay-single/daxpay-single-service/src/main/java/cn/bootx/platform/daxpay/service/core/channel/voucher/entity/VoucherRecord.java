package cn.bootx.platform.daxpay.service.core.channel.voucher.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.service.code.VoucherRecordTypeEnum;
import cn.bootx.platform.daxpay.service.core.channel.voucher.convert.VoucherConvert;
import cn.bootx.platform.daxpay.service.dto.channel.voucher.VoucherRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 储值卡记录
 * @author xxm
 * @since 2023/6/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_voucher_record")
@DbTable(comment = "储值卡记录")
public class VoucherRecord extends MpCreateEntity implements EntityBaseFunction<VoucherRecordDto> {

    /** 储值卡id */
    @DbMySqlIndex(comment = "储值卡ID")
    @DbColumn(comment = "储值卡id")
    private Long voucherId;

    /** 标题 */
    @DbColumn(comment = "标题")
    private String title;

    /**
     * 业务类型
     * @see VoucherRecordTypeEnum
     */
    @DbColumn(comment = "业务类型")
    private String type;

    /** 金额 */
    @DbColumn(comment = "金额")
    private Integer amount;

    /** 变动之前的金额 */
    @DbColumn(comment = "变动之前的金额")
    private Integer oldAmount;

    /** 变动之后的金额 */
    @DbColumn(comment = "变动之后的金额")
    private Integer newAmount;

    /**
     * 交易订单号
     * 支付订单/退款订单
     */
    @DbColumn(comment = "交易订单号")
    private String orderId;

    /** 终端ip */
    @DbColumn(comment = "终端ip")
    private String ip;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;


    /**
     * 转换
     */
    @Override
    public VoucherRecordDto toDto() {
        return VoucherConvert.CONVERT.convert(this);
    }
}
