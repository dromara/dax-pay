package cn.bootx.platform.daxpay.core.merchant.dao;

import cn.bootx.platform.daxpay.core.merchant.entity.MchAppPayConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商户应用支付配置
 *
 * @author xxm
 * @since 2023-05-19
 */
@Mapper
public interface MchAppPayConfigMapper extends BaseMapper<MchAppPayConfig> {

}
