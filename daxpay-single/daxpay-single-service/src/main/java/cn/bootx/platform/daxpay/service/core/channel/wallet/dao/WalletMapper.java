package cn.bootx.platform.daxpay.service.core.channel.wallet.dao;

import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 钱包
 *
 * @author xxm
 * @since 2021/2/24
 */
@Mapper
public interface WalletMapper extends BaseMapper<Wallet> {
}
