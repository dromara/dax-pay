package cn.bootx.platform.daxpay.service.core.system.config.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.service.code.PayApiCallBackTypeEnum;
import cn.bootx.platform.daxpay.service.core.system.config.convert.PayApiConfigConvert;
import cn.bootx.platform.daxpay.service.dto.system.config.PayApiConfigDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付开放接口管理
 * @author xxm
 * @since 2023/12/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付接口配置")
@TableName("pay_api_config")
public class PayApiConfig extends MpBaseEntity implements EntityBaseFunction<PayApiConfigDto> {

    @DbColumn(comment = "编码")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;

    @DbColumn(comment = "接口地址")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String api;

    @DbColumn(comment = "名称")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String name;

    /**
     * 支持回调通知
     * @see PayApiCallBackTypeEnum
     */
    @DbColumn(comment = "支持回调通知")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private boolean noticeSupport;

    @DbColumn(comment = "是否启用")
    private boolean enable;

    @DbColumn(comment = "是否开启回调通知")
    private boolean notice;

    @DbColumn(comment = "默认通知地址")
    private String noticeUrl;

    @DbColumn(comment = "请求参数是否签名")
    private boolean reqSign;

    @DbColumn(comment = "响应参数是否签名")
    private boolean resSign;

    @DbColumn(comment = "回调信息是否签名")
    private boolean noticeSign;

    @DbColumn(comment = "是否记录请求的信息")
    private boolean record;

    @DbColumn(comment = "备注")
    private String remark;

    /**
     * 转换
     */
    @Override
    public PayApiConfigDto toDto() {
        return PayApiConfigConvert.CONVERT.convert(this);
    }
}

