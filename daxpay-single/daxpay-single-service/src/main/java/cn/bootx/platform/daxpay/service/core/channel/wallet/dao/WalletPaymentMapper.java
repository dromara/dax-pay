package cn.bootx.platform.daxpay.service.core.channel.wallet.dao;

import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.WalletPayOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@Deprecated
public interface WalletPaymentMapper extends BaseMapper<WalletPayOrder> {

}
