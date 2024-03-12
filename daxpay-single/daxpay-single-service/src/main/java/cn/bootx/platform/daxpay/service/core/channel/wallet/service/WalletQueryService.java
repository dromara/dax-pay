package cn.bootx.platform.daxpay.service.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.exception.waller.WalletNotExistsException;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.service.core.channel.wallet.dao.WalletManager;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletDto;
import cn.bootx.platform.daxpay.service.param.channel.wallet.WalletQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 钱包
 *
 * @author xxm
 * @since 2022/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletQueryService {

    private final WalletManager walletManager;

    /**
     * 根据钱包ID查询钱包
     */
    public WalletDto findById(Long id) {
        return walletManager.findById(id).map(Wallet::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取钱包综合信息
     */
    public WalletDto findByUserId(String userId) {
        return walletManager.findByUser(userId).map(Wallet::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 判断用户是否开通了钱包
     */
    public boolean existsByUserId(String userId) {
        return walletManager.existsByUser(userId);
    }

    /**
     * 分页
     */
    public PageResult<WalletDto> page(PageParam pageParam, WalletQuery param) {
        return MpUtil.convert2DtoPageResult(walletManager.page(pageParam, param));
    }


    /**
     * 获取钱包, 获取顺序: 1. 钱包ID 2. 用户ID
     */
    public Wallet getWallet(WalletPayParam param){

        Wallet wallet = null;
        if (Objects.nonNull(param.getWalletId())){
            wallet =  walletManager.findById(param.getWalletId()).orElseThrow(WalletNotExistsException::new);
        }
        if (Objects.isNull(wallet)){
            wallet = walletManager.findByUser(param.getUserId()).orElseThrow(WalletNotExistsException::new);
        }
        return wallet;
    }

}
