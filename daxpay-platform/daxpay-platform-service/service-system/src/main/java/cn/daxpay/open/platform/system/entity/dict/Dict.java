package cn.daxpay.open.platform.system.entity.dict;

import cn.daxpay.open.platform.system.convert.dict.DictConvert;
import cn.daxpay.open.platform.system.result.dict.DictResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 字典
///
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_dict")
public class Dict extends MpBaseEntity implements ToResult<DictResult> {

    /// 名称
    private String name;

    /// 国际化key（有值时走语言包翻译）
    private String i18nKey;

    /// 字典类型
    private String dictType;

    /// 编码
    private String code;

    /// 备注
    private String remark;

    /// 是否启用
    private Boolean enable;

    /// 是否内置
    private Boolean internal;

    @Override
    public DictResult toResult() {
        return DictConvert.CONVERT.convert(this);
    }
}
