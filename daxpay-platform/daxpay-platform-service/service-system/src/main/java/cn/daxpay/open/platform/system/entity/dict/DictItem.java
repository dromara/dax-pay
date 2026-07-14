package cn.daxpay.open.platform.system.entity.dict;

import cn.daxpay.open.platform.system.convert.dict.DictConvert;
import cn.daxpay.open.platform.system.result.dict.DictItemResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
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

    /// 国际化key（有值时走语言包翻译）
    private String i18nKey;

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
