package org.dromara.daxpay.service.device.entity.qrcode.template;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.bo.qrcode.CashierCodeIcon;
import org.dromara.daxpay.service.device.convert.qrcode.CashierCodeTemplateConvert;
import org.dromara.daxpay.service.device.result.qrcode.template.CashierCodeTemplateResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 收银台码牌模板
 * @author xxm
 * @since 2025/7/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_cashier_code_template",autoResultMap = true)
public class CashierCodeTemplate extends MpBaseEntity implements ToResult<CashierCodeTemplateResult> {

    /** 名称 */
    private String name;

    /** 模板类型 */
    private String type;

    /** 显示ID */
    private Boolean showId;

    /** 显示名称 */
    private Boolean showName;

    /** 显示金额 */
    private Boolean showAmount;

    /** icon列表 */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private CashierCodeIcon icon;

    /** 背景颜色类型 */
    private String bgColorType;

    /** 背景颜色 */
    private String backgroundColor;

    /** 主Logo */
    private String mainLogo;

    /** 二维码Logo */
    private String qrLogo;

    /**
     * 转换
     */
    @Override
    public CashierCodeTemplateResult toResult() {
        return CashierCodeTemplateConvert.CONVERT.toResult(this);
    }

}
