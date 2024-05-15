package cn.daxpay.single.service.core.channel.alipay.dao;

import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付宝配置
 * @author xxm
 * @since 2023/12/18
 */
@Mapper
public interface AliPayConfigMapper extends BaseMapper<AliPayConfig> {

}
