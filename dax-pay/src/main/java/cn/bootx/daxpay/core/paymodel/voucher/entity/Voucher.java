package cn.bootx.daxpay.core.paymodel.voucher.entity;

import cn.bootx.common.core.function.EntityBaseFunction;
import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.daxpay.code.paymodel.VoucherCode;
import cn.bootx.daxpay.core.paymodel.voucher.convert.VoucherConvert;
import cn.bootx.daxpay.dto.paymodel.voucher.VoucherDto;
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
 * @date 2022/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_voucher")
public class Voucher extends MpBaseEntity implements EntityBaseFunction<VoucherDto> {

    /** 卡号 */
    private String cardNo;

    /** 生成批次号 */
    private Long batchNo;

    /** 面值 */
    private BigDecimal faceValue;

    /** 余额 */
    private BigDecimal balance;

    /** 是否长期有效 */
    private Boolean enduring;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

    /**
     * 状态
     * @see VoucherCode
     */
    private Integer status;

    @Override
    public VoucherDto toDto() {
        return VoucherConvert.CONVERT.convert(this);
    }

}
