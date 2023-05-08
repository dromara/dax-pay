package cn.bootx.daxpay.core.paymodel.wallet.dao;

import cn.bootx.daxpay.core.paymodel.wallet.entity.Wallet;
import cn.bootx.iam.core.user.entity.UserInfo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 钱包
 *
 * @author xxm
 * @date 2021/2/24
 */
@Mapper
public interface WalletMapper extends BaseMapper<Wallet> {

    /**
     * 增加余额
     * @param walletId 钱包ID
     * @param amount 增加的额度
     * @param operator 操作人
     * @param date 时间
     * @return 更新数量
     */
    int increaseBalance(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount,
            @Param("operator") Long operator, @Param("date") LocalDateTime date);

    /**
     * 减少余额
     * @param walletId 钱包ID
     * @param amount 减少的额度
     * @param operator 操作人
     * @param date 操作时间
     * @return 操作条数
     */
    int reduceBalance(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount,
            @Param("operator") Long operator, @Param("date") LocalDateTime date);

    /**
     * 减少余额,允许扣成负数
     * @param walletId 钱包ID
     * @param amount 减少的额度
     * @param operator 操作人
     * @param date 操作时间
     * @return 操作条数
     */
    int reduceBalanceUnlimited(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount,
            @Param("operator") Long operator, @Param("date") LocalDateTime date);

    /**
     * 待开通钱包的用户列表
     */
    Page<UserInfo> pageByNotWallet(Page<UserInfo> mpPage, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

}
