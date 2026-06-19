package cn.daxpay.open.payment.masterdata.constants.provider.dao;

import cn.daxpay.open.payment.masterdata.constants.provider.entity.PayProviderMethod;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 渠道支付方式目录项
@Mapper
public interface PayProviderMethodMapper extends MPJBaseMapper<PayProviderMethod> {
}