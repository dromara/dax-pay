package cn.bootx.platform.baseapi.core.dynamicform.entity;

import cn.bootx.platform.baseapi.core.dynamicform.convert.DynamicFormConvert;
import cn.bootx.platform.baseapi.dto.dynamicform.DynamicFormDto;
import cn.bootx.platform.baseapi.param.dynamicform.DynamicFormParam;
import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 动态表单
 *
 * @author xxm
 * @since 2022-07-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "base_dynamic_form", autoResultMap = true)
public class DynamicForm extends MpBaseEntity implements EntityBaseFunction<DynamicFormDto> {

    /** 表单名称 */
    private String name;

    /** 表单键名 */
    private String code;

    /** 表单内容 */
    @BigField
    private String value;

    /** 备注 */
    private String remark;

    /** 创建对象 */
    public static DynamicForm init(DynamicFormParam in) {
        return DynamicFormConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public DynamicFormDto toDto() {
        return DynamicFormConvert.CONVERT.convert(this);
    }

}
