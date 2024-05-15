package cn.bootx.platform.baseapi.core.dict.convert;

import cn.bootx.platform.baseapi.dto.dict.DictionaryDto;
import cn.bootx.platform.baseapi.dto.dict.DictionaryItemDto;
import cn.bootx.platform.baseapi.dto.dict.DictionaryItemSimpleDto;
import cn.bootx.platform.baseapi.param.dict.DictionaryItemParam;
import cn.bootx.platform.baseapi.param.dict.DictionaryParam;
import cn.bootx.platform.baseapi.core.dict.entity.Dictionary;
import cn.bootx.platform.baseapi.core.dict.entity.DictionaryItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 渠道转换
 *
 * @author xxm
 * @since 2021/7/6
 */
@Mapper
public interface DictionaryConvert {

    DictionaryConvert CONVERT = Mappers.getMapper(DictionaryConvert.class);

    Dictionary convert(DictionaryParam in);

    DictionaryDto convert(Dictionary in);

    DictionaryItem convert(DictionaryItemParam in);

    DictionaryItemDto convert(DictionaryItem in);

    DictionaryItemSimpleDto convertSimple(DictionaryItem in);

}
