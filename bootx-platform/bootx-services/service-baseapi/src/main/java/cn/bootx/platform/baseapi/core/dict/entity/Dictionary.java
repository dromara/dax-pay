package cn.bootx.platform.baseapi.core.dict.entity;

import cn.bootx.platform.baseapi.core.dict.convert.DictionaryConvert;
import cn.bootx.platform.baseapi.dto.dict.DictionaryDto;
import cn.bootx.platform.baseapi.param.dict.DictionaryParam;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典
 *
 * @author xxm
 * @since 2020/11/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_dict")
public class Dictionary extends MpBaseEntity implements EntityBaseFunction<DictionaryDto> {

    /** 名称 */
    private String name;

    /** 分类标签 */
    private String groupTag;

    /** 编码 */
    private String code;

    /** 备注 */
    private String remark;

    /** 是否启用 */
    private Boolean enable;

    public static Dictionary init(DictionaryParam in) {
        return DictionaryConvert.CONVERT.convert(in);
    }

    @Override
    public DictionaryDto toDto() {
        return DictionaryConvert.CONVERT.convert(this);
    }

}
