package cn.daxpay.open.payment.merchant.convert.store;

import cn.daxpay.open.payment.merchant.entity.store.MchStoreInfo;
import cn.daxpay.open.payment.merchant.param.store.MchStoreInfoParam;
import cn.daxpay.open.payment.merchant.result.store.MchStoreInfoResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/// # 门店信息转换
///
@Mapper
public interface MchStoreInfoConvert {
    MchStoreInfoConvert CONVERT = Mappers.getMapper(MchStoreInfoConvert.class);

    MchStoreInfoResult toResult(MchStoreInfo entity);

    MchStoreInfo toEntity(MchStoreInfoParam param);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copy(MchStoreInfoParam param, @MappingTarget MchStoreInfo mchStore);
}
