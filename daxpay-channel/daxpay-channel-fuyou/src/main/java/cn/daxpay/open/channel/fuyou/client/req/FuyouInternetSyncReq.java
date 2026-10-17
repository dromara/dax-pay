package cn.daxpay.open.channel.fuyou.client.req;

import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

/// # 富友互联网直连订单同步请求(主应用侧)
@Data
@Schema(title = "富友互联网直连订单同步请求")
public class FuyouInternetSyncReq {

    /// 通道调用凭证
    @NotNull(message = "{validation.field.credential.notNull}")
    private FuyouRsaSdkCredential credential;

    /// 商户订单号(富友 order_id, 即下单时的平台支付交易号)
    @NotBlank(message = "{validation.field.relationOrderNo.notBlank}")
    private String relationOrderNo;

    /// 原下单日期(富友查询接口要求 order_date, yyyyMMdd, 东八区)
    private OffsetDateTime originOrderTime;
}
