package cn.daxpay.single.sdk.param.trade.transfer;

import cn.daxpay.single.sdk.code.ChannelEnum;
import cn.daxpay.single.sdk.code.TransferPayeeTypeEnum;
import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.daxpay.single.sdk.model.trade.transfer.TransferModel;
import cn.daxpay.single.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 转账参数
 * @author xxm
 * @since 2024/6/19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TransferParam extends DaxPayRequest<TransferModel> {

    /** 商户转账号 */
    private String bizTransferNo;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    private String channel;

    /** 转账金额 */
    private BigDecimal amount;

    /** 标题 */
    private String title;

    /** 转账原因/备注 */
    private String reason;

    /**
     * 收款人账号类型
     * @see TransferPayeeTypeEnum
     */
    private String payeeType;

    /** 收款人账号 */
    private String payeeAccount;

    /** 收款人姓名 */
    private String payeeName;

    /** 回调通知地址 */
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回 */
    private String attach;

    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/transfer";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxPayResult<TransferModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<TransferModel>>() {});
    }
}
