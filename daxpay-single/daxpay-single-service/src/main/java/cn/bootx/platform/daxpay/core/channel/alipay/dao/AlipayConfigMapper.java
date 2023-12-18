package cn.bootx.platform.daxpay.core.channel.alipay.dao;

import cn.bootx.platform.daxpay.core.channel.alipay.entity.AlipayConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付宝配置
 * @author xxm
 * @since 2023/12/18
 */
@Mapper
public interface AlipayConfigMapper extends BaseMapper<AlipayConfig> {

}
