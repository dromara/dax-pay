package cn.bootx.platform.baseapi.entity.dict;

import cn.bootx.platform.baseapi.convert.dict.DictionaryConvert;
import cn.bootx.platform.baseapi.result.dict.DictionaryItemResult;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
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
public class DictionaryItem extends MpBaseEntity implements ToResult<DictionaryItemResult> {

    /** 字典ID */
    private Long dictId;

    /** 字典编码 */
    private String dictCode;

    /** 字典项编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 字典项排序 */
    private Integer sortNo;

    /** 是否启用 */
    private Boolean enable;

    /** 备注 */
    private String remark;


    @Override
    public DictionaryItemResult toResult() {
        return DictionaryConvert.CONVERT.convert(this);
    }
}
