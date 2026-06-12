package org.dromara.daxpay.platform.system.entity.dict;

import org.dromara.daxpay.platform.system.convert.dict.DictConvert;
import org.dromara.daxpay.platform.system.result.dict.DictItemResult;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 字典项
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("system_dict_item")
public class DictItem extends MpBaseEntity implements ToResult<DictItemResult> {

    /// 字典ID
    private Long dictId;

    /// 字典编码
    private String dictCode;

    /// 字典项编码
    private String code;

    /// 中文名称
    private String nameCn;

    /// 英文名称
    private String nameEn;

    /// 字典项排序
    private Integer sortNo;

    /// 是否启用
    private Boolean enable;

    /// 备注
    private String remark;

    @Override
    public DictItemResult toResult() {
        return DictConvert.CONVERT.convert(this);
    }
}
