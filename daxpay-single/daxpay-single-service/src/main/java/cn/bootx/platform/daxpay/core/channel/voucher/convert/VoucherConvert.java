package cn.bootx.platform.daxpay.core.channel.voucher.convert;

import cn.bootx.platform.daxpay.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherLog;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherPayOrder;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherDto;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherLogDto;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherPayOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/3/14
 */
@Mapper
public interface VoucherConvert {

    VoucherConvert CONVERT = Mappers.getMapper(VoucherConvert.class);

    VoucherDto convert(Voucher in);

    VoucherLogDto convert(VoucherLog in);

    VoucherPayOrderDto convert(VoucherPayOrder in);

}
