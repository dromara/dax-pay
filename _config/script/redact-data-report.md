# DaxPay Data SQL 敏感数据清除方案报告

> 工具：`redact-data.mjs`（同目录） ｜ 输入：`../sql/data.sql`（pg_dump --inserts 导出） ｜ 输出：`../sql/data.sql`
> 用途：生成**干净安装包/演示数据**，供他人导入；清除全部敏感数据与运行时业务数据，仅保留系统运行所必需的种子数据与一个内置超级管理员账号。

---

## 1. 背景与目标

从生产/测试库导出 `../sql/data.sql` 后，其中包含大量敏感信息（支付通道密钥、证书、应用 secret、密码哈希、授权令牌等）以及与安装包无关的业务/交易数据。本方案的目标是：

1. **彻底清除敏感数据**——密钥/证书/secret/令牌/密码等，杜绝明文与密文外泄。
2. **清除业务与运行时数据**——商户、订单、交易、日志、通知等安装包不需要的数据。
3. **保留系统运行必需的种子数据**——权限码、菜单、角色、字典、地区、支付能力元数据等（`datas.sql` 不在仓库，清掉后无现成脚本恢复）。
4. **保留一个内置超级管理员账号**——确保导入后可登录。

## 2. 数据源格式特征

| 特征 | 说明 |
|------|------|
| 导出方式 | `pg_dump --inserts` |
| 语句形态 | `INSERT INTO public.<表名> VALUES (...)`，**不带列名** |
| 分段标记 | `-- Data for Name: <表名>; Type: TABLE DATA; Schema: public; Owner: bootx` |
| 跨行 INSERT | 6 条（`base_user_protocol_version`×3、`notify_notice`×2、`starter_audit_unipay_log`×1），值内含真实换行 |
| 规模 | 约 14500 行，4.0 MB；约 80 张表有数据，13562 条 INSERT |

**关键约束**：因无列名，脱敏**不能按列定位**，只能按“表名 + 整表/行级”决策。脚本通过 `INSERT INTO public.<表>` 解析表名，跨行 INSERT 累积到行尾 `);` 视为一条完整语句。

## 3. 敏感数据分布（清除对象识别）

通过实体类 `@TableName` + `DataEncryptTypeHandler` 标注与 SQL 数据交叉分析，敏感数据分 5 类：

| 类别 | 代表表 | 敏感字段类型 | 存储形态 |
|------|--------|------------|---------|
| **A. 通道密钥配置** | 17 张 `*_key_config`（alipay/wechat/douyin/lakala/ums/union/stripe/yeepay/leshua/fuyou/dougong/hkrt/hmpay/vbill/adapay 的 direct/isv） | api_key / private_key / public_key / cert / secret_key / encrypt_key / sm4_key / 证书密码 | AES-256-GCM 加密（`v1:` 前缀密文） |
| **B. 应用 secret / 授权令牌** | `wx_mch_app`、`wx_platform_app`、`dy_mch_app`、`dy_platform_app`（app_secret）；`alipay_isv_channel_merchant`（app_auth_token）；`pay_platform_mobile_app`（小程序 appSecret）；`iam_social_login_config`（第三方登录 client_secret） | 应用通信密钥 / 授权令牌 | 加密存储 |
| **C. 商户凭据** | `mch_credential`（public_key/secret_key）、`pay_easy_pay_credential`（md5_key/public_key） | 商户对接 RSA 密钥 / 签名密钥 | 加密 / 部分明文 |
| **D. 用户认证** | `iam_user_info`（bcrypt 密码哈希 `$2a$10$`）、`iam_user_password_history`（密码历史）、`iam_user_two_factor`（2FA secret + backup_codes）、`iam_user_social`（第三方 openid/昵称/头像） | 密码哈希 / TOTP 密钥 / 个人隐私 | 哈希 / 加密 / 明文 |
| **E. 平台配置** | `system_platform_config`（jsonb，含 OSS accessKey/secretKey **明文**）、`system_platform_encrypt_config`（加密的平台认证配置） | OSS 密钥 / 支付宝/微信/抖音开放平台认证 | 明文 jsonb / 加密 |

> ⚠️ `system_platform_config.config_data` 为**明文 jsonb**，其中 `oss` 配置项含 OSS `accessKey`/`secretKey` 明文——是最高优先级清除点。

## 4. 脱敏策略：白名单架构

经权衡，采用**白名单保留**而非黑名单跳过：

- 黑名单需枚举 60+ 张敏感/业务表，清单冗长且未来新增表默认保留（有遗漏风险）。
- 白名单只需列出“必须保留”的 18 张种子表，**其余一律清除**，对未来新增表默认安全，更契合“安装包”用途。

### 决策三原则

```
对每条 INSERT：
  ① 表 ∈ KEEP_TABLES（19 张系统种子）    → 整表原样保留
  ② iam_user_info 且 id=1 (bootx 内置超管) → 该行保留
  ③ 其余                                  → 整表跳过（写一行审计注释 -- REDACTED: <table>）
```

## 5. 完整表分类清单

### 5.1 保留清单（19 张系统种子表，整表保留）

| 类别 | 表名 | 条数 | 用途 |
|------|------|------|------|
| 权限 | `iam_perm_code` | 163 | 权限点定义 |
| 菜单 | `iam_perm_menu` | 149 | 菜单/路由（i18n_key 真相源） |
| 角色 | `iam_role` | 2 | admin_admin / merchant_admin 内置角色 |
| 角色权限 | `iam_role_code` | 128 | 角色-权限码关联 |
| 角色菜单 | `iam_role_menu` | 121 | 角色-菜单关联 |
| 字典 | `system_dict` | 2 | 字典分类 |
| 字典项 | `system_dict_item` | 3 | 字典枚举值 |
| 地区 | `base_area` | 2984 | 区/县 |
| 城市 | `base_city` | 342 | 市 |
| 城市邻接 | `base_city_adjacent` | 1808 | 相邻城市关系（2026-08-30 复核补列，首版清单遗漏未列但脚本一直在白名单内） |
| 省份 | `base_province` | 31 | 省 |
| 支付元数据 | `pay_md_provider` | 7 | 支付通道提供商（wechat/alipay/…） |
| 支付元数据 | `pay_md_channel` | 18 | 通道 |
| 支付元数据 | `pay_md_method` | 27 | 支付方式 |
| 支付元数据 | `pay_md_capability` | 26 | 支付能力 |
| 支付元数据 | `pay_md_provider_method` | 26 | 提供商-方式关联 |
| 支付元数据 | `pay_md_product` | 26 | 支付产品定义 |
| 支付元数据 | `pay_md_product_capability` | 222 | 产品-能力关联 |
| 系统配置 | `system_sensitive_word` | 1 | 敏感词库 |

**行级保留**：

| 表 | 保留行 | 标识 |
|----|--------|------|
| `iam_user_info` | 1 行 | `id=1`（username=`bootx`，name=`超级管理员`，client_code=`admin`）—— 系统内置超级管理员 |
| `iam_user_expand_info` | 1 行 | `id=1`——内置超管的扩展信息。**必须与 `iam_user_info` 同 id 保留**：登录后 `getLoginAfterUserInfo` 按 id 查此表，缺行则抛 `UserInfoNotExistsException`，token 签发成功也无法进入系统 |

> 该账号在 `iam_user_role` 中无角色绑定记录，推断为代码层面识别的内置超管（拥有全部权限），故 `iam_user_role` 可整表清除。
> `iam_user_password_security` 虽也属超管关联表，但 `getPasswordStatus` 用 `.orElse(null)` 优雅降级（返回 `initialPassword=true`），缺行不卡登录，故**不**纳入行级保留，保持整表清除。

### 5.2 清除清单（约 63 张表，整表跳过）

#### A. 支付通道密钥配置（`*_key_config`）
`alipay_direct_app_key_config`、`douyin_direct_key_config`、`wechat_direct_key_config`、`wechat_isv_key_config`（仓库种子数据仅此 4 张有数据，其余 `*_key_config` 表存在但无 INSERT；脚本对无数据表自然无操作）。

#### B. 应用 secret / 授权令牌
`wx_mch_app`、`wx_platform_app`、`dy_mch_app`、`dy_platform_app`、`alipay_isv_channel_merchant`、`pay_platform_mobile_app`、`iam_social_login_config`。

#### C. 商户凭据
`mch_credential`、`pay_easy_pay_credential`。

#### D. 用户认证与隐私
`iam_user_info`（仅留 id=1）、`iam_user_password_history`、`iam_user_two_factor`、`iam_user_social`、`iam_user_role`、`iam_user_password_security`、`iam_user_dashboard_preference`、`mch_user`。（注：`iam_user_expand_info` 已改为行级保留 id=1，见 §5.1）

#### E. 平台配置
`system_platform_config`（含明文 OSS 密钥，整表清）、`system_platform_encrypt_config`（加密平台认证配置）。

#### F. 商户 / 应用 / 门店
`mch_info`、`mch_app_info`、`mch_channel_merchant`、`mch_store_info`、`alipay_direct_app`、`alipay_direct_app_auth_config`、`alipay_direct_channel_merchant`、`douyin_direct_channel_merchant`、`wechat_direct_channel_merchant`、`wechat_isv_channel_merchant`、`wx_platform_app_capability`、`wx_channel_app_capability`。

#### G. 支付业务配置
`pay_md_product_config`、`pay_route_basic_config`、`pay_route_strategy`、`pay_gateway_pay_config`、`pay_gateway_pay_client_env`、`pay_gateway_cashier_item`、`pay_easy_pay_config`。

#### H. 运行时交易与日志
- **订单/交易**：`pay_normal_order`、`pay_gateway_order`、`pay_refund_order`、`pay_trade`
- **回调/同步/关闭**：`pay_callback_record`、`pay_close_record`、`pay_sync_record`
- **风控**：`pay_blacklist`、`pay_risk_hit`
- **通知**：`notify_notice`、`notify_notice_read`、`mch_notice_record`、`mch_notice_task`
- **审计日志**：`starter_audit_login_log`、`starter_audit_operate_log`、`starter_audit_unipay_log`
- **文件/设备/杂项**：`starter_platform_file_record`、`device_qr_code`、`system_sensitive_word_hit`、`base_user_protocol`、`base_user_protocol_version`

## 6. 脚本设计：`redact-data.mjs`

| 项 | 说明 |
|----|------|
| 语言 | Node.js ESM（`.mjs`），**零第三方依赖**（`node:fs` + `node:readline`） |
| 处理方式 | 流式逐行，内存友好（4 MB 文件） |
| 跨行处理 | 维护 `inInsert` 状态 + 缓冲，累积到行尾 `);` 视为一条语句结束，再做表名决策 |
| 输出头 | 写入时间戳、模式、清除范围说明，便于审计 |
| 审计注释 | 每张被清除的表首次出现时写 `-- REDACTED: <table>`，原 INSERT 不输出 |
| 统计报告 | 结尾打印每张表的 INSERT 总数/保留/清除数 |

### 核心逻辑（伪代码）

```js
const KEEP_TABLES = new Set([
  'iam_perm_code','iam_perm_menu','iam_role','iam_role_code','iam_role_menu',
  'system_dict','system_dict_item',
  'base_area','base_city','base_province',
  'pay_md_provider','pay_md_channel','pay_md_method','pay_md_capability',
  'pay_md_provider_method','pay_md_product','pay_md_product_capability',
  'system_sensitive_word',
])

// 内置超管关联表行级保留：id=1 (bootx) —— iam_user_info + iam_user_expand_info
const BOOTX_ADMIN_TABLES = new Set(['iam_user_info', 'iam_user_expand_info'])
const BOOTX_ADMIN_RE = /^INSERT INTO public\.(?:iam_user_info|iam_user_expand_info)\s+VALUES\s*\(\s*1\s*,/

function handleInsert(table, lines) {
  if (KEEP_TABLES.has(table)) return output(lines)                       // ① 种子表保留
  if (BOOTX_ADMIN_TABLES.has(table) && BOOTX_ADMIN_RE.test(lines[0])) return output(lines) // ② bootx 行保留
  // ③ 其余清除
  writeRedactedMark(table)
}
```

### 用法

```bash
node redact-data.mjs <input.sql> <output.sql>
# 例：
node redact-data.mjs data_raw.sql data.sql
```

## 7. 验证方案

### 7.1 统计核对
脚本结尾输出三类统计：
- `[保留]` 19 张种子表 INSERT 数（应与原文件一致）
- `[行级保留]` iam_user_info 14 → 1（保留 id=1）
- `[清除]` 其余表 INSERT 数 → 0

### 7.2 敏感关键词兜底（输出文件应全部为 0）

```powershell
$f='data.sql'
# 明文敏感值
(Select-String -LiteralPath $f -Pattern 'accessKey|secretKey').Count        # = 0
# 加密密文前缀（清除全部加密字段后应归零）
(Select-String -LiteralPath $f -Pattern 'v1:').Count                        # = 0
# 密码哈希
(Select-String -LiteralPath $f -Pattern '\$2[aby]\$10\$').Count             # 仅剩 1（bootx）
# 证书明文
(Select-String -LiteralPath $f -Pattern 'BEGIN CERTIFICATE|BEGIN PRIVATE KEY').Count  # = 0
# OSS 配置行
(Select-String -LiteralPath $f -Pattern "VALUES\s*\(\s*\d+\s*,\s*'oss'\s*,").Count    # = 0
```

### 7.3 完整性核对（保留项应存在）
```powershell
# bootx 超管仍在
(Select-String -LiteralPath $f -Pattern "INSERT INTO public\.iam_user_info VALUES \(1,").Count  # = 1
# 菜单种子仍在
(Select-String -LiteralPath $f -Pattern '^INSERT INTO public\.iam_perm_menu').Count            # = 149
# 支付元数据仍在（注意加 \s+VALUES 排除 pay_md_provider_method 前缀误匹配）
(Select-String -LiteralPath $f -Pattern '^INSERT INTO public\.pay_md_provider\s+VALUES').Count   # = 7
```

## 8. 预期结果

| 指标 | 原始 | 实测脱敏后 |
|------|------|--------|
| 文件大小 | 4186110 B (~4.0 MB) | 429098 B (~0.41 MB) |
| 有数据表数 | ~80 | 19（18 种子 + iam_user_info 仅 1 行 bootx） |
| 保留 INSERT | 13562 | 4279（4278 种子 + 1 bootx） |
| 清除 INSERT | — | 9270（61 张表） |
| `v1:` 加密密文 | 28 | 0 ✓ |
| `accessKey`/`secretKey` | 各 1 | 0 ✓ |
| 密码哈希 `$2a$10$` | 14 | 1（bootx）✓ |
| `BEGIN CERTIFICATE` | — | 0 ✓ |
| 敏感表（密钥/凭据/secret） | 有 | 全空 ✓ |

## 9. 边界与风险说明

1. **`bootx` 账号密码哈希保留**：导入者需知悉 daxpay 默认密码，登录后应立即改密。若需彻底零密码，则放弃保留账号，改为依赖系统首启建管理员机制（需后端支持）。
2. **`pay_md_*` 元数据定义保留**：这是系统支持的支付通道/方式/产品的“枚举定义”，清除后无 `datas.sql` 恢复，故保留。商户侧的产品开通配置（`pay_md_product_config`）属业务数据，清除。
3. **未来新增表默认被清除**：白名单架构下，升级后若新增业务表，其数据默认不出现在安装包——符合安装包语义，但每次升级需人工确认新增的“系统种子表”是否应补入 `KEEP_TABLES`。
4. **跨行 INSERT 处理**：脚本以行尾 `);` 判断语句结束，对当前 6 条跨行 INSERT（均位于清除表）安全；若未来保留表出现含 `);` 字符串的跨行值，需复核。
5. **脚本状态**：`redact-data.mjs` 已按本报告第 6 节改写为**白名单版本并验证通过**（实测结果见 §8）。

## 10. 实施步骤（Checklist）

- [x] 按 §6 改写 `redact-data.mjs` 为白名单架构（`KEEP_TABLES` + `BOOTX_ADMIN_RE`）
- [x] 运行 `node redact-data.mjs data.sql data-redacted.sql`
- [x] 执行 §7 验证（统计 + 关键词兜底 + 完整性核对）
- [ ] （可选）导入干净 PG 库验证应用可启动、`bootx` 可登录

## 11. 2026-08-30 对应性复核与再导出

### 11.1 复核背景

`data.sql` 上次导出为 2026-08-23，其后一周内多批次上线涉及库表与种子变更（异常订单/资金流水、邮件通知、passkey、terminal 列等），需再导出并复核白名单是否仍然对应。

### 11.2 表集合变化（活库 vs 08-23 旧导出）

活库（pg_dump 18.6 实测）共 **137 张表**，与 08-25 Navicat 版 `table.sql` 建表数一致（期间靠手工归档保持同步）。08-23 后真正新增 4 张表：

| 新表 | 性质 | 白名单处置 | 判定 |
|------|------|-----------|------|
| `iam_user_passkey` | 用户 WebAuthn 凭据 | 默认整表清除 | ✅ 正确 |
| `notify_mail_record` | 邮件发送运行记录 | 默认整表清除 | ✅ 正确 |
| `pay_abnormal_order` | 异常订单（业务单） | 默认整表清除 | ✅ 正确 |
| `pay_fund_flow` | 资金流水（业务单） | 默认整表清除 | ✅ 正确 |

**结论：4 张新表全部为业务/运行时表，无新增系统种子表，`KEEP_TABLES` 无需任何变更**——印证了 §9.3 白名单「对未来新增表默认安全」的设计。其余核验项：

- 19 张白名单表全部存在于活库且有数据 ✅
- bootx 行级保留仍精确命中：两表 `VALUES (1,` 首列位置不变，14 行 → 各保留 1 行 ✅
- 保留表无跨行 INSERT 误切（审计行数与原始导出完全一致）✅
- 唯一变更为文档级勘误：首版清单称「18 张」种子表，实际 **19 张**（`base_city_adjacent` 在脚本白名单内但未列入 §5.1 清单），已修正本文档与脚本头注释

### 11.3 再导出指标（2026-08-30）

| 指标 | 原始导出 | 脱敏后 |
|------|---------|--------|
| 文件大小 | 7,095,999 B (~7.0 MB) | 572,339 B (~559 KB) |
| 有数据表数 | 101（另 36 张空表） | 21（19 种子 + bootx 两表各 1 行） |
| INSERT 条数 | 23,703 | 6,152（6,150 种子 + 2 bootx） |
| 清除 | — | 80 张表 17,525 条 |
| `v1:` 密文 / accessKey / PEM | 有 | 0 ✅ |
| 密码哈希 | — | 1（bootx，交付预期） |

种子增量（vs 08-23）：`iam_perm_code` 163→181、`iam_perm_menu` 149→170、`iam_role_code` 128→141、`iam_role_menu` 121→133，新增菜单 612（异常订单）/613（资金流水）/310（邮件记录）等均已在列。

### 11.4 导出方式变更（table.sql）

自 2026-08-30 起 `table.sql` 由 Navicat 手动导出切换为 **pg_dump 脚本化导出**（可复现、与 data.sql 同源同工具）：

```bash
# 结构（含 DROP IF EXISTS 重建语义）
pg_dump -h 192.168.1.229 -U bootx -d daxpay-dev --schema-only --clean --if-exists --no-owner --no-privileges
# 数据（--inserts 与脱敏脚本格式假设一致）
pg_dump -h 192.168.1.229 -U bootx -d daxpay-dev --data-only --inserts --no-owner --no-privileges
node redact-data.mjs <raw> ../sql/data.sql
```

配套调整：

- pg_dump 18 客户端会输出 `\restrict`/`\unrestrict` psql 元命令（非 psql 工具执行会报错），导出后需剥离
- `analyze-table.mjs`、`find-verbose-comments.mjs` 已适配 Navicat / pg_dump 双格式（含 `timestamp(6) with time zone` 三词类型归一，避免 timestamptz 误报）

### 11.5 全新导入实测（本地容器临时库）

在本地 docker postgres 容器建 `daxpay_export_verify` 临时库（不触碰源库），按 README 顺序执行 `table.sql → data.sql`（`ON_ERROR_STOP=1`）：**零错误**；137 表建成，种子行数与源库一致，bootx 两行为仅有的用户记录，`system_platform_config`/`*_key_config`/`pay_trade`/`pay_close_record` 等全部为 0 行；验后已 dropdb。

### 11.6 遗留事项

- `notify_mail_record` 的 2 条冗长枚举注释已于同日治理完毕：源库 `COMMENT ON` + 实体类注释同步 + `table.sql` 重导 + `update-tables.sql` 补增量，find-verbose-comments 复扫 0 条
- §10 的「导入干净 PG 库验证」勾选项在本次 §11.5 已完成等价实测

---

## 12. 2026-09-11 内置演示账号 +「全新商户」

设计依据详见 `_doc/design/内置演示账号与全新商户种子方案-2026-09-11.md`。

### 12.1 变更目标

data.sql 从「只带 bootx 一个内置超管」升级为**自带一套可登录、可操作的演示账号与商户**，供对外演示：

| 账号 | 端 | 角色 | 商户 |
|------|----|------|------|
| `bootx` | admin | 内置超管（代码识别，无角色绑定） | — |
| `csadmin` | admin | role 1 `admin_admin` | — |
| `csqysh` | merchant | role 2 `merchant_admin` | M1784445131420「示例商户」 |

商户以**「全新形态」**交付 —— 等价于「刚在运营端点完『新增商户』」的状态，不含任何后续配置。

### 12.2 脚本改造（`redact-data.mjs`）

| 项 | 改造前 | 改造后 |
|----|--------|--------|
| 判定层次 | `KEEP_TABLES` + `BOOTX_ADMIN_RE`（硬编码 `id=1`） | `KEEP_TABLES` + `DEMO_ROW_IDS`（表 → 主键 id 集合，含 bootx/csadmin/csqysh 及商户身份行） |
| 密码重置范围 | 仅 `id=1` | 三个演示账号 id（正则交替，长 id 在前） |
| 演示数据校验 | 无 | **新增**：行级白名单表若一条都没命中 → 打印告警并**非零退出**，防演示数据随包静默丢失 |
| pg_dump 18 兼容 | 需外层 sed 剥离 | **内置**：自动跳过 `\restrict` / `\unrestrict` 与 `SET transaction_timeout`（后者 PG17+ 才有，PG16 服务端会报 `unrecognized configuration parameter`），流水线少一步且不再依赖脆弱的 shell 转义 |

另修复一处判据缺陷：密码列替换原先以「替换后字符串是否变化」判断是否生效，当开发库该账号密码**本就是**该固定哈希时会误判为「结构不符」而非零退出；改为先单独 `test()` 正则是否命中，再执行替换（实测修前 2/3、修后 3/3）。

> `DEMO_ROW_IDS` 与 `DEMO_PASSWORD_USER_IDS` 中的 id 固化自 dev 库现网值，保证产物确定性。**重建演示账号/商户后必须同步更新该清单**，否则脚本会以非零退出报错。

### 12.3 保留清单（8 张表 / 14 行）

| 表 | 保留行数 | 说明 |
|----|---------|------|
| `iam_user_info` | 3 | bootx + csadmin + csqysh，密码统一重置为固定哈希 |
| `iam_user_expand_info` | 3 | 同上三个 id（登录后拉用户信息必需） |
| `iam_user_password_security` | 2 | csadmin + csqysh，见 §12.4 |
| `iam_user_role` | 2 | csadmin→role 1、csqysh→role 2 |
| `mch_info` | 1 | 商户本体（示例商户） |
| `mch_user` | 1 | 商户 ↔ 账号绑定（登录后解析 mchNo 必需） |
| `mch_app_info` | 1 | **仅** `default_app=true` 的默认应用 |
| `mch_store_info` | 1 | **仅** `default_store=true` 的默认门店 |

连同 18 张系统种子表（`iam_perm_code`/`iam_perm_menu`/`iam_role`/`iam_role_code`/`iam_role_menu`、`base_*`、`pay_md_*` 元数据、`system_dict*`、`system_sensitive_word`）共 26 张表、4369 条 INSERT。

### 12.4 两个必须处理的点（否则演示账号用不了）

1. **`iam_user_password_security` 必须随包交付**：`PasswordStatusCheck` 对超管直接 `return`（这就是 bootx 可以没有该行的原因），但非超管账号缺行会降级为 `initialPassword=true`，登录后除改密等白名单路径外**全部 40302**，等于卡在改密页进不去系统。交付值：`initial_password=false` + `password_expire_time=2099-12-31`（`needChangePassword` 会比较过期时间，原值的 2026-12 日期将来装包就过期）。
2. **`iam_user_password_history` 必须保持剔除**：bcrypt 每次加盐不同，历史里的哈希文本与固定哈希不同但 `checkpw("121212", ...)` 为真，`validatePasswordHistory` 命中即抛 `historyDuplicate`，导致演示账号**改不回 121212**。不给演示账号开例外。

### 12.5 实测结果（2026-09-11）

- **流水线**：`pg_dump --data-only --inserts` → `node redact-data.mjs`，退出码 0；密码重置 3/3；演示行保留数 8 张表全部命中
- **干净库导入**：新建临时库按 `table.sql → data.sql` 顺序执行，`ON_ERROR_STOP=1` 下**零错误**；验后已 dropdb
- **敏感扫描**：`v1:` / `accessKey` / `secretKey` / `BEGIN PRIVATE KEY` / `BEGIN CERTIFICATE` 命中数**均为 0**；`$2a$10$` 命中 3（三个演示账号）
- **商户形态**：`mch_channel_merchant` / `mch_credential` / `device_qr_code` / `pay_normal_order` / `pay_trade` / `pay_close_record` / `mch_notice_record` / `iam_user_password_history` / `system_platform_config` **全部 0 行**；`mch_app_info` 与 `mch_store_info` 各 1 行（默认）
- **权限种子**：role 1 授权 80 权限码 / 82 菜单，role 2 授权 66 权限码 / 56 菜单
- **密码可用性**：用项目同款 hutool `BCrypt.checkpw("121212", 交付哈希)` = `true`（反例 `123456` = `false`）

### 12.6 产物变化

| 指标 | 改造前 | 改造后 |
|------|--------|--------|
| 文件大小 | 438 KB | 443 KB |
| 有数据表数 | 20 | 26 |
| 含账号数 | 1（bootx） | 3 |
| 含商户数 | 0 | 1（示例商户，全新形态） |
