package org.dromara.daxpay.payment.device.dao.qrcode.template;

import org.dromara.daxpay.payment.device.entity.qrcode.template.CashierCodeTemplate;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收银台码牌配置
 * @author xxm
 * @since 2025/7/1
 */
@Mapper
public interface CashierCodeTemplateMapper extends MPJBaseMapper<CashierCodeTemplate> {
}
