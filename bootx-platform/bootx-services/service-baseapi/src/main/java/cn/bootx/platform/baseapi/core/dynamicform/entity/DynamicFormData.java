package cn.bootx.platform.baseapi.core.dynamicform.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2022/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "动态表单数据")
public class DynamicFormData extends MpBaseEntity {

    /** 表单id */
    private Long formId;

    /** 内容 */

}
