package cn.bootx.platform.baseapi.convert.dict;

import cn.bootx.platform.baseapi.entity.dict.Dictionary;
import cn.bootx.platform.baseapi.entity.dict.DictionaryItem;
import cn.bootx.platform.baseapi.param.dict.DictionaryItemParam;
import cn.bootx.platform.baseapi.param.dict.DictionaryParam;
import cn.bootx.platform.baseapi.result.dict.DictionaryItemResult;
import cn.bootx.platform.baseapi.result.dict.DictionaryResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 字典转换
 *
 * @author xxm
 * @since 2021/7/6
 */
@Mapper
public interface DictionaryConvert {

    DictionaryConvert CONVERT = Mappers.getMapper(DictionaryConvert.class);

    Dictionary convert(DictionaryParam in);

    DictionaryResult convert(Dictionary in);

    DictionaryItem convert(DictionaryItemParam in);

    DictionaryItemResult convert(DictionaryItem in);

    DictionaryItem convertSimple(DictionaryItemParam in);

}
