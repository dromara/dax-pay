package cn.bootx.platform.daxpay.core.channel.wallet.convert;

import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletConfig;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletLog;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.WalletPayment;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletConfigDto;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletDto;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletLogDto;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletPaymentDto;
import cn.bootx.platform.daxpay.param.channel.wechat.WalletConfigParam;
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

    WalletPaymentDto convert(WalletPayment in);

    WalletLogDto convert(WalletLog in);

    WalletConfigDto convert(WalletConfig in);

    WalletConfig convert(WalletConfigParam in);

}
