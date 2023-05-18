package cn.bootx.platform.daxpay.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.channel.wallet.dao.WalletManager;
import cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet;
import cn.bootx.platform.daxpay.dto.paymodel.wallet.WalletDto;
import cn.bootx.platform.daxpay.dto.paymodel.wallet.WalletInfoDto;
import cn.bootx.platform.daxpay.param.paymodel.wallet.WalletPayParam;
import cn.bootx.platform.iam.core.user.service.UserQueryService;
import cn.bootx.platform.iam.dto.user.UserInfoDto;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 钱包
 *
 * @author xxm
 * @date 2022/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletQueryService {

    private final WalletManager walletManager;

    private final UserQueryService userQueryService;

    /**
     * 根据ID查询Wallet
     */
    public WalletDto findById(Long walletId) {
        return walletManager.findById(walletId).map(Wallet::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据用户ID查询钱包
     */
    public WalletDto findByUser() {
        Long userId = SecurityUtil.getUserId();
        return walletManager.findByUser(userId).map(Wallet::toDto).orElseThrow(DataNotExistException::new);
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
    public PageResult<WalletDto> page(PageParam pageParam, WalletPayParam param) {
        return MpUtil.convert2DtoPageResult(walletManager.page(pageParam, param));
    }

    /**
     * 待开通钱包的用户列表
     */
    public PageResult<UserInfoDto> pageByNotWallet(PageParam pageParam, UserInfoParam userInfoParam) {
        return MpUtil.convert2DtoPageResult(walletManager.pageByNotWallet(pageParam, userInfoParam));
    }

}
