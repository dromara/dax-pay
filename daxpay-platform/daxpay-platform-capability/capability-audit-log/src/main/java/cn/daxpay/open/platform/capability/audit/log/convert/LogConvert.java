package cn.daxpay.open.platform.capability.audit.log.convert;

import cn.daxpay.open.platform.capability.audit.log.entity.LoginLogDb;
import cn.daxpay.open.platform.capability.audit.log.entity.OperateLogDb;
import cn.daxpay.open.platform.capability.audit.log.result.LoginLogResult;
import cn.daxpay.open.platform.capability.audit.log.result.OperateLogResult;
import cn.daxpay.open.platform.capability.audit.log.param.LoginLogParam;
import cn.daxpay.open.platform.capability.audit.log.param.OperateLogParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/// # 日志转换
///
@Mapper
public interface LogConvert {

    LogConvert CONVERT = Mappers.getMapper(LogConvert.class);

    OperateLogResult convert(OperateLogDb in);

    LoginLogResult convert(LoginLogDb in);

    OperateLogDb convert(OperateLogParam in);

    LoginLogDb convert(LoginLogParam in);

}
