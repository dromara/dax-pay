package cn.daxpay.open.payment.masterdata.dao.provider;

import cn.daxpay.open.payment.masterdata.entity.provider.PayProviderMethod;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 渠道支付方式目录项
@Mapper
public interface PayProviderMethodMapper extends MPJBaseMapper<PayProviderMethod> {
}