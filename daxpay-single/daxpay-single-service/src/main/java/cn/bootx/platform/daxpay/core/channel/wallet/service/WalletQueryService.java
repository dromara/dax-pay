package cn.bootx.platform.daxpay.core.channel.wallet.service;

import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletManager;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletDto;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletInfoDto;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletPayParam;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletQueryParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.iam.core.user.service.UserQueryService;
import cn.bootx.platform.iam.dto.user.UserInfoDto;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
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

    private final UserQueryService userQueryService;

    /**
     * 根据钱包ID查询Wallet
     */
    public WalletDto findById(Long walletId) {
        return walletManager.findById(walletId).map(Wallet::toDto).orElseThrow(DataNotExistException::new);
    }


    /**
     * 根据用户ID查询钱包
     */
    public WalletDto findByUser(String mchAppCode) {
        Long userId = SecurityUtil.getUserId();
        return walletManager.findByUser(userId,mchAppCode).map(Wallet::toDto).orElse(null);
    }

    /**
     * 获取钱包综合信息
     */
    public WalletInfoDto getWalletInfo(Long walletId) {
        Wallet wallet = walletManager.findById(walletId).orElseThrow(DataNotExistException::new);
        UserInfoDto userInfoDto = userQueryService.findById(wallet.getUserId());
        WalletInfoDto walletInfoDto = new WalletInfoDto();
        BeanUtil.copyProperties(wallet, walletInfoDto);
        walletInfoDto.setUserName(userInfoDto.getName());
        return walletInfoDto;
    }

    /**
     * 查询用户 分页
     */
    public PageResult<WalletDto> page(PageParam pageParam, WalletQueryParam param) {
        return MpUtil.convert2DtoPageResult(walletManager.page(pageParam, param));
    }

    /**
     * 待开通钱包的用户列表
     */
    public PageResult<UserInfoDto> pageByNotWallet(PageParam pageParam,String mchCode, UserInfoParam userInfoParam) {
        return MpUtil.convert2DtoPageResult(walletManager.pageByNotWallet(pageParam,mchCode ,userInfoParam));
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
        // 根据用户id查询
        if (Objects.isNull(walletPayParam.getUserId())){
            userId = SecurityUtil.getCurrentUser().map(UserDetail::getId).orElse(null);
        }
        return walletManager.findByUser(userId,payParam.getMchAppCode()).orElse(null);
    }

}
