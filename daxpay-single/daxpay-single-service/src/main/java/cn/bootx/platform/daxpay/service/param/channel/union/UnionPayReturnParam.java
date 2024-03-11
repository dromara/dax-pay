package cn.bootx.platform.daxpay.service.param.channel.union;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 云闪付同步回调参数
 * @author xxm
 * @since 2024/2/11
 */
@Data
@Accessors(chain = true)
@Schema(title = "云闪付同步回调参数")
public class UnionPayReturnParam {
    private String orderNo;
    private String signature;
    private String merName;
    private String settleDate;
    private String certId;
    private String voucherNum;
    private String version;
    private String settleKey;
    private String termId;
    private String origReqType;
    private String qrNo;
    private String reqReserved;
    private String reqType;
    private String origRespMsg;
    private String comInfo;
    private String merId;
    private String merCatCode;
    private String currencyCode;
    private String origRespCode;
    private String txnAmt;
}
