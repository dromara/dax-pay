#!/usr/bin/env node
/**
 * Data SQL 敏感数据清除脚本（白名单 + mock 行级排除架构）
 *
 * 用途：生成纯净版生产种子(data.sql), 供发版装机使用。自 DaxPay Plus 商业版
 *       _config/sql/build/redact-data.mjs 移植(2026-11-02), 按开源版库结构重写常量层：
 *       ① KEEP_TABLES 对齐开源版 20 张系统种子表(开源版无 base_city_adjacent /
 *          alipay_mcc_const / wechat_settlement_const / device_qr_code_template / pay_print_template)；
 *       ② 新增 BOOTX_LINK_RULES: 开源版 bootx 超管登录还依赖 iam_user_role(user_id=1) 与
 *          iam_user_password_security(id=1) 两行(plus 版架构无此依赖)；
 *       ③ MOCK_ROW_RULES 对齐开源版 mock 元数据真实主键(渠道 92005 / 产品 96005,96006)；
 *       ④ 删除全部「无列名 INSERT 位置回退」分支，改为硬失败——开源版各表列序与 plus 不同，
 *          位置锚定会静默错位，宁可中止导出也不产出错位的种子。
 * 策略：保留系统种子表(KEEP_TABLES) + bootx 内置超管(id=1)及其登录链路关联行，其余整表清除；
 *       种子表内的逻辑删除行一律丢弃(dev 库测试残留, 纯净/demo 都不带)；
 *       种子表内的 mock 演示元数据与 dev 测试敏感词按 MOCK_ROW_RULES 行级排除(旁路输出供 demo 侧复用)；
 *       bootx 密码重置为开发默认密码(见 BOOTX_DEV_PASSWORD_HASH)、扩展行 sex 置空(防源头漂移)。
 *
 * 决策序：
 *   ① 表 ∈ KEEP_TABLES（系统种子）              → 行级过 MOCK_ROW_RULES 排除 mock 后保留
 *   ② 内置超管关联表且 id=1 (bootx)              → 该行保留，同表其余行清除；
 *      其中 iam_user_info 的密码列替换为开发默认密码哈希
 *      （开发库密码常被改动，原样保留会导致每次导出的 data.sql 登录密码漂移）
 *      （iam_user_expand_info 扩展行 sex 列置 NULL——dev 源头常被登录测试改为
 *       'female' 等，纯净版口径清 NULL，机制同密码重置防漂移；失败仅标记不阻断）
 *      （iam_user_info 账号主表 + iam_user_expand_info 扩展信息，
 *       后者登录后拉用户信息必需，缺则抛 UserInfoNotExistsException）
 *   ③ 表 ∈ BOOTX_LINK_RULES（bootx 登录链路关联行） → 只保留规则列命中白名单值的行
 *      （iam_user_role.user_id=1 角色绑定 / iam_user_password_security.id=1 密码安全行）；
 *       一行未命中即失败(bootx 无角色或密码状态行异常, 导出的种子登录不进系统)
 *   ④ iam_perm_menu / iam_role_menu              → 硬错误退出（菜单种子另有链路, 见 export-sql.mjs；
 *      误传本工具说明上游管线接错, 静默清除会产出空菜单种子）
 *   ⑤ 其余                                       → 整表跳过（写审计注释）
 *
 * 格式假设：**只接受** pg_dump --column-inserts（INSERT INTO public.表名 (列...) VALUES (...)）。
 *           无列名 INSERT(旧 --inserts 格式)一律硬失败退出：开源版列序与历史位置锚定假设不一致，
 *           静默按位置取值会产出错位数据，不提供回退路径。
 *           自动处理跨多行 INSERT（值内含换行，累积到行尾 ");" 视为语句结束）。
 *
 * 用法：node redact-data.mjs <input.sql> <output.sql> [--bypass-dir <目录>]
 *       --bypass-dir: 被排除的 mock 元数据行旁路写入 <目录>/bypass-mock-metadata.sql,
 *                     供 demo 侧(data-demo.sql [4.2] 段)复用; 未指定时仅统计不落盘。
 * 注意：input 必须是 pg_dump 原始 dump，勿对本脚本产物重复处理
 *      （产物头部注释属非 INSERT 行会被透传，重复处理会叠加出双头部）。
 */

import { createReadStream, createWriteStream, mkdirSync, writeFileSync } from 'node:fs'
import { createInterface } from 'node:readline'
import { argv, exit, cwd } from 'node:process'
import { isAbsolute, join } from 'node:path'
import { pathToFileURL } from 'node:url'

// ---------- 白名单：系统种子表（整表保留, 行级再过 mock 排除） ----------
export const KEEP_TABLES = new Set([
  // 权限/角色（菜单与角色菜单绑定种子由 export-sql.mjs 的 [3] 段单独产出, 不在 data.sql 的白名单里）
  'iam_perm_code',
  'iam_role',
  'iam_role_code',
  // 字典
  'system_dict',
  'system_dict_item',
  // 地区
  'base_area',
  'base_city',
  'base_province',
  'base_street',
  // 默认用户协议/隐私政策种子(纯默认文本)
  'base_user_protocol',
  'base_user_protocol_item',
  'base_user_protocol_version',
  // 支付元数据定义
  'pay_md_provider',
  'pay_md_channel',
  'pay_md_method',
  'pay_md_capability',
  'pay_md_provider_method',
  'pay_md_product',
  'pay_md_product_capability',
  // 敏感词库
  'system_sensitive_word',
])

// ---------- 行级排除规则(rules)：mock 演示元数据, 纯净版生产种子不得包含 ----------
// 结构: 表 -> [ {列: 值集合}, ... ], 同一对象内多列为 AND、对象之间为 OR;
// 下面每张表都写成「多个单列对象」= 命中任一条件即排除(mock 的主键或编码任一漂移都能拦住)。
// 注意 pay_md_product 的编码列是 code(不是 product_code), 产品能力表的列才是 product_code。
// 开源版真实值(dev 库 2026-11-02 核实): 渠道 id=92005/code=mock; 产品 id=96005(mock_pay)/96006(mock_isv);
// 产品能力 product_code in (mock_pay, mock_isv) 共 42 行。
// 反例提醒: 渠道 id=92001 在开源版是 stripe(真实通道, 必须留在纯净种子), 不能按 id 段误删。
export const MOCK_ROW_RULES = new Map([
  ['pay_md_channel', [{ id: new Set(['92005']) }, { code: new Set(['mock']) }]],
  ['pay_md_product', [{ id: new Set(['96005', '96006']) }, { code: new Set(['mock_pay', 'mock_isv']) }]],
  ['pay_md_product_capability', [{ product_code: new Set(['mock_pay', 'mock_isv']) }]],
  // dev 库测试敏感词(status=enable + level=reject): 纯净版装机会误拦含该词/该串的业务内容,
  // demo 段依赖它(命中样本 3 条 + 断言指向), 故行级排除并旁路给 [4.2] 段
  ['system_sensitive_word', [{ id: new Set(['2077569027575820288']) }]],
])

// 菜单两表不经本工具(种子链路在 export-sql.mjs 的 [3] 菜单段), 误传入即硬错误
const MENU_TABLES = new Set(['iam_perm_menu', 'iam_role_menu'])

// ---------- 行级保留：内置超管 bootx(id=1) 的关联表 ----------
// iam_user_info: 账号主表
// iam_user_expand_info: 用户扩展信息（登录后拉用户信息必需，缺则 getLoginAfterUserInfo 抛 UserInfoNotExistsException）
export const BOOTX_ADMIN_TABLES = new Set(['iam_user_info', 'iam_user_expand_info'])

// ---------- 行级保留：bootx 登录链路的其余关联行（开源版专有, plus 架构无此依赖） ----------
// iam_user_role: 超管角色绑定(role_id=1 admin_admin)，缺则登录后无任何菜单权限；
// iam_user_password_security: 密码状态行，非超管缺行会被判为初始密码拦在改密页（超管虽豁免，
//   仍按 plus 版「账号三件套」口径保留，保证 bootx 在任何校验分支下行为一致）。
// 结构: 表 -> { 列, 允许值集合 }；只保留命中行，其余行属业务数据一律剔除。
export const BOOTX_LINK_RULES = new Map([
  ['iam_user_role', { col: 'user_id', values: new Set(['1']) }],
  ['iam_user_password_security', { col: 'id', values: new Set(['1']) }],
])

// ---------- bootx 密码重置：开发/演示默认密码 ----------
// 明文为 121212，与 _config/sql/reset-bootx-password.sql 急救脚本共用同一哈希，改动时两处同步。
// 用项目同款 hutool（cn.hutool.crypto.digest.BCrypt#hashpw，$2a$10$ 前缀）生成并 checkpw 自验通过，
// 登录侧 AbstractPasswordLoginHandler 用 BCrypt.checkpw 校验，标准算法跨实现互通。
// 固定哈希而非每次动态生成：BCrypt 带盐每次结果不同，固定值保证 data.sql 产物确定性（可 diff、重跑不变）。
// 安全语义：data.sql 面向纯净版生产种子，默认密码可接受，但生产部署后必须立即修改（README 有标注）。
const BOOTX_DEV_PASSWORD_PLAIN = '121212'
const BOOTX_DEV_PASSWORD_HASH = '$2a$10$HiIvaX7tbGWDeRVSciX/LuIAIYUgVJwasWtstsXsakpt0d9Sw.cKG'

// ---------- 解析规则 ----------
// 兼容 pg_dump(public.tbl)与 Navicat("public"."tbl")两种引用风格
const INSERT_RE = /^INSERT INTO (?:public\.|"public"\.)?"?(\w+)"?\s*(\([^)]*\))?\s*VALUES\b/i
const STMT_END_RE = /\);\s*$/
// 需跳过的整行（历史 pg_dump 18 客户端产物：\restrict 带随机 nonce 破坏产物确定性，
// SET transaction_timeout 在 PG16 服务端报 unrecognized configuration parameter）
const SKIP_LINE_RES = [/^\\restrict/, /^\\unrestrict/, /^SET transaction_timeout\s*=/]

// ---------- 供 export-sql.mjs 复用的语句解析 ----------
/**
 * 解析一条完整 INSERT 语句(引号感知, 支持跨行字符串)。
 * @param stmt 完整语句文本(单行或含换行)
 * @returns {null|{table: string, columns: string[]|null, tokens: Array<{raw: string, start: number, end: number}>}}
 *          tokens 为 VALUES 元组的顶层逗号分段(含定位, start/end 为 stmt 内下标)
 */
export function parseInsert(stmt) {
  const m = stmt.match(INSERT_RE)
  if (!m) return null
  const table = m[1]
  let columns = null
  if (m[2]) {
    columns = m[2].slice(1, -1).split(',').map((c) => c.trim().replace(/^"|"$/g, ''))
  }
  // 定位 VALUES 之后: INSERT_RE 的 m[0] 恒以 VALUES 关键字结尾(\b 零宽),
  // 直接从其后找元组开括号即可, 无需再扫语句(引号内 VALUES 字样不会干扰)
  const vIdx = m[0].length
  // 元组分段(带定位)
  let j = stmt.indexOf('(', vIdx)
  if (j === -1) return { table, columns, tokens: [] }
  const tokens = []
  let depth = 0
  let curStart = -1
  let inStr = false
  for (; j < stmt.length; j++) {
    const ch = stmt[j]
    if (inStr) {
      if (ch === "'") {
        if (stmt[j + 1] === "'") j++
        else inStr = false
      }
      continue
    }
    if (ch === "'") {
      inStr = true
      continue
    }
    if (ch === '(') {
      depth++
      if (depth === 1) {
        curStart = j + 1
        continue
      }
    }
    if (ch === ')') {
      depth--
      if (depth === 0) {
        tokens.push(trimToken(stmt, curStart, j))
        return { table, columns, tokens }
      }
    }
    if (ch === ',' && depth === 1) {
      tokens.push(trimToken(stmt, curStart, j))
      curStart = j + 1
    }
  }
  return { table, columns, tokens }
}

/** 去两端空白取定位(先扫出非空白边界, 再记录 start/end) */
function trimToken(stmt, from, to) {
  let s = from
  let e = to
  while (s < e && /\s/.test(stmt[s])) s++
  while (e > s && /\s/.test(stmt[e - 1])) e--
  return { raw: stmt.slice(s, e), start: s, end: e }
}

/** 值 token 规整为可比对文本: 引号串去引号并还原 '' 转义, 其余原样 */
function normalizeValue(token) {
  if (token.startsWith("'") && token.endsWith("'") && token.length >= 2) {
    return token.slice(1, -1).replace(/''/g, "'")
  }
  return token
}

/** mock 行级排除判定: 命中任一规则对象(OR)即排除 */
export function isMockRow(table, columns, tokens) {
  const rules = MOCK_ROW_RULES.get(table)
  if (!rules) return false
  if (!columns || !tokens?.length) {
    throw new Error(`[排除规则] 表 ${table} 出现无列名 INSERT, 无法安全应用 mock 行级排除, 中止退出`)
  }
  for (const rule of rules) {
    let all = true // 对象内多列 AND(当前规则均为单列, 预留语义)
    for (const [col, allowed] of Object.entries(rule)) {
      const idx = columns.indexOf(col)
      if (idx === -1 || idx >= tokens.length) {
        throw new Error(`[排除规则] 表 ${table} 缺少规则列 ${col}(实际列: ${columns.join(',')})`)
      }
      if (!allowed.has(normalizeValue(tokens[idx].raw))) {
        all = false
        break
      }
    }
    if (all) return true
  }
  return false
}

/** bootx 行判定与密码/性别重置: 命中返回改写后的 stmt, 未命中返回 null */
function bootxKeepAndReset(table, stmt, columns, tokens, report) {
  const idIdx = columns.indexOf('id')
  if (idIdx === -1 || idIdx >= tokens.length) {
    report.fail(`bootx 行判定失败: ${table} 缺 id 列(实际列: ${columns.join(',')})`)
    return null
  }
  if (normalizeValue(tokens[idIdx].raw) !== '1') return null
  if (table === 'iam_user_info') {
    const pwIdx = columns.indexOf('password')
    if (pwIdx === -1 || pwIdx >= tokens.length) {
      report.fail('bootx 密码列替换失败: iam_user_info 缺 password 列')
      return null
    }
    const tk = tokens[pwIdx]
    if (!tk.raw.startsWith("'") || !tk.raw.endsWith("'")) {
      report.fail('bootx 密码列替换失败: password 值不是引号字面量, 无法安全替换')
      return null
    }
    report.passwordResetCount++
    return stmt.slice(0, tk.start) + `'${BOOTX_DEV_PASSWORD_HASH}'` + stmt.slice(tk.end)
  }
  if (table === 'iam_user_expand_info') {
    return resetBootxSex(table, stmt, columns, tokens, report)
  }
  return stmt
}

/**
 * bootx 扩展行 sex 置空: dev 源头 sex 常被登录测试改为 'female' 等,
 * 纯净版口径拍板清 NULL(与密码重置同理防漂移); 失败仅统计标记不阻断(非性敏数据)。
 */
function resetBootxSex(table, stmt, columns, tokens, report) {
  const sexIdx = columns.indexOf('sex')
  if (sexIdx === -1 || sexIdx >= tokens.length) {
    report.sexSoftFail(`bootx sex 置空失败: ${table} 缺 sex 列(实际列: ${columns.join(',')})`)
    return stmt
  }
  const tk = tokens[sexIdx]
  if (tk.raw === 'NULL') {
    report.sexResetCount++ // 幂等: 库中已是 NULL 同样计成功
    return stmt
  }
  if (!tk.raw.startsWith("'") || !tk.raw.endsWith("'")) {
    report.sexSoftFail('bootx sex 置空失败: sex 值不是引号字面量也不是 NULL: ' + tk.raw.slice(0, 40))
    return stmt
  }
  report.sexResetCount++
  return stmt.slice(0, tk.start) + 'NULL' + stmt.slice(tk.end)
}

// ---------- CLI 入口(被 import 复用时跳过) ----------
const isMain = argv[1] && import.meta.url === pathToFileURL(argv[1]).href
if (isMain) {
  main()
}

function main() {
  const positional = argv.slice(2).filter((a) => !a.startsWith('--'))
  const flag = (name) => {
    const i = argv.indexOf(name)
    return i === -1 ? undefined : argv[i + 1]
  }
  const bypassDir = flag('--bypass-dir')
  if (positional.length < 2) {
    console.error('用法：node redact-data.mjs <input.sql> <output.sql> [--bypass-dir <目录>]')
    exit(1)
  }
  const [inputPath, outputPath] = positional

  // ---------- 统计 ----------
  const stats = new Map() // table -> { total, kept, mockExcluded }
  let passwordResetFailed = false // bootx 密码列替换失败标记（失败时非零退出，防真实密码哈希泄露）
  const missingLinkRows = []      // BOOTX_LINK_RULES 表一行都没命中（有则非零退出）
  let sexResetFailed = false      // bootx 扩展行 sex 置空失败标记（软失败: 仅报告标记, 不阻断）
  const report = {
    passwordResetCount: 0,
    sexResetCount: 0,
    fail(msg) {
      console.error(`[错误] ${msg}`)
      passwordResetFailed = true
    },
    sexSoftFail(msg) {
      console.warn(`[警告] ${msg}`)
      sexResetFailed = true
    },
  }
  const bypassLines = []          // 旁路输出的 mock 元数据行
  function bump(table, kept, mockExcluded = false) {
    const s = stats.get(table) ?? { total: 0, kept: 0, mockExcluded: 0 }
    s.total += 1
    if (kept) s.kept += 1
    if (mockExcluded) s.mockExcluded += 1
    stats.set(table, s)
  }

  const input = createReadStream(inputPath, { encoding: 'utf8' })
  const output = createWriteStream(outputPath, { encoding: 'utf8' })

  output.write(`-- ============================================================\n`)
  output.write(`-- 纯净版生产种子(已脱敏) | 工具 build/redact-data.mjs | 时间 ${new Date().toISOString()}\n`)
  output.write(`-- 策略：保留 ${KEEP_TABLES.size} 张系统种子表(排除规则见脚本 MOCK_ROW_RULES) + bootx 超管(id=1)及其登录链路关联行，\n`)
  output.write(`--       其余整表清除(菜单种子在 export-sql.mjs 的 [3] 段); mock 演示元数据已行级排除(旁路给 data-demo.sql)\n`)
  output.write(`-- bootx 超管密码已重置为开发默认密码(121212)，生产部署后必须立即修改\n`)
  output.write(`-- ============================================================\n\n`)

  const rl = createInterface({ input, crlfDelay: Infinity })
  const redactedMarked = new Set() // 每张被清除的表仅写一次审计注释
  let inInsert = false // 是否在累积跨行 INSERT
  let buf = []         // 跨行 INSERT 累积缓冲
  let bufTable = ''    // 当前累积的表名
  let fatal = ''       // 致命错误(菜单表误入/无列名 INSERT/规则解析失败)

  function writeRedactedMark(table) {
    if (redactedMarked.has(table)) return
    output.write(`-- REDACTED: ${table} (整表清除)\n`)
    redactedMarked.add(table)
  }

  /** 决策一条完整 INSERT（lines 为单行或跨行合并后的行数组） */
  function handleInsert(table, lines) {
    const stmt = lines.join('\n')
    // ⓪ 无列名 INSERT 一律硬失败: 本脚本所有行级规则都按列名定位, 开源版列序与位置锚定假设不一致,
    //    静默按位置取值会产出错位数据(比导出失败严重得多), 故不提供回退路径
    const parsed = parseInsert(stmt)
    if (!parsed) {
      fatal = `无法解析的 INSERT(表 ${table}), 请检查 dump 格式`
      return
    }
    if (!parsed.columns || !parsed.tokens.length) {
      fatal = `表 ${table} 出现无列名 INSERT, 本脚本只接受 pg_dump --column-inserts 格式, 中止退出`
      return
    }
    const { columns, tokens } = parsed
    // ④ 菜单两表硬守卫(另有链路, 误传入即管线接错)
    if (MENU_TABLES.has(table)) {
      fatal = `表 ${table} 不经本工具(菜单种子另有链路), 请检查上游管线`
      return
    }
    // ① 系统种子表：先丢弃逻辑删除行(dev 库测试残留), 再过 mock 行级排除, 最后保留
    if (KEEP_TABLES.has(table)) {
      // 逻辑删除行不属装机种子: 纯净版/demo 版都不带、也不旁路(dev 库历史测试数据会随整表导出, 如 system_dict code='cs')
      const deletedIdx = columns.indexOf('deleted')
      if (deletedIdx !== -1 && tokens[deletedIdx] && String(tokens[deletedIdx].raw).toLowerCase() === 'true') {
        bump(table, false)
        return
      }
      let mockRow = false
      if (MOCK_ROW_RULES.has(table)) {
        mockRow = isMockRow(table, columns, tokens)
        if (mockRow) bypassLines.push(stmt.endsWith(';') ? stmt : stmt + ';')
      }
      if (mockRow) {
        bump(table, false, true)
        return
      }
      bump(table, true)
      for (const ln of lines) output.write(ln + '\n')
      return
    }
    // ② 内置超管关联表行级保留：bootx (id=1), iam_user_info 密码列重置
    if (BOOTX_ADMIN_TABLES.has(table)) {
      const rewritten = bootxKeepAndReset(table, stmt, columns, tokens, report)
      if (rewritten !== null) {
        bump(table, true)
        output.write(rewritten + '\n')
      } else {
        bump(table, false)
        writeRedactedMark(table)
      }
      return
    }
    // ③ bootx 登录链路关联行：只留规则列命中白名单值的行
    const linkRule = BOOTX_LINK_RULES.get(table)
    if (linkRule) {
      const idx = columns.indexOf(linkRule.col)
      if (idx === -1 || idx >= tokens.length) {
        fatal = `[bootx 关联行] 表 ${table} 缺少规则列 ${linkRule.col}(实际列: ${columns.join(',')})`
        return
      }
      if (linkRule.values.has(normalizeValue(tokens[idx].raw))) {
        bump(table, true)
        for (const ln of lines) output.write(ln + '\n')
      } else {
        bump(table, false)
        writeRedactedMark(table)
      }
      return
    }
    // ⑤ 其余：整表清除
    bump(table, false)
    writeRedactedMark(table)
  }

  rl.on('line', (line) => {
    if (fatal || passwordResetFailed) return
    if (inInsert) {
      // 正在累积跨行 INSERT
      buf.push(line)
      if (STMT_END_RE.test(line)) {
        handleInsert(bufTable, buf)
        inInsert = false
        buf = []
        bufTable = ''
      }
      return
    }
    const m = line.match(INSERT_RE)
    if (m) {
      const table = m[1]
      if (STMT_END_RE.test(line)) {
        handleInsert(table, [line]) // 单行 INSERT
      } else {
        inInsert = true // 跨行 INSERT 起始
        bufTable = table
        buf = [line]
      }
      return
    }
    // 非 INSERT 行（注释、空行等）跳过不兼容元命令后原样输出
    if (SKIP_LINE_RES.some((re) => re.test(line))) return
    output.write(line + '\n')
  })

  rl.on('close', () => {
    // 兜底：文件结束时若仍在累积（理论上不会发生）
    if (inInsert && buf.length) handleInsert(bufTable, buf)
    output.end(() => {
      // bootx 链路关联行一行未命中 = 种子登录不进系统, 与密码替换失败同等级硬失败
      for (const [t] of BOOTX_LINK_RULES) {
        if ((stats.get(t)?.kept ?? 0) === 0) missingLinkRows.push(t)
      }
      if (bypassDir && bypassLines.length) {
        mkdirSync(bypassDir, { recursive: true })
        const bypassPath = isAbsolute(bypassDir) ? join(bypassDir, 'bypass-mock-metadata.sql') : join(cwd(), bypassDir, 'bypass-mock-metadata.sql')
        writeFileSync(bypassPath, bypassHeader() + bypassLines.join('\n') + '\n')
        console.log(`[旁路] mock 元数据 ${bypassLines.length} 行已写入 ${bypassPath}(供 data-demo.sql [4.2] 段复用)`)
      } else if (bypassLines.length) {
        console.warn('[警告] 排除了 mock 元数据但未指定 --bypass-dir, 旁路明细未落盘(仅统计)')
      }
      printReport()
      // 密码替换失败/无列名 INSERT/菜单表误入/bootx 关联行缺失时以非零码退出，提示产物不可用
      if (passwordResetFailed || fatal || missingLinkRows.length) {
        if (fatal) console.error(`[错误] ${fatal}`)
        for (const t of missingLinkRows) console.error(`[错误] bootx 关联行缺失: ${t} 未命中任何行, 种子不可用`)
        exit(1)
      }
    })
  })

  function bypassHeader() {
    return `-- ============================================================\n-- 旁路输出: 纯净版排除的 mock 演示元数据 | 工具 build/redact-data.mjs | 时间 ${new Date().toISOString()}\n-- 用途: 供 data-demo.sql [4.2] 段复用, 防纯净版/演示版两线漂移; 禁止导入生产\n-- ============================================================\n\n`
  }

  function printReport() {
    const keptSeedTables = []
    const partialTables = [] // 行级保留表（内置超管关联表）
    const linkTables = []    // bootx 登录链路关联行
    const clearedTables = []
    for (const [t, s] of stats) {
      if (KEEP_TABLES.has(t)) keptSeedTables.push([t, s])
      else if (BOOTX_ADMIN_TABLES.has(t)) partialTables.push([t, s])
      else if (BOOTX_LINK_RULES.has(t)) linkTables.push([t, s])
      else clearedTables.push([t, s])
    }

    const sumKept = keptSeedTables.reduce((a, [, s]) => a + s.kept, 0)
    const sumMock = keptSeedTables.reduce((a, [, s]) => a + s.mockExcluded, 0)
    const sumCleared = clearedTables.reduce((a, [, s]) => a + s.total, 0)
    const sumPartialCleared = partialTables.reduce((a, [, s]) => a + (s.total - s.kept), 0)

    const lines = []
    lines.push('')
    lines.push('========== 脱敏统计（白名单 + mock 行级排除 + bootx 行级保留） ==========')
    lines.push(`[保留·种子表] ${keptSeedTables.length} 张表，INSERT ${sumKept} 条（另排除 mock 行 ${sumMock} 条）`)
    const partialDetail = partialTables.map(([t, s]) => `${t}(${s.total}→${s.kept})`).join(', ') || '无'
    lines.push(`[保留·行级] 内置超管(id=1)关联表：${partialDetail}，清除 ${sumPartialCleared}`)
    const linkDetail = linkTables.map(([t, s]) => `${t}(${s.total}→${s.kept})`).join(', ') || '无'
    lines.push(`[保留·行级] bootx 登录链路：${linkDetail}`)
    lines.push(`[重置·密码] bootx 密码 → 开发默认密码 ${BOOTX_DEV_PASSWORD_PLAIN}（${report.passwordResetCount} 处）`)
    lines.push(`[重置·性别] bootx 扩展行 sex → NULL（${report.sexResetCount} 处${sexResetFailed ? ', 有失败标记见警告' : ''}）`)
    lines.push(`[清除] ${clearedTables.length} 张表，丢弃 INSERT ${sumCleared} 条`)
    const mockDetail = keptSeedTables.filter(([, s]) => s.mockExcluded > 0).map(([t, s]) => `${t}(-${s.mockExcluded})`).join(', ')
    if (mockDetail) lines.push(`[排除·mock] ${mockDetail}（旁路输出见 --bypass-dir）`)
    lines.push('---- 清除明细 ----')
    for (const [t, s] of clearedTables.sort((a, b) => a[0].localeCompare(b[0]))) {
      lines.push(`  ${t.padEnd(38)} ${s.total}`)
    }
    lines.push('=============================================')
    console.log(lines.join('\n'))
  }
}
