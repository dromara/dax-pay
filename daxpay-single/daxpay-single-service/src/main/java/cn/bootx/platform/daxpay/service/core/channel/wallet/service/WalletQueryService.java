package cn.bootx.platform.daxpay.service.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.channel.wallet.dao.WalletManager;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletDto;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletInfoDto;
import cn.bootx.platform.daxpay.service.param.channel.wallet.WalletPayParam;
import cn.bootx.platform.daxpay.service.param.channel.wallet.WalletQueryParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.hutool.core.bean.BeanUtil;
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
     * 根据钱包ID查询Wallet
     */
    public WalletDto findById(Long walletId) {
        return walletManager.findById(walletId).map(Wallet::toDto).orElseThrow(DataNotExistException::new);
    }



    /**
     * 获取钱包综合信息
     */
    public WalletInfoDto getWalletInfo(Long walletId) {
        Wallet wallet = walletManager.findById(walletId).orElseThrow(DataNotExistException::new);
        WalletInfoDto walletInfoDto = new WalletInfoDto();
        BeanUtil.copyProperties(wallet, walletInfoDto);
        return walletInfoDto;
    }

    /**
     * 查询用户 分页
     */
    public PageResult<WalletDto> page(PageParam pageParam, WalletQueryParam param) {
        return MpUtil.convert2DtoPageResult(walletManager.page(pageParam, param));
    }


    /**
     * 获取钱包, 获取顺序: 1. 显式传入的钱包ID 2. 显式传入的用户ID 3. 从系统中获取到的用户ID
     *
     */
    public Wallet getWallet(WalletPayParam walletPayParam, PayParam payParam){

        Wallet wallet = null;
        Long userId = null;
        // 首先根据钱包ID查询
        if (Objects.nonNull(walletPayParam.getWalletId())) {
            wallet = walletManager.findById(walletPayParam.getWalletId()).orElseThrow(null);
        }
        if (Objects.nonNull(wallet)){
            return wallet;
        }
        return null;
    }

}
