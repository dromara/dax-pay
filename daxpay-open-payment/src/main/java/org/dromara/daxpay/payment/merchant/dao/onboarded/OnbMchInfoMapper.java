package org.dromara.daxpay.payment.merchant.dao.onboarded;

import org.dromara.daxpay.payment.merchant.entity.onboarded.OnbMchInfo;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 进件商户信息
 * @author xxm
 * @since 2025/11/11
 */
@Mapper
public interface OnbMchInfoMapper extends MPJBaseMapper<OnbMchInfo> {
}