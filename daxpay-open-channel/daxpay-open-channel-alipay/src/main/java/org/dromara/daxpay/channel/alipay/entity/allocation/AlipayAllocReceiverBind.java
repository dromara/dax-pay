package org.dromara.daxpay.channel.alipay.entity.allocation;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.channel.alipay.convert.AlipayAllocReceiverBindConvert;
import org.dromara.daxpay.channel.alipay.param.allocation.AlipayAllocReceiverBindParam;
import org.dromara.daxpay.channel.alipay.result.allocation.AlipayAllocReceiverBindResult;
import org.dromara.daxpay.core.enums.AllocReceiverTypeEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付宝分账接收方绑定
 * @author xxm
 * @since 2025/1/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alipay_alloc_receiver_bind")
public class AlipayAllocReceiverBind extends MchAppBaseEntity implements ToResult<AlipayAllocReceiverBindResult> {

    /**
     * 接收方类型
     * @see AllocReceiverTypeEnum
     */
    private String receiverType;

    /** 接收方账号 */
    private String receiverAccount;

    /** 接收方名称 */
    private String receiverName;

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

    /**
     * 初始化
     */
    public static AlipayAllocReceiverBind init(AlipayAllocReceiverBindParam param){
        return AlipayAllocReceiverBindConvert.CONVERT.toEntity(param);
    }


    @Override
    public AlipayAllocReceiverBindResult toResult() {
        return AlipayAllocReceiverBindConvert.CONVERT.toResult(this);
    }
}
