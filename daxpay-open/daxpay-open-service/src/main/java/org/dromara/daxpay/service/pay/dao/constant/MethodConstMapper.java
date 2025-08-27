package org.dromara.daxpay.service.pay.dao.constant;

import org.dromara.daxpay.service.pay.entity.constant.PayMethodConst;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付方式
 * @author xxm
 * @since 2024/6/26
 */
@Mapper
public interface MethodConstMapper extends MPJBaseMapper<PayMethodConst> {
}
