package org.dromara.daxpay.service.device.entity.qrcode.info;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.device.convert.qrcode.CashierCodeConvert;
import org.dromara.daxpay.service.device.result.qrcode.info.CashierCodeResult;
import org.dromara.daxpay.service.pay.common.entity.MchAppEditEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 收款码牌
 * @author xxm
 * @since 2025/7/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_cashier_code")
public class CashierCode extends MchAppEditEntity implements ToResult<CashierCodeResult> {

    /** 金额类型 固定金额/任意金额 */
    private String amountType;

    /** 金额 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal amount;

    /** 名称 */
    private String name;

    /** 编号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;

    /** 配置Id */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long configId;

    /** 模板ID */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long templateId;

    /** 状态 */
    private Boolean enable;

    /** 批次号 */
    private String batchNo;

    public Boolean getEnable() {
        return Objects.equals(enable, true);
    }

    /**
     * 转换
     */
    @Override
    public CashierCodeResult toResult() {
        return CashierCodeConvert.CONVERT.toResult(this);
    }
}
