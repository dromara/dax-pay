package cn.bootx.platform.starter.audit.log.convert;

import cn.bootx.platform.starter.audit.log.entity.LoginLogDb;
import cn.bootx.platform.starter.audit.log.entity.OperateLogDb;
import cn.bootx.platform.starter.audit.log.result.LoginLogResult;
import cn.bootx.platform.starter.audit.log.result.OperateLogResult;
import cn.bootx.platform.starter.audit.log.param.LoginLogParam;
import cn.bootx.platform.starter.audit.log.param.OperateLogParam;
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

    OperateLogResult convert(OperateLogDb in);

    LoginLogResult convert(LoginLogDb in);

    OperateLogDb convert(OperateLogParam in);

    LoginLogDb convert(LoginLogParam in);

}
