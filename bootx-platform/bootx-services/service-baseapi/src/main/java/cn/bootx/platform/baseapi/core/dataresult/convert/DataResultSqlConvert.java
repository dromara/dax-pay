package cn.bootx.platform.baseapi.core.dataresult.convert;

import cn.bootx.platform.baseapi.core.dataresult.entity.DataResultSql;
import cn.bootx.platform.baseapi.dto.dataresult.QuerySqlDto;
import cn.bootx.platform.baseapi.param.dataresult.DataResultSqlParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2023/3/13
 */
@Mapper
public interface DataResultSqlConvert {

    DataResultSqlConvert CONVERT = Mappers.getMapper(DataResultSqlConvert.class);

    QuerySqlDto convert(DataResultSql in);

    DataResultSql convert(DataResultSqlParam in);

}
