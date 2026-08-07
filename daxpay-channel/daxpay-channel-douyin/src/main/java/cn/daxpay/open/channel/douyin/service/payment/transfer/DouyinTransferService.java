package cn.daxpay.open.channel.douyin.service.payment.transfer;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.req.DouyinTransferReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinTransferResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.transfer.bo.TransferResultBo;
import cn.daxpay.open.payment.trade.transfer.enums.TransferPayeeTypeEnum;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 抖音转账执行业务服务
///
/// 通过 [DouyinChannelClient] 调用子应用 dax-pay-channel-one 完成抖音商家转账
/// (/v1/fund_trade/mch-transfer/transfer-bills)。状态映射见 [mapSyncResult]。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinTransferService {

    /// 转账成功状态
    private static final String STATE_SUCCESS = "SUCCESS";
    /// 转账失败终态
    private static final String STATE_FAIL = "FAIL";

    private final DouyinChannelClient douyinChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行抖音转账
    ///
    /// @param context    转账策略上下文(通道特有字段: payeeType/payeeAccount/payeeName/transferScene)
    /// @param credential 通道调用凭证
    /// @return 转账结果
    public TransferResultBo transfer(TransferStrategyContext context, DouyinSdkCredential credential) {
        DouyinTransferReq req = new DouyinTransferReq();
        req.setOutBillNo(context.getTransferNo());
        req.setAmount(context.getAmount());
        // 收款人: openid 或手机号(phone 模式, 子应用证书加密上送)
        if (TransferPayeeTypeEnum.PHONE.getCode().equals(context.getPayeeType())) {
            req.setPhoneNumber(context.getPayeeAccount());
        } else {
            req.setOpenid(context.getPayeeAccount());
        }
        req.setScene(context.getTransferScene());
        req.setUserName(context.getPayeeName());
        req.setRemark(StrUtil.sub(context.getTitle(), 0, 32));
        // 用户收款感知不传, 抖音按场景取默认(第一个)
        // 转账场景报备信息(按场景要求填写)
        req.setReportInfos(context.getReportInfos());
        req.setNotifyUrl(this.buildNotifyUrl(context));
        req.setCredential(credential);

        DaxResult<DouyinTransferResp> result = douyinChannelClient.transfer(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.douyin.transferFailed", result.getMsg());
        }
        DouyinTransferResp resp = result.getData();
        TransferResultBo bo = new TransferResultBo()
                .setOutTransferNo(resp.getTransferBillNo());
        // 同步返回 SUCCESS 直接成功, 其余(ACCEPTED/TRANSFERING)视为处理中
        if (Objects.equals(resp.getState(), STATE_SUCCESS)) {
            bo.setStatus(PayFundStatusEnum.SUCCESS);
        } else {
            bo.setStatus(PayFundStatusEnum.PROCESSING);
        }
        return bo;
    }

    /// 同步查询抖音转账状态
    ///
    /// @param context    转账策略上下文
    /// @param credential 通道调用凭证
    /// @return 转账同步结果
    public TransferResultBo sync(TransferStrategyContext context, DouyinSdkCredential credential) {
        DouyinTransferReq req = new DouyinTransferReq();
        req.setOutBillNo(context.getOutTransferNo());
        req.setTransferNo(context.getTransferNo());
        req.setCredential(credential);

        DaxResult<DouyinTransferResp> result = douyinChannelClient.transferSync(req);
        if (result.getCode() != 0) {
            return new TransferResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg())
                    .setStatus(PayFundStatusEnum.PROCESSING);
        }
        return mapSyncResult(result.getData());
    }

    /// 映射同步结果为平台资金态
    ///
    /// 抖音状态: SUCCESS(成功) / FAIL(失败) / ACCEPTED/TRANSFERING(处理中)
    private TransferResultBo mapSyncResult(DouyinTransferResp resp) {
        String state = resp.getState();
        TransferResultBo bo = new TransferResultBo()
                .setOutTransferNo(resp.getTransferBillNo())
                .setSyncErrorMsg(resp.getFailReason());
        if (Objects.equals(state, STATE_SUCCESS)) {
            bo.setStatus(PayFundStatusEnum.SUCCESS);
        } else if (Objects.equals(state, STATE_FAIL)) {
            bo.setStatus(PayFundStatusEnum.CLOSE);
        } else {
            // ACCEPTED/TRANSFERING/未知: 保持处理中, 由后续同步轮询确认
            bo.setStatus(PayFundStatusEnum.PROCESSING);
        }
        return bo;
    }

    /// 生成抖音转账异步通知地址(抖音→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/douyin/transfer`
    private String buildNotifyUrl(TransferStrategyContext context) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // backendBaseUrl 未配置时抛清晰异常
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/douyin/transfer",
                base, context.getMchNo(), context.getChannelMchNo());
    }
}
