package cn.daxpay.multi.service.convert.merchant;

import cn.daxpay.multi.service.entity.merchant.MchApp;
import cn.daxpay.multi.service.param.merchant.MchAppParam;
import cn.daxpay.multi.service.result.merchant.MchAppResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/6/24
 */
@Mapper
public interface MchAppConvert {
    MchAppConvert CONVERT = Mappers.getMapper(MchAppConvert.class);

    MchAppResult toResult(MchApp entity);

    MchApp toEntity(MchAppParam param);
}
