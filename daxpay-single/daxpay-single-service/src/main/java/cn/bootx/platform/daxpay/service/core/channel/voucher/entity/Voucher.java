package cn.bootx.platform.daxpay.service.core.channel.voucher.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.service.code.VoucherStatusEnum;
import cn.bootx.platform.daxpay.service.core.channel.voucher.convert.VoucherConvert;
import cn.bootx.platform.daxpay.service.dto.channel.voucher.VoucherDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 储值卡
 *
 * @author xxm
 * @since 2022/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "储值卡")
@Accessors(chain = true)
@TableName("pay_voucher")
public class Voucher extends MpBaseEntity implements EntityBaseFunction<VoucherDto> {

    /** 卡号 */
    @DbColumn(comment = "卡号")
    @DbMySqlIndex(comment = "卡号索引")
    private String cardNo;

    /** 面值 */
    @DbColumn(comment = "面值")
    private Integer faceValue;

    /** 余额 */
    @DbColumn(comment = "余额")
    private Integer balance;


    /** 是否长期有效 */
    @DbColumn(comment = "是否长期有效")
    private boolean enduring;

    /** 开始时间 */
    @DbColumn(comment = "开始时间")
    private LocalDateTime startTime;

    /** 结束时间 */
    @DbColumn(comment = "结束时间")
    private LocalDateTime endTime;

    /**
     * 状态
     * @see VoucherStatusEnum
     */
    @DbColumn(comment = "状态")
    private String status;

    @Override
    public VoucherDto toDto() {
        return VoucherConvert.CONVERT.convert(this);
    }

}
