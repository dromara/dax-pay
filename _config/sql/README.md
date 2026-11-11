# SQL 脚本说明

装机先执行 `table.sql` 建表，再执行 `data.sql`（纯净版）或 `data-demo.sql`（演示版）。

> 旧版面板按 `table.sql → data.sql → iam_perm_menu.sql` 三步执行，第三步的文件已删除；`update-tables.sql` / `update-datas.sql` 增量脚本也已退役。面板须随本版升级为「`table.sql` + 数据文件」两步，旧三步式不再兼容。

## 一、装机

| 场景 | 依次执行 | 状态 |
|---|---|---|
| 生产 / 纯净版 | `table.sql` → `data.sql` | 可用 |
| 演示 / 开发版 | `table.sql` → `data-demo.sql` | 可用 |

- 表结构只有 `table.sql` 一份，纯净版与演示版共用；数据文件只含数据，两个数据文件**二选一**
- `data.sql` = `[2]` 纯净种子 + `[3]` 菜单种子
- `data-demo.sql` = `[2]` + `[3]` + `[4]` 演示数据段（含 `[4.6]` 装机期一致性断言）
- 必须在空库执行。两个文件只做建表与 INSERT，不含 DROP，重复执行会主键冲突
- 执行时开单事务、遇错即停（psql 的 `ON_ERROR_STOP=1` 与 `--single-transaction`）；`[4.6]` 断言失败会让整个安装事务回滚
- 数据异常直接重建库重装，不提供局部补丁
- 表结构以 dev 库为真相源，不维护手写建表脚本，也不维护跨版本增量脚本（发版只保证全新安装路径）

```bash
psql -v ON_ERROR_STOP=1 --single-transaction -f table.sql
psql -v ON_ERROR_STOP=1 --single-transaction -f data.sql        # 纯净版
psql -v ON_ERROR_STOP=1 --single-transaction -f data-demo.sql   # 演示版（与 data.sql 二选一）
```

### 纯净版内容（data.sql）

- 系统种子表 20 张：权限码/角色/角色权限、字典、地区、用户协议、支付元数据定义（渠道/产品/能力/方式/服务商）、敏感词库
- 内置超管 `bootx / 121212` 三件套 + 登录链路两行：
  - `iam_user_info` / `iam_user_expand_info`（id=1，扩展行 `sex` 置 NULL 防源头漂移）
  - `iam_user_password_security`（id=1，密码状态行）
  - `iam_user_role`（user_id=1 → role_id=1 `admin_admin`）
- `bootx` 密码由 `build/redact-data.mjs` 重置为开发默认密码 `121212`（防止开发库改密导致导出密码漂移），**生产部署后必须立即修改**；忘记密码执行 `reset-bootx-password.sql`
- **不含**任何商户域数据：`mch_*`、`mock_*`、`device_qr_code`、演示账号（`admin` / `csqysh` 等）、示例商户与 mock 通道元数据全部排除，由演示数据段承担
- **不含 dev 库测试残留**：种子表内的逻辑删除行（`deleted=true`）一律丢弃；dev 库的测试敏感词（`测试敏感词`，reject 级，会让生产误拦）行级排除，改由演示段 `[4.2]` 旁路注入

### 演示版内容（data-demo.sql）

- 演示账号（密码 `121212`）：
  - `admin`（运营端，role 1 `admin_admin`，普通运营管理员）
  - `csqysh`（商户端，role 2 `merchant_admin`，绑演示商户）
- 主演示商户 `M1999999999999`：默认应用 `A1999999999999999`、门店 `S1999999999990001/2`、码牌 `DEMOQR20261101001/2/3`、mock 通道商户 `MOCKM99999`（`mock_pay`，沙箱）
- 路由策略 1 条（basic 模式）+ 5 个支付渠道（`aggregate_pay` / `wechat` / `alipay` / `union_pay` / `douyin`）全部指向 mock 通道商户
- mock 凭证：全局单行 `mock_gateway_credential`（主键固定 1）+ 直连 `mock_pay_credential`（`gateway_base_url` 留空，回落子应用 yml）；两段 PEM 为公开测试钥
- 微信应用 `wx9999999999999999`（与 mock 内置种子一致）+ `wechat_jsapi` / `wechat_mini` 两个应用能力绑定
- 交易流水（近 30 天）：176 单 / 182 交易 / 13 退款（成功/处理中/失败/关闭四态齐全）/ 156 资金流水 / 186 通知任务 / 211 通知记录 / 6 条通道回调；含今日 3 笔「现场演示订单」（可退款/可关单）
- **仅适用于开发/演示环境**（要求 `daxpay.platform.config.sandbox-enabled=true`）；生产装机必须用 `data.sql`

## 二、目录

| 文件 | 内容 | 手编 |
|---|---|---|
| `table.sql` | 表结构（168 张表：建表 / 主键 / 索引 / 注释），纯净/演示共用 | 否 |
| `data.sql` | 纯净版数据 = `[2]` 纯净种子 + `[3]` 菜单种子（不含表结构） | 否 |
| `data-demo.sql` | 演示版数据 = `[2]` + `[3]` + `[4]` 演示段（旁路菜单/mock 元数据 + 剧本生成 + 装机断言） | 否 |
| `reset-bootx-password.sql` | `bootx` 密码重置回 `121212` | 是 |
| `build/export-sql.mjs` | 生成脚本（唯一入口）：连库导出 + 拼装 + 自检 + 原子落盘 | — |
| `build/redact-data.mjs` | 纯净种子脱敏规则（白名单表 / mock 行级排除 / bootx 行级保留 / 密码重置），同时旁路输出演示段所需的 mock 元数据 | — |
| `build/demo-gen.mjs` | 演示数据编译器，按 `table.sql` 列契约生成 INSERT（列名/必填/引用完整性校验） | — |
| `build/demo-dataset.mjs` | 演示数据剧本，唯一手编数据源（锚点 / 身份配置 / 通道 / 交易流水 / 装机断言） | **是** |

`table.sql` / `data.sql` / `data-demo.sql` 是构建产物，下一次重跑会覆盖，不要手编。

## 三、生成

```bash
node build/export-sql.mjs                     # 默认 --profile all：出 table.sql + data.sql + data-demo.sql
node build/export-sql.mjs --profile pure      # 只出 table.sql + data.sql
node build/export-sql.mjs --profile demo      # 出 table.sql + data-demo.sql
node build/export-sql.mjs --out-dir /tmp/x    # 试跑，不动仓内产物（先写 .tmp-export，断言全过才 rename）
```

- 数据源为 `daxpay-start/src/main/resources/application-dev.yml` 的 `spring.datasource`（dev 库 `daxpay-dev`，主机名 `postgresql` 由本机 hosts 解析）
- 本机无 psql 客户端，`pg_dump` / `psql` 均走 WSL docker `postgres:16`（版式必须与 PG16 服务端一致）
- 一份规则两个方向：`redact-data.mjs` 从纯净版排除的 mock 元数据行会旁路输出到 `.tmp-export/bypass-mock-metadata.sql`，演示段 `[4.2]` 直接复用，避免两线漂移
- 同一版本重跑，除文件头时间戳外字节一致

### 自动检查（写文件前）

任何一条不成立就停下，不落盘、原产物不动：

| 检查点 | 口径 |
|---|---|
| 建表数 | `CREATE TABLE` = 168 = 库 `pg_tables` 数，且双向差集为空（不漏表也不多表） |
| 表注释 | 168 / 168（每张表都有 `COMMENT ON TABLE`） |
| 列注释 | ≥ 2400（dev 库实测 2489） |
| 序列 | `CREATE SEQUENCE` = 0（主键全为雪花 `ASSIGN_ID`） |
| pg_dump 残留 | 无 `\restrict` / `\unrestrict` / `SET transaction_timeout` / `CREATE SCHEMA public` |
| 纯净种子 | 零 mock 行、零演示菜单、零演示锚点、零 `mch_*` / `mock_*` / `device_qr_code` 行 |
| bootx | `iam_user_info` / `iam_user_expand_info` / `iam_user_role` / `iam_user_password_security` 各恰好 1 行 |
| 无列名 INSERT | 硬失败（开源版列序与 plus 不同，位置锚定会静默错位，不提供回退） |
| 菜单段 | `[3]` 段必须带列名；演示菜单 id（Demos 6 条）剔除，真实商户分组菜单保留 |
| 演示段 | 关键锚点齐全、无 plus 独有表（`agent_*` / `pay_onb_*` / 费率限额/快照明细）、无 dev 示例商户编号、含 `[4.6]` 断言块、订单数 ≥ 150、`mock_gateway_credential` 主键 1、`pay_md_product_config(mock_pay, sandbox)` 存在 |

## 四、演示数据

本仓的 SQL 生成链自 DaxPay Plus 商业版移植，分两阶段（均已落地）：

- **阶段 A**：`table.sql` + `data.sql` 全链路，含空库导入验证
- **阶段 B**：`data-demo.sql` 演示段（2026-11-03 完成，空库导入验证通过）

| 段 | 内容 | 来源 |
|---|---|---|
| `[4.1]` 演示菜单 | Demos 6 条菜单 + 6 条角色绑定（`2,202,204,205,206,208`） | 旁路导出 |
| `[4.2]` 旁路元数据 | 渠道 `92005` / 产品 `96005,96006` / 产品能力 42 行 + 演示敏感词 1 条 | 旁路导出 |
| `[4.3]` 身份与配置 | 账号 `admin` / `csqysh`、商户 `M1999999999999`、应用 `A1999999999999999`、门店 `S1999999999990001/2`、码牌 `DEMOQR20261101001/2/3`、路由策略 + 5 渠道 | 剧本生成 |
| `[4.4]` 通道配置 | mock 全局/直连凭证（PEM）、微信应用 `wx9999999999999999` + 2 条能力绑定、`mock_pay` 沙箱生效环境 | 剧本生成 |
| `[4.5]` 交易流水 | 近 30 天 176 单 / 182 交易 / 13 退款 / 156 资金流水 / 186 通知任务 / 211 通知记录 / 6 条回调 | 剧本生成 |
| `[4.6]` 一致性校验 | `DO $$ … $$` 检查块（97 处断言），失败则整装回滚 | 编译器产出 |

- 演示菜单 id 已固定在 `export-sql.mjs` 的 `DEMO_MENU_IDS`；`4040130~4040132` 是真实商户分组菜单（`menu.payment.merchant.group.*`），**不是**演示菜单
- 开源版时间为 `timestamp(6) with time zone`，`demo-gen.mjs` 的时间工具为 `now()` / `now() - interval '…'`；`dayAt()` 先按 `Asia/Shanghai` 取日界再还原为 timestamptz
- 剧本已按开源版表集裁剪：无代理商（`agent_*`）、无进件（`pay_onb_*`）、无费率/限额/风控配置与费用快照/明细；`pay_trade` 不带 `method` / `capability` / `agent_no`
- `mock_gateway_credential` 主键**固定 id=1**（`MockChannelCredentialManager.SINGLETON_ID`），不落演示 ID 段
- 演示通道商户 `sandbox=true`，必须与 `pay_md_product_config(mock_pay).active_env='sandbox'` 匹配，否则路由以 `channelMchEnvMismatch` 拒绝（故要求 `sandbox-enabled=true` 的 dev 环境）
- 改演示数据只改 `build/demo-dataset.mjs`，然后重跑 `node build/export-sql.mjs`

## 五、常见问题

**Q：脚本报「检查不通过」怎么办？**
不会落盘，产物保持原样。按控制台明细排查：dev 库结构落后（改了代码没在库里执行 DDL）、列注释丢失、菜单导出格式异常。

**Q：装机报 `schema "public" already exists` 或 `\restrict` 语法错误？**
文件里混入了未清洁化的 pg_dump 原始输出。生成脚本会从表结构段与数据段分别过滤 `\restrict` / `\unrestrict`（pg_dump 16.10+ 的 psql 元命令，带随机 nonce）与 PG17+ 的 `SET transaction_timeout`。手工改过产物会重现。

**Q：报 `relation "xxx" already exists`？**
`table.sql` 只能执行一次。重复执行、或在已有表的库上装，都会撞这个错；重建库再装。

**Q：能单独重放菜单种子吗？**
不能。菜单种子是 `data.sql` 的 `[3]` 段，段首是两条 DELETE 全量重建，单独摘出来会清掉库里已有菜单（含演示菜单）。

**Q：纯净版登录后没有商户数据？**
预期行为。纯净版只带 `bootx` 与系统种子，商户/应用/通道/订单需自行创建，或改用 `data-demo.sql` 演示数据。

**Q：演示版装机后扫码支付失败，报「通道商户环境与产品当前生效环境不一致」？**
演示数据固定 `sandbox=true`，只适用于 `sandbox-enabled=true` 的开发/演示环境。生产配置（`sandbox-enabled=false`）启动时会把 `mock_pay` 的 `active_env` 重置为 `prod`，与演示通道商户不再匹配；生产装机请用 `data.sql`。
