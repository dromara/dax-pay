package cn.bootx.platform.daxpay.service.core.channel.voucher.convert;

import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.VoucherConfig;
import cn.bootx.platform.daxpay.service.core.channel.voucher.entity.VoucherRecord;
import cn.bootx.platform.daxpay.service.dto.channel.voucher.VoucherConfigDto;
import cn.bootx.platform.daxpay.service.dto.channel.voucher.VoucherDto;
import cn.bootx.platform.daxpay.service.dto.channel.voucher.VoucherRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.voucher.VoucherBatchImportParam;
import cn.bootx.platform.daxpay.service.param.channel.voucher.VoucherImportParam;
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

    VoucherRecordDto convert(VoucherRecord in);

    VoucherConfigDto convert(VoucherConfig in);

    Voucher convert(VoucherImportParam in);

    Voucher convert(VoucherBatchImportParam in);

}
