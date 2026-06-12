package org.dromara.daxpay.platform.system.convert.dict;

import org.dromara.daxpay.platform.system.entity.dict.Dict;
import org.dromara.daxpay.platform.system.entity.dict.DictItem;
import org.dromara.daxpay.platform.system.param.dict.DictItemParam;
import org.dromara.daxpay.platform.system.param.dict.DictParam;
import org.dromara.daxpay.platform.system.result.dict.DictItemResult;
import org.dromara.daxpay.platform.system.result.dict.DictResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 字典转换
///
@Mapper
public interface DictConvert {

    DictConvert CONVERT = Mappers.getMapper(DictConvert.class);

    /// 参数转实体
    ///
    /// @param in 字典参数
    /// @return 字典实体
    Dict convert(DictParam in);

    /// 实体转结果
    ///
    /// @param in 字典实体
    /// @return 字典结果
    DictResult convert(Dict in);

    /// 参数转实体
    ///
    /// @param in 字典项参数
    /// @return 字典项实体
    DictItem convert(DictItemParam in);

    /// 实体转结果
    ///
    /// @param in 字典项实体
    /// @return 字典项结果
    DictItemResult convert(DictItem in);

    /// 复制属性
    ///
    /// @param param 字典参数
    /// @param dict 字典实体
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(DictParam param, @MappingTarget Dict dict);

    /// 复制属性
    ///
    /// @param param 字典项参数
    /// @param dictItem 字典项实体
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(DictItemParam param, @MappingTarget DictItem dictItem);
}


