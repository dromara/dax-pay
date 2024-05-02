package cn.bootx.platform.daxpay.service.core.channel.wallet.convert;

import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.WalletConfig;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.WalletRecord;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletConfigDto;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletDto;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.wechat.WalletConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 转换
 *
 * @author xxm
 * @since 2021/8/20
 */
@Mapper
public interface WalletConvert {

    WalletConvert CONVERT = Mappers.getMapper(WalletConvert.class);

    WalletDto convert(Wallet in);

    WalletConfigDto convert(WalletConfig in);

    WalletConfig convert(WalletConfigParam in);

    WalletRecordDto convert(WalletRecord in);

}
