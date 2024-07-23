package cn.bootx.platform.core.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;


/**
 * kv键值对象
 *
 * @author xxm
 * @since 2021/5/18
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@Schema(title = "kv键值对象")
public class KeyValue {

    @Schema(description = "键")
    private String key;

    @Schema(description = "值")
    private String value;

}
