package cn.bootx.platform.baseapi.core.dict.entity;

import cn.bootx.platform.baseapi.core.dict.convert.DictionaryConvert;
import cn.bootx.platform.baseapi.dto.dict.DictionaryItemDto;
import cn.bootx.platform.baseapi.dto.dict.DictionaryItemSimpleDto;
import cn.bootx.platform.baseapi.param.dict.DictionaryItemParam;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 字典项
 *
 * @author xxm
 * @since 2020/4/15 17:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("base_dict_item")
public class DictionaryItem extends MpBaseEntity implements EntityBaseFunction<DictionaryItemDto> {

    /** 字典ID */
    private Long dictId;

    /** 字典编码 */
    private String dictCode;

    /** 字典项编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 字典项排序 */
    private Double sortNo;

    /** 是否启用 */
    private Boolean enable;

    /** 备注 */
    private String remark;

    public static DictionaryItem init(DictionaryItemParam in) {
        return DictionaryConvert.CONVERT.convert(in);
    }

    @Override
    public DictionaryItemDto toDto() {
        return DictionaryConvert.CONVERT.convert(this);
    }

    /**
     * 转换成简单对象
     */
    public DictionaryItemSimpleDto toSimpleDto() {
        return DictionaryConvert.CONVERT.convertSimple(this);

    }

}
