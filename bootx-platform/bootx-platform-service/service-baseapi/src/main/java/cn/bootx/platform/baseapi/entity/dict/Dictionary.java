package cn.bootx.platform.baseapi.entity.dict;

import cn.bootx.platform.baseapi.convert.dict.DictionaryConvert;
import cn.bootx.platform.baseapi.result.dict.DictionaryResult;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
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
public class Dictionary extends MpBaseEntity implements ToResult<DictionaryResult> {

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

    @Override
    public DictionaryResult toResult() {
        return DictionaryConvert.CONVERT.convert(this);
    }
}
