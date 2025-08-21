package org.dromara.daxpay.channel.alipay.entity.allocation;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.core.util.JsonUtil;
import cn.hutool.json.JSONUtil;
import org.dromara.daxpay.channel.alipay.convert.AlipayAllocReceiverConvert;
import org.dromara.daxpay.channel.alipay.result.allocation.AlipayAllocReceiverResult;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付宝分账接收方
 * @author xxm
 * @since 2025/1/25
 */
@Data
@Accessors(chain = true)
public class AlipayAllocReceiver implements ToResult<AlipayAllocReceiverResult> {

    /** 主键 */
    private Long id;

    /** 分账接收方编号, 需要保证唯一 */
    private String receiverNo;

    /** 接收方名称 */
    private String receiverName;

    /** 接收方账号 */
    private String receiverAccount;

    /**
     * 接收方类型
     * @see AllocReceiverTypeEnum
     */
    private String receiverType;

    /** 分账关系类型 */
    private String relationType;

    /** 分账关系名称 */
    private String relationName;

    /** 商户AppId */
    private String appId;


    /**
     * 转换为通用接收方
     */
    public AllocReceiver toReceiver(boolean isv) {
        var receiver = new AllocReceiver();
        receiver.setId(this.getId());
        receiver.setAppId(this.getAppId());
        receiver.setReceiverType(this.getReceiverType());
        receiver.setReceiverNo(this.getReceiverNo());
        receiver.setReceiverName(this.getReceiverName());
        receiver.setReceiverAccount(this.getReceiverAccount());
        receiver.setChannel(isv?ChannelEnum.ALIPAY_ISV.getCode():ChannelEnum.ALIPAY.getCode());
        var copy = AlipayAllocReceiverConvert.CONVERT.copy(this);
        // 清空不需要序列化的字段
        copy.setId(null).setReceiverNo(null).setReceiverName(null).setReceiverType(null).setReceiverAccount(null);
        String jsonStr = JsonUtil.toJsonStr(copy);
        receiver.setExt(jsonStr);
        return receiver;
    }

    /**
     * 转换为通道接收方
     */
    public static AlipayAllocReceiver convertChannel(AllocReceiver receiver) {
        var leshuaAllocReceiver = JSONUtil.toBean(receiver.getExt(), AlipayAllocReceiver.class);
        leshuaAllocReceiver.setId(receiver.getId())
                .setReceiverNo(receiver.getReceiverNo())
                .setReceiverName(receiver.getReceiverName())
                .setReceiverType(receiver.getReceiverType())
                .setReceiverAccount(receiver.getReceiverAccount());
        return leshuaAllocReceiver;
    }

    @Override
    public AlipayAllocReceiverResult toResult() {
        return AlipayAllocReceiverConvert.CONVERT.toResult(this);
    }
}
