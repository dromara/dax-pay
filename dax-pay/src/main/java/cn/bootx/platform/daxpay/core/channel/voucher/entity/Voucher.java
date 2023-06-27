package cn.bootx.platform.daxpay.core.channel.voucher.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbComment;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlIndex;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.paymodel.VoucherCode;
import cn.bootx.platform.daxpay.core.channel.voucher.convert.VoucherConvert;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
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
    @DbComment("卡号")
    @DbMySqlIndex(comment = "卡号索引")
    private String cardNo;

    /** 生成批次号 */
    @DbComment("生成批次号")
    private Long batchNo;

    /** 面值 */
    @DbComment("面值")
    private BigDecimal faceValue;

    /** 余额 */
    @DbComment("余额")
    private BigDecimal balance;

    /** 预冻结额度 */
    @DbColumn(comment = "预冻结额度")
    private BigDecimal freezeBalance;


    /** 是否长期有效 */
    @DbComment("是否长期有效")
    private boolean enduring;

    /** 开始时间 */
    @DbComment("开始时间")
    private LocalDateTime startTime;

    /** 结束时间 */
    @DbComment("结束时间")
    private LocalDateTime endTime;

    /**
     * 状态
     * @see VoucherCode#STATUS_FORBIDDEN
     */
    @DbComment("状态")
    private String status;

    @Override
    public VoucherDto toDto() {
        return VoucherConvert.CONVERT.convert(this);
    }

}
