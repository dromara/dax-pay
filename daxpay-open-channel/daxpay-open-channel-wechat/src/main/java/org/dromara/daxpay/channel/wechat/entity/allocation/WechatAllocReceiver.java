package org.dromara.daxpay.channel.wechat.entity.allocation;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.core.util.JsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.channel.wechat.convert.WechatAllocReceiverConvert;
import org.dromara.daxpay.channel.wechat.result.allocation.WechatAllocReceiverResult;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocReceiver;

/**
 * 微信分账接收方
 * @author xxm
 * @since 2025/1/25
 */
@Data
@Accessors(chain = true)
public class WechatAllocReceiver implements ToResult<WechatAllocReceiverResult> {

    /** 主键 */
    private Long id;

    /** 分账接收方编号, 需要保证唯一 */
    private String receiverNo;

    /** 接收方名称 */
    private String receiverName;

    /** 接收方账号 */
    private String receiverAccount;

    /** 接收方类型 */
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
        receiver.setReceiverNo(this.getReceiverNo());
        receiver.setReceiverType(this.getReceiverType());
        receiver.setReceiverName(this.getReceiverName());
        receiver.setReceiverAccount(this.getReceiverAccount());
        receiver.setChannel(isv ? ChannelEnum.WECHAT_ISV.getCode() : ChannelEnum.WECHAT.getCode());
        var copy = WechatAllocReceiverConvert.CONVERT.copy(this);
        // 清空不需要序列化的字段
        copy.setId(null).setReceiverNo(null).setReceiverName(null).setReceiverAccount(null);
        String jsonStr = JsonUtil.toJsonStr(copy);
        receiver.setExt(jsonStr);
        return receiver;
    }

    /**
     * 转换为通道接收方
     */
    public static WechatAllocReceiver convertChannel(AllocReceiver receiver) {
        var leshuaAllocReceiver = JsonUtil.toBean(receiver.getExt(), WechatAllocReceiver.class);
        leshuaAllocReceiver.setId(receiver.getId())
                .setReceiverNo(receiver.getReceiverNo())
                .setReceiverName(receiver.getReceiverName())
                .setReceiverType(receiver.getReceiverType())
                .setReceiverAccount(receiver.getReceiverAccount());
        return leshuaAllocReceiver;
    }

    @Override
    public WechatAllocReceiverResult toResult() {
        return WechatAllocReceiverConvert.CONVERT.toResult(this);
    }

}
