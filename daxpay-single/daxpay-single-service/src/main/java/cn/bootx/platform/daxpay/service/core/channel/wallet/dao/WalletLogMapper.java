package cn.bootx.platform.daxpay.service.core.channel.wallet.dao;

import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.WalletLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 钱包日志
 *
 * @author xxm
 * @since 2020/12/8
 */
@Mapper
public interface WalletLogMapper extends BaseMapper<WalletLog> {

}
