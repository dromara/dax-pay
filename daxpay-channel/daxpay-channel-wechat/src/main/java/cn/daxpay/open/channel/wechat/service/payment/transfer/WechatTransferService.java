package cn.daxpay.open.channel.wechat.service.payment.transfer;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatTransferReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatTransferResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.transfer.bo.TransferResultBo;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/// # 微信转账执行业务服务
///
/// 通过 [WechatChannelClient] 调用子应用 dax-pay-channel-one 完成微信商家转账到零钱(V3)。
/// 请求构建、响应解析与状态映射全部在本类中完成。
/// 金额单位: 分; 状态映射见 [mapSyncResult]。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatTransferService {

    /// 转账成功状态
    private static final String STATE_SUCCESS = "SUCCESS";
    /// 转账失败/撤销终态
    private static final List<String> STATE_FAIL_CLOSE = List.of("FAIL", "CANCELLED");
    /// 转账处理中状态
    private static final List<String> STATE_PROCESSING =
            List.of("ACCEPTED", "PROCESSING", "WAIT_USER_CONFIRM", "TRANSFERING", "CANCELING");

    private final WechatChannelClient wechatChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行微信转账
    ///
    /// @param context    转账策略上下文(通道特有字段: payeeOpenid/transferScene/userName)
    /// @param credential 通道调用凭证(密钥/证书)
    /// @return 转账结果
    public TransferResultBo transfer(TransferStrategyContext context, WechatSdkCredential credential) {
        WechatTransferReq req = new WechatTransferReq();
        req.setOutBillNo(context.getTransferNo());
        req.setAmount(context.getAmount());
        req.setOpenid(context.getPayeeOpenid());
        req.setScene(context.getTransferScene());
        req.setUserName(context.getUserName());
        req.setRemark(StrUtil.sub(context.getTitle(), 0, 32));
        req.setNotifyUrl(this.buildNotifyUrl(context));
        req.setCredential(credential);

        DaxResult<WechatTransferResp> result = wechatChannelClient.transfer(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.wechat.transferFailed", result.getMsg());
        }
        WechatTransferResp resp = result.getData();
        return new TransferResultBo()
                .setStatus(PayFundStatusEnum.PROCESSING)
                .setOutTransferNo(resp.getTransferBillNo())
                .setTransferBody(resp.getPackageInfo());
    }

    /// 同步查询微信转账状态
    ///
    /// @param context    转账策略上下文
    /// @param credential 通道调用凭证
    /// @return 转账同步结果
    public TransferResultBo sync(TransferStrategyContext context, WechatSdkCredential credential) {
        WechatTransferReq req = new WechatTransferReq();
        req.setOutBillNo(context.getOutTransferNo());
        req.setTransferNo(context.getTransferNo());
        req.setCredential(credential);

        DaxResult<WechatTransferResp> result = wechatChannelClient.transferSync(req);
        if (result.getCode() != 0) {
            return new TransferResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg())
                    .setStatus(PayFundStatusEnum.PROCESSING);
        }
        return mapSyncResult(result.getData());
    }

    /// 映射同步结果为平台资金态
    private TransferResultBo mapSyncResult(WechatTransferResp resp) {
        String state = resp.getState();
        TransferResultBo bo = new TransferResultBo()
                .setOutTransferNo(resp.getTransferBillNo())
                .setFinishTime(resp.getFinishTime());
        if (Objects.equals(state, STATE_SUCCESS)) {
            bo.setStatus(PayFundStatusEnum.SUCCESS);
        } else if (STATE_FAIL_CLOSE.contains(state)) {
            bo.setStatus(PayFundStatusEnum.CLOSE)
                    .setSyncErrorMsg(resp.getFailReason());
        } else if (STATE_PROCESSING.contains(state)) {
            bo.setStatus(PayFundStatusEnum.PROCESSING);
        } else {
            // 未知状态保持处理中, 由后续同步轮询确认
            log.warn("微信转账同步未知状态: state={}, transferNo={}", state, resp.getTransferBillNo());
            bo.setStatus(PayFundStatusEnum.PROCESSING);
        }
        return bo;
    }

    /// 生成微信转账异步通知地址(微信→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/wechat/transfer`
    private String buildNotifyUrl(TransferStrategyContext context) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // backendBaseUrl 未配置时抛清晰异常
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/wechat/transfer",
                base, context.getMchNo(), context.getChannelMchNo());
    }
}
