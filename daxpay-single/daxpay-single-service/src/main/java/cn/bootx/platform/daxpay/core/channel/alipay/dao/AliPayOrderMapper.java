package cn.bootx.platform.daxpay.core.channel.alipay.dao;

import cn.bootx.platform.daxpay.core.channel.alipay.entity.AliPayOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付宝支付
 *
 * @author xxm
 * @since 2021/2/26
 */
@Mapper
public interface AliPayOrderMapper extends BaseMapper<AliPayOrder> {

}
