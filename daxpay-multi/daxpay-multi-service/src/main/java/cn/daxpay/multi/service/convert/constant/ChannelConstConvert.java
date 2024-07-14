package cn.daxpay.multi.service.convert.constant;

import cn.daxpay.multi.service.entity.constant.ChannelConst;
import cn.daxpay.multi.service.result.constant.ChannelConstResult;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/7/14
 */
public interface ChannelConstConvert {
    ChannelConstConvert CONVERT = Mappers.getMapper(ChannelConstConvert.class);

    ChannelConstResult toResult(ChannelConst source);
}
