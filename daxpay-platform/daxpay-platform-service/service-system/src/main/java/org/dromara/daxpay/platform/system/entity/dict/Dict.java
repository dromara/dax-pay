package org.dromara.daxpay.platform.system.entity.dict;

import org.dromara.daxpay.platform.system.convert.dict.DictConvert;
import org.dromara.daxpay.platform.system.result.dict.DictResult;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
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

    /// 中文名称
    private String nameCn;

    /// 英文名称
    private String nameEn;

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
