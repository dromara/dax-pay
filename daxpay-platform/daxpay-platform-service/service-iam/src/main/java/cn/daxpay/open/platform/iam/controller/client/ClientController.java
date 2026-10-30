package cn.daxpay.open.platform.iam.controller.client;

import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.result.client.ClientResult;
import cn.daxpay.open.platform.iam.service.client.ClientQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// # 登录终端主数据
///
/// 只读列表, 供用户/角色/菜单等页下拉使用. 无启停、无 CRUD.
///
/// 前缀归 IAM 域 `/iam/client`(原 `/client`): `/client/**` 保留为对外开放命名空间,
/// 仅供无自有前端的客户端(APP/小程序/桌面端/设备)的匿名/签名端点使用;
/// 本端点消费方是运营端 Web 且需要登录, 属正式业务。
@Validated
@Tag(name = "登录终端")
@RestController
@RequestMapping("/iam/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientQueryService clientQueryService;

    /// 查询全部登录终端(需登录)
    @IgnoreAuth(login = true)
    @Operation(summary = "查询全部登录终端")
    @GetMapping("/find-all")
    public Result<List<ClientResult>> findAll() {
        return Res.ok(clientQueryService.findAll());
    }
}
