package cn.bootx.platform.baseapi.core.chinaword.convert;

import cn.bootx.platform.baseapi.core.chinaword.entity.ChinaWord;
import cn.bootx.platform.baseapi.dto.chinaword.ChinaWordDto;
import cn.bootx.platform.baseapi.param.chinaword.ChinaWordParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 敏感词
 * @author xxm
 * @since 2023-08-09
 */
@Mapper
public interface ChinaWordConvert {
    ChinaWordConvert CONVERT = Mappers.getMapper(ChinaWordConvert.class);

    ChinaWord convert(ChinaWordParam in);

    ChinaWordDto convert(ChinaWord in);

}
