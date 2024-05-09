package cn.bootx.platform.starter.audit.log.core.mongo.convert;

import cn.bootx.platform.starter.audit.log.param.LoginLogParam;
import cn.bootx.platform.starter.audit.log.param.OperateLogParam;
import cn.bootx.platform.starter.audit.log.core.mongo.entity.DataVersionLogMongo;
import cn.bootx.platform.starter.audit.log.core.mongo.entity.LoginLogMongo;
import cn.bootx.platform.starter.audit.log.core.mongo.entity.OperateLogMongo;
import cn.bootx.platform.starter.audit.log.dto.DataVersionLogDto;
import cn.bootx.platform.starter.audit.log.dto.LoginLogDto;
import cn.bootx.platform.starter.audit.log.dto.OperateLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 日志转换
 *
 * @author xxm
 * @since 2021/8/12
 */
@Mapper
public interface LogConvert {

    LogConvert CONVERT = Mappers.getMapper(LogConvert.class);

    OperateLogDto convert(OperateLogMongo in);

    LoginLogDto convert(LoginLogMongo in);

    OperateLogMongo convert(OperateLogParam in);

    LoginLogMongo convert(LoginLogParam in);

    DataVersionLogDto convert(DataVersionLogMongo in);

}
