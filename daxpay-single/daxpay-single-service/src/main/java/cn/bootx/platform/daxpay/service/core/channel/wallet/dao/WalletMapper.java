package cn.bootx.platform.daxpay.service.core.channel.wallet.dao;

import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 钱包
 *
 * @author xxm
 * @since 2021/2/24
 */
@Mapper
public interface WalletMapper extends BaseMapper<Wallet> {

    /**
     * 预冻结余额
     * @param walletId 钱包ID
     * @param amount 冻结的额度
     * @param operator 操作人
     * @param date 时间
     * @return 更新数量
     */
    int freezeBalance(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount,
                      @Param("operator") Long operator, @Param("date") LocalDateTime date);

    /**
     * 解冻金额
     * @param walletId 钱包ID
     * @param amount 冻结的额度
     * @param operator 操作人
     * @param date 时间
     * @return 更新数量
     */
    int unfreezeBalance(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount,
                      @Param("operator") Long operator, @Param("date") LocalDateTime date);

    /**
     * 释放预占额度
     * @param walletId 钱包ID
     * @param amount 要释放的额度
     * @param operator 操作人
     * @param date 操作时间
     * @return 操作条数
     */
    int reduceBalance(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount,
                      @Param("operator") Long operator, @Param("date") LocalDateTime date);

    /**
     * 扣减余额同时解除预冻结的额度
     * @param walletId 钱包ID
     * @param amount 减少的额度
     * @param operator 操作人
     * @param date 操作时间
     * @return 操作条数
     */
    int reduceAndUnfreezeBalance(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount,
                      @Param("operator") Long operator, @Param("date") LocalDateTime date);


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
     * 扣减余额,允许扣成负数
     * @param walletId 钱包ID
     * @param amount 更改的额度, 正数增加,负数减少
     * @param operator 操作人
     * @param date 操作时间
     * @return 操作条数
     */
    int reduceBalanceUnlimited(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount,
                               @Param("operator") Long operator, @Param("date") LocalDateTime date);

}
