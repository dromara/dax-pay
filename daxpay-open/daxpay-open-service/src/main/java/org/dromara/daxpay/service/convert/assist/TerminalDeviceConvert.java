package org.dromara.daxpay.service.convert.assist;

import org.dromara.daxpay.service.entity.assist.TerminalDevice;
import org.dromara.daxpay.service.param.termina.TerminalDeviceParam;
import org.dromara.daxpay.service.result.termina.TerminalDeviceResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付终端设备转换
 * @author xxm
 * @since 2025/3/8
 */
@Mapper
public interface TerminalDeviceConvert {
    TerminalDeviceConvert CONVERT = Mappers.getMapper(TerminalDeviceConvert.class);

    TerminalDeviceResult toResult(TerminalDevice entity);

    TerminalDevice toEntity(TerminalDeviceParam param);

}
