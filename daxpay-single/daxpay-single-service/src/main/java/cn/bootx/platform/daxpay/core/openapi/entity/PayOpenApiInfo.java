package cn.bootx.platform.daxpay.core.openapi.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.core.openapi.convert.PayOpenApiInfoConvert;
import cn.bootx.platform.daxpay.param.openapi.PayOpenApiInfoParam;
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
@DbTable(comment = "支付开放接口管理")
@TableName("pay_open_api_info")
public class PayOpenApiInfo extends MpBaseEntity {

    @DbColumn(comment = "编码")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;

    @DbColumn(comment = "接口地址")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String api;

    @DbColumn(comment = "名称")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String name;

    @DbColumn(comment = "是否启用")
    private boolean enable;

    @DbColumn(comment = "是否开启回调通知")
    private boolean notice;

    @DbColumn(comment = "默认回调地址")
    private String noticeUrl;

    @DbColumn(comment = "请求参数是否签名")
    private boolean reqSign;

    @DbColumn(comment = "响应参数是否签名")
    private boolean resSign;

    @DbColumn(comment = "回调信息是否签名")
    private boolean noticeSign;

    @DbColumn(comment = "是否记录请求的信息")
    private boolean record;

    /**
     * 初始化
     */
    public static PayOpenApiInfo init(PayOpenApiInfoParam param){
        return PayOpenApiInfoConvert.CONVERT.convert(param);
    }

}

