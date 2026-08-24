package cn.daxpay.open.payment.unipay.trade.service;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.payment.trade.transfer.dao.TransferTradeManager;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.transfer.runtime.service.TransferAssistService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 转账单定位服务(对外)
///
/// 查询与同步共用的凭证定位逻辑: 按平台转账单号(transferNo)或 通道+商户转账号(channel+bizTransferNo)
/// 定位跨通道公共凭证, 并校验商户归属。
/// 商户转账号不能单独定位——幂等维度为 通道+商户转账号+商户号(与发起时 [TransferAssistService#findExist] 一致),
/// 同商户同商户转账号可在不同通道各存在一笔, 必须配通道才能唯一确定容器。
@Service
@RequiredArgsConstructor
public class TransferOrderLocateService {

    private final TransferTradeManager transferTradeManager;
    private final TransferAssistService transferAssistService;

    /// 定位转账凭证
    ///
    /// @param mchNo         商户号(归属校验)
    /// @param transferNo    平台转账单号(优先)
    /// @param channel       转账通道(与商户转账号配对)
    /// @param bizTransferNo 商户转账号(与通道配对)
    public TransferTrade locate(String mchNo, String transferNo, String channel, String bizTransferNo) {
        // 校验参数: 平台转账单号与 商户转账号+通道 至少传一组
        if (StrUtil.isBlank(transferNo) && (StrUtil.isBlank(bizTransferNo) || StrUtil.isBlank(channel))) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.transfer.locateRequired");
        }

        // 优先按平台转账单号查询
        if (StrUtil.isNotBlank(transferNo)) {
            TransferTrade trade = transferTradeManager.findByTradeNo(transferNo)
                    .orElseThrow(() -> new DataNotExistException("pay.error.transfer.notFound"));
            // 归属校验: transferNo 为全局唯一编号, 防跨商户查单
            if (!Objects.equals(trade.getMchNo(), mchNo)) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNotBelong");
            }
            return trade;
        }
        // 按商户转账号+通道定位容器(查询条件含商户号, 天然限定归属), 再取公共凭证
        Long containerId = transferAssistService.findExist(channel, bizTransferNo, mchNo)
                .orElseThrow(() -> new DataNotExistException("pay.error.transfer.notFound"));
        return transferTradeManager.findByContainerId(containerId, channel)
                .orElseThrow(() -> new DataNotExistException("pay.error.transfer.notFound"));
    }
}
