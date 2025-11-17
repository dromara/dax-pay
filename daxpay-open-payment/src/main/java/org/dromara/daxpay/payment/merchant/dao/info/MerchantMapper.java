package org.dromara.daxpay.payment.merchant.dao.info;

import org.dromara.daxpay.payment.merchant.entity.info.Merchant;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商户信息
 * @author xxm
 * @since 2024/5/27
 */
@Mapper
public interface MerchantMapper extends MPJBaseMapper<Merchant> {
}
