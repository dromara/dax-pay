package cn.bootx.platform.daxpay.service.core.channel.wallet.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.service.core.channel.wallet.dao.WalletRecordManager;
import cn.bootx.platform.daxpay.service.core.channel.wallet.entity.WalletRecord;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletRecordDto;
import cn.bootx.platform.daxpay.service.param.channel.wallet.WalletRecordQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 钱包记录服务类
 * @author xxm
 * @since 2024/2/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletRecordService {
    private final WalletRecordManager walletRecordManager;

    /**
     * 分页
     */
    public PageResult<WalletRecordDto> page(PageParam pageParam, WalletRecordQuery param) {
        return MpUtil.convert2DtoPageResult(walletRecordManager.page(pageParam, param));
    }

    /**
     * 查询详情
     */
    public WalletRecordDto findById(Long id){
        return walletRecordManager.findById(id).map(WalletRecord::toDto).orElseThrow(DataNotExistException::new);
    }

}
