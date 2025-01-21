package cn.bootx.platform.baseapi.entity.parameter;

import cn.bootx.platform.baseapi.convert.parameter.SystemConvert;
import cn.bootx.platform.baseapi.param.parameter.SystemParameterParam;
import cn.bootx.platform.baseapi.result.parameter.SystemParameterResult;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统参数
 *
 * @author xxm
 * @since 2021/10/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("base_param")
public class SystemParameter extends MpBaseEntity implements ToResult<SystemParameterResult> {

    /** 参数名称 */
    private String name;

    /** 参数键名 */
    private String paramKey;

    /** 参数值 */
    private String value;

    /** 参数类型 */
    private String type;

    /** 是否启用 */
    private Boolean enable;

    /** 内置参数 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private boolean internal;

    /** 备注 */
    private String remark;

    @Override
    public SystemParameterResult toResult() {
        return SystemConvert.CONVERT.convert(this);
    }

    public static SystemParameter init(SystemParameterParam in) {
        return SystemConvert.CONVERT.convert(in);
    }

}
