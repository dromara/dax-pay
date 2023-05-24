package cn.bootx.platform.daxpay.core.channel.wechat.convert;

import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayment;
import cn.bootx.platform.daxpay.dto.channel.wechat.WeChatPayConfigDto;
import cn.bootx.platform.daxpay.dto.channel.wechat.WeChatPaymentDto;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 微信转换类
 *
 * @author xxm
 * @date 2021/6/21
 */
@Mapper
public interface WeChatConvert {

    WeChatConvert CONVERT = Mappers.getMapper(WeChatConvert.class);

    WeChatPayConfig convert(WeChatPayConfigParam in);

    WeChatPayConfigDto convert(WeChatPayConfig in);

    WeChatPaymentDto convert(WeChatPayment in);

    WeChatPayment convert(WeChatPaymentDto in);

}
