package org.dromara.daxpay.service.convert.constant;

import org.dromara.daxpay.service.entity.constant.TerminalConst;
import org.dromara.daxpay.service.result.constant.TerminalConstResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2025/3/12
 */
@Mapper
public interface TerminalConstConvert {
    TerminalConstConvert CONVERT = Mappers.getMapper(TerminalConstConvert.class);

    TerminalConstResult toResult(TerminalConst channelTerminalTypeConst);
}
