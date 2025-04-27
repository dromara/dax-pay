package org.dromara.daxpay.channel.wechat.entity.allocation;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.channel.wechat.convert.WechatAllocReceiverBindConvert;
import org.dromara.daxpay.channel.wechat.param.allocation.WechatAllocReceiverBindParam;
import org.dromara.daxpay.channel.wechat.result.allocation.WechatAllocReceiverBindResult;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;

/**
 * 微信分账接收方绑定
 * @author xxm
 * @since 2025/1/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_wechat_alloc_receiver_bind")
public class WechatAllocReceiverBind extends MchAppBaseEntity implements ToResult<WechatAllocReceiverBindResult> {

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

    /** 是否服务商模式 */
    private boolean isv;

    /** 是否绑定 */
    private boolean bind;

    /** 错误提示 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    public static WechatAllocReceiverBind init(WechatAllocReceiverBindParam param) {
        return WechatAllocReceiverBindConvert.CONVERT.toEntity(param);
    }

    @Override
    public WechatAllocReceiverBindResult toResult() {
        return WechatAllocReceiverBindConvert.CONVERT.toResult(this);
    }
}
