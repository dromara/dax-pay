package cn.bootx.platform.iam.entity.client;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.iam.convert.client.ClientConvert;
import cn.bootx.platform.iam.param.client.ClientParam;
import cn.bootx.platform.iam.result.client.ClientResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 认证终端
 *
 * @author xxm
 * @since 2021/8/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("iam_client")
@Accessors(chain = true)
public class Client extends MpBaseEntity implements ToResult<ClientResult> {

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 是否系统内置 */
    private boolean internal;

    /** 备注 */
    private String remark;

    /** 创建对象 */
    public static Client init(ClientParam in) {
        return ClientConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public ClientResult toResult() {
        return ClientConvert.CONVERT.convert(this);
    }

}
