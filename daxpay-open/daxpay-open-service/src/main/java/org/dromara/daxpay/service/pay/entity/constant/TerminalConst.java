package org.dromara.daxpay.service.pay.entity.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.TerminalTypeEnum;
import org.dromara.daxpay.service.pay.convert.constant.TerminalConstConvert;
import org.dromara.daxpay.service.pay.result.constant.TerminalConstResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通道终端报送类型
 * @author xxm
 * @since 2025/3/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_terminal_const")
public class TerminalConst extends MpIdEntity implements ToResult<TerminalConstResult> {

    /** 所属通道 */
    private String channel;

    /**
     * 终端报送类型
     * @see TerminalTypeEnum
     */
    private String type;

    /** 终端报送名称 */
    private String name;

    /** 是否启用 */
    private boolean enable;

    /** 备注 */
    private String remark;

    /**
     * 转换
     */
    @Override
    public TerminalConstResult toResult() {
        return TerminalConstConvert.CONVERT.toResult(this);
    }
}
