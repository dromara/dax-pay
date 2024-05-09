package cn.bootx.platform.baseapi.core.dynamicsource.convert;

import cn.bootx.platform.baseapi.dto.dynamicsource.DynamicDataSourceDto;
import cn.bootx.platform.baseapi.param.dynamicsource.DynamicDataSourceParam;
import cn.bootx.platform.baseapi.core.dynamicsource.entity.DynamicDataSource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 动态数据源管理
 *
 * @author xxm
 * @since 2022-09-24
 */
@Mapper
public interface DynamicDataSourceConvert {

    DynamicDataSourceConvert CONVERT = Mappers.getMapper(DynamicDataSourceConvert.class);

    DynamicDataSource convert(DynamicDataSourceParam in);

    DynamicDataSourceDto convert(DynamicDataSource in);

}
