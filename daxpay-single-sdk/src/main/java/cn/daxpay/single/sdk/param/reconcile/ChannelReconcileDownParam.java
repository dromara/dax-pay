package cn.daxpay.single.sdk.param.reconcile;

import cn.daxpay.single.sdk.net.DaxPayRequest;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.daxpay.single.sdk.model.reconcile.ReconcileDownModel;
import cn.daxpay.single.sdk.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 通道对账文件下载参数
 * @author xxm
 * @since 2024/8/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "对账下载参数")
public class ChannelReconcileDownParam extends DaxPayRequest<ReconcileDownModel> {
    @Schema(description = "通道")
    private String channel;

    @Schema(description = "日期")
    private LocalDate date;

    /**
     * 方法请求路径
     *
     * @return 请求路径
     */
    @Override
    public String path() {
        return "/unipay/reconcile/channelDownUrl";
    }

    /**
     * 将请求返回结果反序列化为实体类
     *
     * @param json json字符串
     * @return 反序列后的对象
     */
    @Override
    public DaxPayResult<ReconcileDownModel> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxPayResult<ReconcileDownModel>>() {});
    }
}
