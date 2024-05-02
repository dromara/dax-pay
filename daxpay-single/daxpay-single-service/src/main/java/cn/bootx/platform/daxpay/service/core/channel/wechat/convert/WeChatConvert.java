package cn.bootx.platform.daxpay.service.core.channel.wechat.convert;

import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.payment.reconcile.domain.GeneralReconcileRecord;
import cn.bootx.platform.daxpay.service.dto.channel.wechat.WeChatPayConfigDto;
import cn.bootx.platform.daxpay.service.dto.channel.wechat.WeChatPayRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.wechat.WeChatPayConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 微信转换类
 *
 * @author xxm
 * @since 2021/6/21
 */
@Mapper
public interface WeChatConvert {

    WeChatConvert CONVERT = Mappers.getMapper(WeChatConvert.class);

    WeChatPayConfig convert(WeChatPayConfigParam in);

    WeChatPayRecordDto convert(WeChatPayRecord in);

    GeneralReconcileRecord convertReconcileRecord(WeChatPayRecord in);

    WeChatPayConfigDto convert(WeChatPayConfig in);

}
