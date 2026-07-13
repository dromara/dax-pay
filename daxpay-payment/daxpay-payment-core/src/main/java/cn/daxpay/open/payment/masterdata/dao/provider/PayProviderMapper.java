package cn.daxpay.open.payment.masterdata.dao.provider;

import cn.daxpay.open.payment.masterdata.entity.provider.PayProvider;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付渠道
@Mapper
public interface PayProviderMapper extends MPJBaseMapper<PayProvider> {
}