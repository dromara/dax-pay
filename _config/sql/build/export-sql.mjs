#!/usr/bin/env node
/**
 * export-sql.mjs — SQL 装机产物生成器（唯一入口）
 *
 * 产物（默认落 _config/sql/，--out-dir 可改；分文件，装机先 table 再 data）：
 *   table.sql        表结构（pg_dump --schema-only --schema=public 洁净后的产物），纯净/演示共用
 *   data.sql         纯净版数据 = [2] 纯净种子 + [3] 菜单种子（不含表结构）
 *   data-demo.sql    演示版数据 = [2] + [3] + [4] 演示数据段（不含表结构）
 *
 * 状态（自 DaxPay Plus 商业版 _config/sql/build/ 移植，2026-11-02 / 2026-11-03）：
 *   阶段 A：table.sql + data.sql 全链路（含空库验证）已落地；
 *   阶段 B：data-demo.sql 演示段（旁路菜单/mock 元数据 + 剧本生成 demo-dataset + 装机期断言）已落地，
 *          默认 profile 为 all（三件全出）。
 *
 * 数据源：daxpay-start/src/main/resources/application-dev.yml 的 spring.datasource
 *         （主机名经本机 hosts 解析；本机无 psql 客户端，pg_dump/psql 均走 WSL docker postgres:16）
 *
 * 用法：
 *   node build/export-sql.mjs                     # 默认 --profile all：出 table.sql + data.sql + data-demo.sql
 *   node build/export-sql.mjs --profile pure      # 只出 table.sql + data.sql
 *   node build/export-sql.mjs --profile demo      # 只出 table.sql + data-demo.sql（table 两版共用）
 *   node build/export-sql.mjs --out-dir /tmp/x    # 试跑到临时目录，不动仓内产物
 *
 * 落盘策略：先写 <outDir>/.tmp-export，全部断言通过后 rename 覆盖；断言失败一律不落盘、原产物不动。
 */
import { spawnSync } from 'node:child_process'
import { existsSync, mkdirSync, readFileSync, renameSync, rmSync, writeFileSync } from 'node:fs'
import { dirname, isAbsolute, join, resolve } from 'node:path'
import { fileURLToPath } from 'node:url'
import { argv, exit, platform, cwd } from 'node:process'

import { KEEP_TABLES, BOOTX_ADMIN_TABLES, BOOTX_LINK_RULES, MOCK_ROW_RULES, parseInsert, isMockRow } from './redact-data.mjs'
import { createGenerator } from './demo-gen.mjs'
import {
  ANCHORS, buildIdentity, buildChannels, buildTerminals, buildGateway, buildTransactions,
  buildGatewayOrders, buildAbnormalOrders, buildNotices, buildSecurity, buildTransfers, buildP2Extras,
  assertConsistency, buildAssertBlock,
} from './demo-dataset.mjs'

const here = dirname(fileURLToPath(import.meta.url))
const sqlDir = resolve(here, '..')                    // _config/sql
const repoDir = resolve(sqlDir, '..', '..')           // dax-pay-open 仓根
const devYml = join(repoDir, 'daxpay-start', 'src', 'main', 'resources', 'application-dev.yml')

// 阶段 A 的硬口径（dev 库 2026-11-02 实测 170 张表 / 2506 条列注释;
// 2026-11-06 异步通知配置下线删 mch_app_notify_config 后为 169 张;
// 2026-11-09 mock 去多服务商删 mock_isv_credential 后为 168 张）
const EXPECT_TABLE_COUNT = 168
const MIN_COLUMN_COMMENTS = 2400

// 演示菜单 id（Demos 6 条 {2,202,204,205,206,208}）：
// 纯净版排除，演示版由旁路注入（[4.1] 段）。
// 4010130~4040132 是真实商户分组菜单（menu.payment.merchant.group.*），不是演示菜单，禁止加入本集合。
const DEMO_MENU_IDS = new Set(['2', '202', '204', '205', '206', '208'])

// 菜单两表之外的硬守卫: 这些表出现在纯净种子即断言失败（凭证/演示数据走演示段, 菜单走菜单段）
const FORBIDDEN_IN_DATA = new Set([
  'iam_perm_menu', 'iam_role_menu',
  'mch_credential',
  // 开源版 mock 通道凭证/服务商档案（plus 版为单张凭证表, 开源版拆成多张）
  'mock_gateway_credential', 'mock_pay_credential',
  'mock_isv_info', 'mock_isv_channel_merchant', 'mock_alloc_receiver',
])

// 纯净版禁用锚点（dev 库演示数据的业务编号, 出现即说明纯净版混入演示行）
// 2026-11-02 dev 库核实: 示例商户 M1784445131420 / 默认应用 A6925929877811827 / 默认门店 S9358855090931035
const PURE_FORBIDDEN_ANCHORS = [
  'M1784445131420', 'M1789016549186',
  'A6925929877811827', 'A3412914238250850', 'A6780944142000590',
  'S9358855090931035', 'S9336863516508377',
  // 历史演示运营账号 csadmin 保留为回灌守卫；演示运营账号已统一为 admin，
  // 不能把裸 'admin' 加进来（纯种子里 client_code='admin' 到处都是，会误报）
  'csadmin', 'csqysh', 'csxwsh',
]

// plus 独有表（开源版库中不存在；演示段出现即说明剧本从商业版直接复制未裁剪）
const PLUS_ONLY_TABLES = [
  'agent_info', 'agent_user',
  'pay_onb_mch_apply', 'pay_onb_mch_apply_history',
  'pay_rate_config', 'mch_limit_rule', 'mch_risk_config',
  'pay_trade_rate_snapshot', 'pay_trade_profit_detail',
]
// 演示版禁用锚点 = dev 库示例商户编号（演示数据全部剧本生成，不应带出 dev 业务编号）
const DEMO_STALE_ANCHORS = [
  'M1784445131420', 'M1789016549186',
  'A6925929877811827', 'A3412914238250850', 'A6780944142000590',
  'S9358855090931035', 'S9336863516508377',
]

// 分页/骨架分隔线长度统一
const RULE = '-- ' + '='.repeat(76)

// ---------- 参数 ----------
function pick(flag) {
  const i = argv.indexOf(flag)
  return i === -1 ? undefined : argv[i + 1]
}
// 阶段 B 已落地：默认三件全出（all）；pure/demo 用于单端调试
const profile = pick('--profile') ?? 'all'            // all | pure | demo
const outDirArg = pick('--out-dir')
const outDir = outDirArg ? (isAbsolute(outDirArg) ? outDirArg : resolve(cwd(), outDirArg)) : sqlDir
if (!['all', 'pure', 'demo'].includes(profile)) {
  console.error(`[错误] --profile 只支持 all|pure|demo（当前 ${profile}）`)
  exit(1)
}
const wantPure = profile === 'all' || profile === 'pure'
const wantDemo = profile === 'all' || profile === 'demo'
const tmpDir = join(outDir, '.tmp-export')
mkdirSync(outDir, { recursive: true })
if (existsSync(tmpDir)) rmSync(tmpDir, { recursive: true, force: true })
mkdirSync(tmpDir, { recursive: true })

// ---------- 数据源 ----------
function readDatasource() {
  const yml = readFileSync(devYml, 'utf8')
  const dsBlock = yml.split('datasource:')[1]?.split(/^\S/m)[0] ?? ''
  const url = dsBlock.match(/url:\s*(\S+)/)?.[1]
  const username = dsBlock.match(/username:\s*(\S+)/)?.[1]
  const password = dsBlock.match(/password:\s*(\S+)/)?.[1]
  if (!url || !username || !password) {
    console.error(`[错误] 未能从 ${devYml} 解析 datasource url/username/password`)
    exit(1)
  }
  const m = url.match(/jdbc:postgresql:\/\/([^:/?]+):(\d+)\/([^?]+)/)
  if (!m) {
    console.error(`[错误] 无法解析 JDBC URL: ${url}`)
    exit(1)
  }
  return { hostRaw: m[1], port: m[2], db: m[3], username, password }
}

/** 主机名解析: IP/localhost 原样, 其余查本机 hosts 文件(Windows/Linux 兼容), 查不到原样透出 */
function resolveHost(hostRaw) {
  if (/^[\d.]+$/.test(hostRaw) || hostRaw === 'localhost') return { host: hostRaw, via: 'raw' }
  const hostsFiles = platform === 'win32'
    ? ['C:/Windows/System32/drivers/etc/hosts', '/c/Windows/System32/drivers/etc/hosts']
    : ['/etc/hosts']
  for (const f of hostsFiles) {
    if (!existsSync(f)) continue
    for (const line of readFileSync(f, 'utf8').split('\n')) {
      const m = line.trim().match(/^([\d.]+)\s+(\S+)\s*$/)
      if (m && m[2] === hostRaw) return { host: m[1], via: `hosts(${f})` }
    }
  }
  return { host: hostRaw, via: 'unresolved(原样透出)' }
}

const ds = readDatasource()
const { host, via } = resolveHost(ds.hostRaw)
const connStr = `postgresql://${ds.username}:${ds.password}@${host}:${ds.port}/${ds.db}`
console.log(`[数据源] ${ds.hostRaw} --${via}--> ${host}:${ds.port}/${ds.db}`)

// ---------- 数据库命令执行封装 ----------
/**
 * 本机 PostgreSQL 客户端探测：PATH 优先，再查 Windows 常见安装目录；未命中返回 null。
 * 命中时直接执行本机二进制，避免依赖 WSL docker（部分环境禁用 wsl.exe）。
 */
function resolveNativeClient(cmd) {
  const exe = platform === 'win32' ? `${cmd}.exe` : cmd
  for (const dir of (process.env.PATH || '').split(platform === 'win32' ? ';' : ':')) {
    if (!dir) continue
    const candidate = join(dir, exe)
    if (existsSync(candidate)) return candidate
  }
  if (platform === 'win32') {
    for (const version of ['18', '17', '16', '15', '14']) {
      const candidate = `C:/Program Files/PostgreSQL/${version}/bin/${exe}`
      if (existsSync(candidate)) return candidate
    }
  }
  return null
}

/**
 * 执行 pg_dump / psql：优先本机客户端直连，未安装时回落 WSL docker postgres:16。
 * 两条路径的参数语义一致（args 中 pg_dump/psql 之后的参数原样透传）。
 */
function runDocker(label, args, { quietStderr = false } = {}) {
  const nativeIdx = args[0] === 'docker' ? args.findIndex((a) => a === 'pg_dump' || a === 'psql') : -1
  const nativeBin = nativeIdx > 0 ? resolveNativeClient(args[nativeIdx]) : null
  const r = nativeBin
    ? spawnSync(nativeBin, args.slice(nativeIdx + 1), { encoding: 'utf8', maxBuffer: 256 * 1024 * 1024 })
    : spawnSync('wsl', args, { encoding: 'utf8', maxBuffer: 256 * 1024 * 1024 })
  if (r.stderr && r.stderr.trim() && !quietStderr) {
    // WSL 的 localhost 代理提示(UTF-16 中文)属环境噪音, 只回显真正的错误行
    const lines = r.stderr.trim().split(/\r?\n/).filter((l) => /error|fatal|failed|错误/i.test(l))
    if (lines.length) console.warn(`[${label} stderr] ${lines.join('\n').slice(0, 1000)}`)
  }
  if (r.status !== 0) {
    console.error(`[错误] ${label} 失败 exit=${r.status}`)
    console.error((r.stderr || '').slice(0, 2000))
    exit(1)
  }
  // Windows 版本机 pg_dump/psql 输出 CRLF，统一收敛为 LF：否则行尾 \r 会让
  // 后续按行匹配的净化/断言（如 ^CREATE SCHEMA public;$）全部失效
  return r.stdout.replace(/\r\n/g, '\n')
}

function pgDumpSchemaOnly() {
  const args = ['docker', 'run', '--rm', 'postgres:16', 'pg_dump', connStr,
    '--schema-only', '--schema=public', '--no-owner', '--no-privileges']
  console.log('[pg_dump] 表结构 (public schema, --schema-only)')
  return runDocker('pg_dump(schema)', args)
}

function pgDumpTables(tables, label) {
  const args = ['docker', 'run', '--rm', 'postgres:16', 'pg_dump', connStr,
    '--data-only', '--column-inserts', '--no-owner', '--no-privileges',
    ...tables.map((t) => `--table=public.${t}`)]
  console.log(`[pg_dump] ${label}: ${tables.length} 张表`)
  return runDocker('pg_dump(data)', args)
}

function queryScalar(sql, label) {
  const args = ['docker', 'run', '--rm', 'postgres:16', 'psql', connStr, '-t', '-A', '-c', sql]
  return runDocker(label, args, { quietStderr: true }).trim()
}

function queryLines(sql, label) {
  return queryScalar(sql, label).split('\n').map((s) => s.trim()).filter(Boolean)
}

// ---------- 文本处理 ----------
/**
 * 表结构洁净化：
 *  - 剥 pg_dump 头部元信息（-- PostgreSQL database dump / -- Dumped from|by）
 *  - 去 \restrict / \unrestrict（pg_dump 16.15+ 新增 psql 元命令，旧版 psql 不认会直接报错）
 *  - 去 SET transaction_timeout（PG17+ 才有，服务端 PG16 会报 unrecognized configuration parameter）
 *  - 去 CREATE SCHEMA public;（新库 public 已存在，执行即 "schema already exists" 报错）
 * 保留 SET 语句（statement_timeout 等，无害）与 -- Name: xxx; Type: TABLE 注释（供 diff 定位）。
 */
function cleanSchema(raw) {
  const out = []
  for (const ln of raw.split('\n')) {
    if (/^\\restrict /.test(ln) || /^\\unrestrict /.test(ln)) continue
    if (/^SET transaction_timeout\s*=/.test(ln)) continue
    if (/^CREATE SCHEMA public;$/.test(ln)) continue
    if (/^-- PostgreSQL database dump$/.test(ln)) continue
    if (/^-- Dumped (from|by) /.test(ln)) continue
    out.push(ln)
  }
  return out.join('\n').replace(/\n{3,}/g, '\n\n').trim() + '\n'
}

/** 剥脱敏脚本固定 6 行头（第二个 -- ==== 分隔线之后即为正文），并清掉审计噪音行 */
function stripRedactArtifacts(text) {
  const lines = text.split('\n')
  let seps = 0
  let start = 0
  for (let i = 0; i < lines.length; i++) {
    if (/^-- ={10,}/.test(lines[i])) {
      seps++
      if (seps === 2) { start = i + 1; break }
    }
  }
  return lines.slice(start)
    .filter((ln) => !/^-- REDACTED: /.test(ln))
    .filter((ln) => !/^-- ROW-EXCLUDED: /.test(ln))
    // pg_dump 16.15+ 的 psql 元命令带随机 nonce：既破坏产物确定性，旧版 psql 也不认，必须剥掉
    .filter((ln) => !/^\\restrict /.test(ln) && !/^\\unrestrict /.test(ln))
    .filter((ln) => !/^SET transaction_timeout\s*=/.test(ln))
    .join('\n').replace(/\n{3,}/g, '\n\n').trim() + '\n'
}

/** 从 pg_dump 输出中提取完整 INSERT 语句数组(剥离 SET/注释/preamble; 跨行语句聚合为整条) */
function extractInsertStmts(dump) {
  const lines = dump.split('\n')
  const stmts = []
  let cur = null
  for (const ln of lines) {
    if (cur !== null) {
      cur.push(ln)
      if (/\);;*\s*$/.test(ln)) {
        stmts.push(cur.join('\n').trim())
        cur = null
      }
      continue
    }
    if (/^INSERT INTO /i.test(ln)) {
      if (/\);;*\s*$/.test(ln)) stmts.push(ln.trim())
      else cur = [ln]
    }
  }
  return stmts
}

// ---------- [1] 表结构段 ----------
function buildSchemaSection() {
  const raw = pgDumpSchemaOnly()
  const clean = cleanSchema(raw)
  const createdTables = [...clean.matchAll(/^CREATE TABLE (?:IF NOT EXISTS )?public\.(\w+)/gm)].map((m) => m[1])
  const tableCount = createdTables.length
  const commentTables = new Set([...clean.matchAll(/^COMMENT ON TABLE public\.(\w+)/gm)].map((m) => m[1]))
  const commentColumns = (clean.match(/^COMMENT ON COLUMN /gm) ?? []).length
  const dbTables = queryLines("select tablename from pg_tables where schemaname='public' order by 1", 'psql(表集)')
  const dbTableCount = dbTables.length
  const seqCount = (clean.match(/^CREATE SEQUENCE /gm) ?? []).length

  const assertFails = []
  if (tableCount !== EXPECT_TABLE_COUNT) assertFails.push(`建表数 ${tableCount} != 预期 ${EXPECT_TABLE_COUNT}`)
  if (tableCount !== dbTableCount) assertFails.push(`建表数 ${tableCount} != 库表数 ${dbTableCount}`)
  // 双向差集: 漏表(导出丢表)与多表(库里有、脚本没有)都要拦
  const createdSet = new Set(createdTables)
  const dbSet = new Set(dbTables)
  const missing = [...dbSet].filter((t) => !createdSet.has(t))
  const extra = [...createdSet].filter((t) => !dbSet.has(t))
  if (missing.length) assertFails.push(`导出漏表(库里有、脚本无): ${missing.join(', ')}`)
  if (extra.length) assertFails.push(`导出多表(脚本有、库里无): ${extra.join(', ')}`)
  const missingComment = createdTables.filter((t) => !commentTables.has(t))
  if (missingComment.length) assertFails.push(`缺表注释: ${missingComment.join(', ')}`)
  if (commentTables.size !== tableCount) assertFails.push(`表注释 ${commentTables.size} != 建表数 ${tableCount}`)
  if (commentColumns < MIN_COLUMN_COMMENTS) assertFails.push(`列注释 ${commentColumns} 低于下限 ${MIN_COLUMN_COMMENTS}`)
  if (seqCount !== 0) assertFails.push(`表结构含 ${seqCount} 条 CREATE SEQUENCE（开源版主键全为雪花 ASSIGN_ID, 不应有序列）`)
  if (/^\\restrict /m.test(clean) || /^\\unrestrict /m.test(clean)) assertFails.push('表结构残留 \\restrict / \\unrestrict 元命令')
  if (/^SET transaction_timeout\s*=/m.test(clean)) assertFails.push('表结构残留 SET transaction_timeout(PG16 不认)')
  if (/^CREATE SCHEMA public;$/m.test(clean)) assertFails.push('表结构残留 CREATE SCHEMA public')

  return { body: clean, tableCount, dbTableCount, commentColumns, commentTables: commentTables.size, seqCount, assertFails }
}

// ---------- [2] 纯净种子段 ----------
function buildSeedSection() {
  // bootx 登录链路关联行(iam_user_role / iam_user_password_security)必须一起导出，由 redact 行级过滤
  const tables = [...KEEP_TABLES, ...BOOTX_ADMIN_TABLES, ...BOOTX_LINK_RULES.keys()]
  const rawPath = join(tmpDir, 'seed-raw.sql')
  const outPath = join(tmpDir, 'seed-redacted.sql')
  writeFileSync(rawPath, pgDumpTables(tables, '纯净种子白名单'))

  // 走 CLI 子进程（与独立用法同一条代码路径），脱敏/密码重置/mock 行排除全在其内
  const r = spawnSync(process.execPath, [join(here, 'redact-data.mjs'), rawPath, outPath, '--bypass-dir', tmpDir], {
    encoding: 'utf8', maxBuffer: 64 * 1024 * 1024,
  })
  console.log(r.stdout?.trim() ?? '')
  if (r.status !== 0) {
    console.error(r.stderr ?? '')
    console.error('[错误] redact-data.mjs 失败，纯净种子不可用')
    exit(1)
  }
  // 旁路产物 = 纯净版排除的 mock 元数据行（带列名 INSERT），演示段 [4.2] 直接复用：
  // 一份规则两个方向——纯净版排除的行正是演示版要的行，且列名随表结构自动跟随，永不漂移。
  const bypassPath = join(tmpDir, 'bypass-mock-metadata.sql')
  const bypassRaw = existsSync(bypassPath) ? readFileSync(bypassPath, 'utf8') : ''
  const bypassStmts = extractInsertStmts(bypassRaw)
  return {
    body: stripRedactArtifacts(readFileSync(outPath, 'utf8')),
    bypass: bypassStmts.join('\n') + '\n',
    bypassCount: bypassStmts.length,
  }
}

// ---------- [3] 菜单种子段 ----------
function buildMenuSection() {
  const dump = pgDumpTables(['iam_perm_menu', 'iam_role_menu'], '菜单两表')
  const keptMenus = []
  const keptRoleMenus = []
  const bypassMenus = []
  for (const stmt of extractInsertStmts(dump)) {
    const parsed = parseInsert(stmt)
    if (!parsed) {
      console.error(`[检查不通过] 菜单导出含无法解析的语句: ${stmt.slice(0, 120)}`)
      exit(1)
    }
    const { table, columns, tokens } = parsed
    // 菜单两表必须带列名: 开源版列序(iam_role_menu 为 id/role_id/menu_id)与历史位置假设不同,
    // 按位置取值会静默把 role_id 当 menu_id, 直接硬失败
    if (!columns || !tokens.length) {
      console.error(`[检查不通过] 菜单导出出现无列名 INSERT(表 ${table})，无法安全按列定位`)
      console.error(`  语句: ${stmt.slice(0, 160)}`)
      exit(1)
    }
    if (table === 'iam_perm_menu') {
      const idIdx = columns.indexOf('id')
      if (idIdx === -1 || idIdx >= tokens.length) {
        console.error(`[检查不通过] iam_perm_menu 缺 id 列: ${columns.join(',')}`)
        exit(1)
      }
      const id = tokens[idIdx].raw
      if (DEMO_MENU_IDS.has(id)) bypassMenus.push(stmt)
      else keptMenus.push({ id, stmt })
    } else if (table === 'iam_role_menu') {
      const midIdx = columns.indexOf('menu_id')
      const ridIdx = columns.indexOf('role_id')
      if (midIdx === -1 || ridIdx === -1 || midIdx >= tokens.length || ridIdx >= tokens.length) {
        console.error(`[检查不通过] iam_role_menu 缺 menu_id/role_id 列: ${columns.join(',')}`)
        exit(1)
      }
      const menuId = tokens[midIdx].raw
      const roleId = tokens[ridIdx].raw
      if (DEMO_MENU_IDS.has(menuId)) bypassMenus.push(stmt)
      else keptRoleMenus.push({ roleId, menuId, stmt })
    } else {
      console.error(`[检查不通过] 菜单导出混入非菜单表: ${table}`)
      exit(1)
    }
  }
  keptMenus.sort((a, b) => Number(a.id) - Number(b.id))
  keptRoleMenus.sort((a, b) => Number(a.roleId) - Number(b.roleId) || Number(a.menuId) - Number(b.menuId))

  const body = `DELETE FROM public.iam_role_menu;\nDELETE FROM public.iam_perm_menu;\n\n`
    + '-- ----------------------------\n-- 菜单（权限树）\n-- ----------------------------\n'
    + keptMenus.map((x) => x.stmt).join('\n') + '\n\n'
    + '-- ----------------------------\n-- 内置角色菜单绑定（全量；纯净种子不承担角色绑定）\n-- ----------------------------\n'
    + keptRoleMenus.map((x) => x.stmt).join('\n') + '\n'
  // 被排除的演示菜单旁路给演示段复用（与 mock 元数据同一机制：纯净版排除的正是演示版要的）
  return {
    body,
    menuCount: keptMenus.length,
    roleMenuCount: keptRoleMenus.length,
    bypass: bypassMenus.join('\n'),
    bypassCount: bypassMenus.length,
  }
}

// ---------- [4] 演示数据段 ----------
// 旁路导出（菜单 / mock 元数据）+ 剧本生成（身份配置 / 通道 / 交易）+ 装机期断言。
// 剧本由 demo-dataset.mjs 声明、demo-gen.mjs 按 table.sql 列契约编译。
// 开源版表集裁剪：无 agent_* / pay_onb_* / pay_rate_config / mch_limit_rule / mch_risk_config /
// pay_trade_rate_snapshot / pay_trade_profit_detail，引用规则只保留 open 真实存在的表。
function buildDemoSection() {
  const gen = createGenerator(schemaSection.body, {
    track: new Set([
      'mch_info.mch_no', 'iam_user_info.id', 'mch_app_info.app_id',
      'mch_store_info.store_no',
      'device_terminal.terminal_no', 'device_channel_terminal.id', 'device_channel_terminal.out_terminal_no',
      'device_qr_code.code',
      'pay_route_strategy.id', 'pay_normal_order.id',
      'pay_trade.id', 'pay_trade.trade_no', 'mch_notice_task.id',
      'pay_gateway_pay_config.id', 'pay_gateway_order.id',
      // 通知全家桶 / 安全域（2026-11-10）：公告被已读记录回指、黑名单被风险命中回指
      'notify_notice.id', 'pay_blacklist.id',
    ]),
    refs: [
      { table: 'mch_info', col: 'admin_user_id', to: 'iam_user_info', toCol: 'id' },
      { table: 'mch_user', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'mch_user', col: 'user_id', to: 'iam_user_info', toCol: 'id' },
      { table: 'iam_user_role', col: 'user_id', to: 'iam_user_info', toCol: 'id' },
      { table: 'mch_app_info', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'mch_credential', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'mch_channel_merchant', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'mch_store_info', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'mch_store_info', col: 'app_id', to: 'mch_app_info', toCol: 'app_id' },
      { table: 'device_qr_code', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'device_qr_code', col: 'store_no', to: 'mch_store_info', toCol: 'store_no' },
      // 终端设备域：终端→门店、通道终端→商户、绑定→终端/通道终端、虚拟设备→门店/应用、播报打印配置→应用
      { table: 'device_terminal', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'device_terminal', col: 'store_no', to: 'mch_store_info', toCol: 'store_no' },
      { table: 'device_channel_terminal', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'device_terminal_channel_bind', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'device_terminal_channel_bind', col: 'system_terminal_no', to: 'device_terminal', toCol: 'terminal_no' },
      { table: 'device_terminal_channel_bind', col: 'channel_terminal_id', to: 'device_channel_terminal', toCol: 'id' },
      { table: 'device_virtual', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'device_virtual', col: 'app_id', to: 'mch_app_info', toCol: 'app_id' },
      { table: 'device_virtual', col: 'store_no', to: 'mch_store_info', toCol: 'store_no' },
      { table: 'device_mch_app_config', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'device_mch_app_config', col: 'app_id', to: 'mch_app_info', toCol: 'app_id' },
      // 路由：策略收敛为唯一 basic 行（双策略行会触发运行时 oneOpt 异常），basic 子表回指策略主表
      { table: 'pay_route_strategy', col: 'app_id', to: 'mch_app_info', toCol: 'app_id' },
      { table: 'pay_route_basic_config', col: 'strategy_id', to: 'pay_route_strategy', toCol: 'id' },
      // 网关配置域：主表→应用、客户端环境→主表、收银项→应用
      { table: 'pay_gateway_pay_config', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'pay_gateway_pay_config', col: 'app_id', to: 'mch_app_info', toCol: 'app_id' },
      { table: 'pay_gateway_pay_client_env', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'pay_gateway_pay_client_env', col: 'config_id', to: 'pay_gateway_pay_config', toCol: 'id' },
      { table: 'pay_gateway_cashier_item', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'pay_gateway_cashier_item', col: 'app_id', to: 'mch_app_info', toCol: 'app_id' },
      { table: 'pay_normal_order', col: 'app_id', to: 'mch_app_info', toCol: 'app_id' },
      // 交易四维度跨域引用（2026-11-10）：订单→门店/码牌/终端/通道终端出号
      { table: 'pay_normal_order', col: 'store_no', to: 'mch_store_info', toCol: 'store_no' },
      { table: 'pay_normal_order', col: 'qr_code', to: 'device_qr_code', toCol: 'code' },
      { table: 'pay_normal_order', col: 'terminal_no', to: 'device_terminal', toCol: 'terminal_no' },
      { table: 'pay_normal_order', col: 'out_terminal_no', to: 'device_channel_terminal', toCol: 'out_terminal_no' },
      // pay_trade.container_id 不再建 ref：normal/gateway 两套容器分属不同表，
      // 分型完整性由 demo-dataset checkConsistency 1a/1b + 装机断言 [4.6]#2 双向兜底
      // 网关订单：→商户/应用/门店；gateway 交易的容器回指同样由断言兜底
      { table: 'pay_gateway_order', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'pay_gateway_order', col: 'app_id', to: 'mch_app_info', toCol: 'app_id' },
      { table: 'pay_gateway_order', col: 'store_no', to: 'mch_store_info', toCol: 'store_no' },
      { table: 'pay_fund_flow', col: 'trade_no', to: 'pay_trade', toCol: 'trade_no' },
      { table: 'pay_refund_order', col: 'trade_no', to: 'pay_trade', toCol: 'trade_no' },
      // 异常订单 → 交易（动态引用 buildTransactions 生成结果，禁止硬编码）
      { table: 'pay_abnormal_order', col: 'trade_no', to: 'pay_trade', toCol: 'trade_no' },
      { table: 'mch_notice_record', col: 'task_id', to: 'mch_notice_task', toCol: 'id' },
      // 通知全家桶（2026-11-10）：已读回指公告、个人消息回指剧本内用户
      //（notice_read.user_id 与审计日志 user_id 不建 ref：bootx id=1 来自 [2] 段种子，不在 gen 行集内，
      //  由 checkConsistency 21/27 的 knownUserIds 集合 + 装机断言 [4.6]#27/#33 兜底）
      { table: 'notify_notice_read', col: 'notice_id', to: 'notify_notice', toCol: 'id' },
      { table: 'notify_message', col: 'user_id', to: 'iam_user_info', toCol: 'id' },
      // 安全域：风险命中 → 黑名单（可空，海外命中无名单行）/ 交易（动态引用）
      { table: 'pay_risk_hit', col: 'blacklist_id', to: 'pay_blacklist', toCol: 'id' },
      { table: 'pay_risk_hit', col: 'trade_no', to: 'pay_trade', toCol: 'trade_no' },
      // 域名校验商户行回指商户（平台行 mch_no='0' 不建 ref，由 checkConsistency 24 归属断言兜底）
      // unipay 日志 / 敏感词命中 → 演示商户（mch_no 可空）
      { table: 'starter_audit_unipay_log', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
      { table: 'system_sensitive_word_hit', col: 'mch_no', to: 'mch_info', toCol: 'mch_no' },
    ],
  })

  const ctx = buildIdentity(gen)
  const nIdentity = gen.count()
  buildChannels(gen, ctx)
  buildTerminals(gen, ctx)
  buildGateway(gen, ctx)
  const nChannel = gen.count()
  const txStats = buildTransactions(gen, ctx)
  buildGatewayOrders(gen, ctx, txStats)
  buildAbnormalOrders(gen, ctx, txStats)
  // 2026-11-10 三组新维度：通知全家桶 / 安全与平台（依赖 tx 动态交易引用）/ 转账终态单（续用 tx 序列）
  buildNotices(gen, ctx)
  buildSecurity(gen, ctx, txStats)
  buildTransfers(gen, ctx, txStats)
  buildP2Extras(gen, ctx)
  // 全部交易域 builder 完成后统一断言（normal + gateway + abnormal + 通知/安全/转账/P2 跨域一致）
  assertConsistency(gen)
  const { lines, stats } = gen.build()

  const part = (from, to) => lines.slice(from, to).join('\n')
  const body = [
    '-- ---------------------------------------------------------------------------',
    `-- [4.1] 演示菜单（旁路导出 ${menuSection.bypassCount} 条；纯净版排除的正是这一批）`,
    '-- ---------------------------------------------------------------------------',
    menuSection.bypass,
    '',
    '-- ---------------------------------------------------------------------------',
    `-- [4.2] mock 支付元数据（旁路导出 ${seedSection.bypassCount} 条，带列名）`,
    '-- ---------------------------------------------------------------------------',
    seedSection.bypass,
    '',
    '-- ---------------------------------------------------------------------------',
    '-- [4.3] 身份与配置（剧本生成：商户 / 用户 / 应用 / 门店×5 / 码牌×8 / 路由策略 basic / 协议版本）',
    '-- ---------------------------------------------------------------------------',
    part(0, nIdentity),
    '',
    '-- ---------------------------------------------------------------------------',
    '-- [4.4] 通道与设备网关配置（mock 全局/直连凭证 + 模拟服务商 + 微信应用 + mock 产品沙箱生效环境',
    '--       + 终端设备域 5 表 + 网关支付/收银配置 3 表）',
    '-- ---------------------------------------------------------------------------',
    part(nIdentity, nChannel),
    '',
    '-- ---------------------------------------------------------------------------',
    `-- [4.5] 交易流水（剧本生成：${txStats.orders} 单 / ${txStats.trades} 交易 / ${txStats.refunds} 退款 / `,
    `--       ${txStats.flows} 资金流水 / ${txStats.notices} 通知任务 / ${txStats.records} 通知记录 / 近 30 天；`,
    `--       网关订单 ${txStats.gwOrders} 笔 / 异常订单 ${txStats.abnormals} 条；`,
    '--       订单四维度已打通：门店加权分布 / 码牌回指 / 终端绑定链出号 / 方式按码牌形态解析；',
    `--       通知全家桶（公告 6 / 已读 6 / 个人消息 4）+ 安全与平台（黑名单 5 / 风险命中 4 / 域名校验 4 / 平台配置 2）`,
    `--       + 转账终态单 ${txStats.transfers ?? 0} 笔 + P2 审计（登录 8 / 操作 5 / unipay 6 / 敏感词命中 3）`,
    '-- ---------------------------------------------------------------------------',
    part(nChannel),
    '',
    '-- ---------------------------------------------------------------------------',
    '-- [4.6] 装机期一致性校验（失败则整个安装事务回滚，不留半个演示环境）',
    '-- ---------------------------------------------------------------------------',
    buildAssertBlock(),
  ].join('\n')

  const inserts = (body.match(/^INSERT INTO /gm) ?? []).length
  const tables = new Set([...body.matchAll(/^INSERT INTO public\.(\w+)/gm)].map((m) => m[1]))
  const total = Object.values(stats).reduce((a, b) => a + b, 0)
  console.log(`[演示段] 旁路 ${menuSection.bypassCount + seedSection.bypassCount} 条 + 剧本 ${total} 行，${tables.size} 张表 / INSERT ${inserts} 条`)
  return {
    body,
    source: '剧本生成 + 旁路导出',
    inserts,
    tables: tables.size,
    rows: total,
    txStats,
  }
}

// ---------- 拼接 ----------
function buildHeaders({ schema, seed, menu }) {
  const ts = new Date().toISOString()
  const table = `${RULE}
-- table.sql — DaxPay 表结构脚本（纯净版与演示版共用） | 工具 build/export-sql.mjs | 时间 ${ts}
-- 来源: ${ds.db} @ ${host}:${ds.port}
-- 内容: [1] 表结构 ${schema.tableCount} 张表（建表 / 主键 / 索引 / 注释）
-- 用法: 空库先执行本文件，再执行 data.sql（纯净版）或 data-demo.sql（演示版，阶段 B）
-- 说明: 只建表不插数据；执行前库须为空，重复执行会主键冲突
-- 警告: 构建产物，禁止手编；改表结构改 dev 库后重跑脚本
${RULE}
`
  const pure = `${RULE}
-- data.sql — DaxPay 纯净版数据脚本 | 工具 build/export-sql.mjs | 时间 ${ts}
-- 来源: ${ds.db} @ ${host}:${ds.port}
-- 内容: [2] 纯净种子数据（已脱敏）  [3] 菜单种子   —— 不含表结构
-- 用法: 先执行 table.sql，再执行本文件
-- 说明: 仅内置超管 bootx / 121212 与系统种子，无任何演示商户/账号/mock 数据；生产部署后必须立即修改密码
-- 警告: 构建产物，禁止手编；改纯净口径改 build/redact-data.mjs，然后重跑脚本
${RULE}
`
  const demo = `${RULE}
-- data-demo.sql — DaxPay 演示版数据脚本 | 工具 build/export-sql.mjs | 时间 ${ts}
-- 来源: ${ds.db} @ ${host}:${ds.port}（[4] 段为剧本生成，不取库内业务数据）
-- 内容: [2] 纯净种子  [3] 菜单种子（含演示菜单）  [4] 演示数据（旁路 mock 元数据 + 剧本生成的商户/通道/流水）
-- 用法: 先执行 table.sql，再执行本文件（与 data.sql 二选一，不要都执行）
-- 说明: 演示账号 ${ANCHORS.adminUser} / ${ANCHORS.merchantUser}，密码 121212；演示商户 ${ANCHORS.mchMain}；mock 通道 ${ANCHORS.mockChannelMchNo}
--       仅适用于开发/演示环境（daxpay.platform.config.sandbox-enabled=true），生产部署必须改用 data.sql
-- 警告: 构建产物，禁止手编；改演示口径改 build/demo-dataset.mjs，然后重跑脚本
${RULE}
`
  return { table, pure, demo }
}

// ---------- 主流程 ----------
const report = []
report.push('========== export-sql 产物报告 ==========')
const assertFails = []

let schemaSection = null
let seedSection = null
let menuSection = null

schemaSection = buildSchemaSection()
assertFails.push(...schemaSection.assertFails)
console.log(`[表结构] ${schemaSection.tableCount} 张表 / 表注释 ${schemaSection.commentTables} / 列注释 ${schemaSection.commentColumns} / 序列 ${schemaSection.seqCount}`)

seedSection = buildSeedSection()
menuSection = buildMenuSection()
// [4] 演示段依赖 [4.1]/[4.2] 的旁路产物（menuSection/seedSection 已就绪）
const demoSection = wantDemo ? buildDemoSection() : null

const headers = buildHeaders({ schema: schemaSection, seed: seedSection, menu: menuSection })

// 表结构：纯净/演示共用一份，只有它包含 CREATE TABLE
const tableSql = schemaSection ? headers.table + '\n' + `${RULE}
-- [1] 表结构（${schemaSection.tableCount} 张表：建表 / 主键 / 索引 / 注释）
${RULE}

${schemaSection.body}
` : null

// 数据段 = [2]+[3]
const dataBody = `${RULE}
-- [2] 纯净种子数据（白名单系统种子表 + bootx 超管，已脱敏；不含任何 mock / 演示数据）
${RULE}

${seedSection.body}

${RULE}
-- [3] 菜单种子（运营端/商户端菜单 + 内置角色绑定，重跑幂等重建）
${RULE}

${menuSection.body}
`

const pureSql = headers.pure + '\n' + dataBody
// 演示版 = 纯净版同源 [2]+[3] + [4] 演示段（含 [4.1] 演示菜单 / [4.2] mock 元数据 / [4.6] 装机期断言）
const demoSql = demoSection ? headers.demo + '\n' + dataBody + '\n' + demoSection.body + '\n' : null

// ---------- 断言 ----------
/** 只对「纯净种子段」跑：禁入表 / mock 残留 / 演示菜单 id / 商户域任何行 */
function assertSeedText(text, label) {
  const counts = new Map()
  for (const stmt of extractInsertStmts(text)) {
    const parsed = parseInsert(stmt)
    if (!parsed) {
      assertFails.push(`${label} 含无法解析的语句: ${stmt.slice(0, 80)}`)
      continue
    }
    const { table, columns, tokens } = parsed
    if (!columns || !tokens.length) assertFails.push(`${label} 出现无列名 INSERT: ${table}`)
    if (FORBIDDEN_IN_DATA.has(table)) assertFails.push(`${label} 混入禁入表 ${table}`)
    // 纯净版口径: 任何商户域 / mock 域 / 码牌行都不得出现
    if (table.startsWith('mch_') || table.startsWith('mock_') || table === 'device_qr_code') {
      assertFails.push(`${label} 混入演示域表 ${table}`)
    }
    if (MOCK_ROW_RULES.has(table) && isMockRow(table, columns, tokens)) {
      assertFails.push(`${label} 残留 mock 行: ${table}`)
    }
    if (table === 'iam_perm_menu') {
      const idIdx = columns ? columns.indexOf('id') : -1
      const id = idIdx >= 0 ? tokens?.[idIdx]?.raw : undefined
      if (DEMO_MENU_IDS.has(id)) assertFails.push(`${label} 残留演示菜单 id=${id}`)
    }
    counts.set(table, (counts.get(table) ?? 0) + 1)
  }
  return counts
}

/** 对整份纯净单文件跑：不得出现演示数据锚点 */
function assertNoDemoAnchors(text, label) {
  for (const anchor of PURE_FORBIDDEN_ANCHORS) {
    if (text.includes(anchor)) assertFails.push(`${label} 残留演示锚点 ${anchor}`)
  }
}

const seedCounts = assertSeedText(seedSection.body, '纯净种子段')
assertNoDemoAnchors(pureSql, 'data.sql')

// bootx 三件套 + 登录链路行必须各恰好 1 行（多行 = 混入其他账号，0 行 = 登录不进系统）
for (const [t, expect] of [['iam_user_info', 1], ['iam_user_expand_info', 1], ['iam_user_role', 1], ['iam_user_password_security', 1]]) {
  const got = seedCounts.get(t) ?? 0
  if (got !== expect) assertFails.push(`data.sql 的 ${t} 应有 ${expect} 行(仅 bootx id=1), 实际 ${got} 行`)
}

// 表结构只允许出现在 table.sql：data 文件混入建表语句会在 table.sql 之后撞 already exists
if (/^CREATE TABLE /m.test(pureSql)) assertFails.push('data.sql 混入建表语句（表结构应只在 table.sql）')
if (/^CREATE SEQUENCE /m.test(pureSql)) assertFails.push('data.sql 混入 CREATE SEQUENCE')
if (!/^CREATE TABLE /m.test(tableSql)) assertFails.push('table.sql 无建表语句')
for (const [label, text] of [['table.sql', tableSql], ['data.sql', pureSql]]) {
  if (/^\\restrict /m.test(text) || /^\\unrestrict /m.test(text)) assertFails.push(`${label} 残留 \\restrict / \\unrestrict 元命令`)
  if (/^SET transaction_timeout\s*=/m.test(text)) assertFails.push(`${label} 残留 SET transaction_timeout`)
}

// 菜单段: 演示菜单必须真的被剔出去，且剔除数量与 DEMO_MENU_IDS 对得上
if (menuSection.menuCount + menuSection.bypassCount === 0) assertFails.push('菜单段为空（导出失败）')
const keptMenuIds = new Set([...menuSection.body.matchAll(/^INSERT INTO public\.iam_perm_menu \(/gm)].map((_, i) => i))
if (keptMenuIds.size === 0) assertFails.push('菜单段无 iam_perm_menu 插入')

// 演示段: 关键锚点 / 表集 / 装机断言必须真实存在（构建期与装机期双重兜底）
if (demoSection) {
  if (!demoSql) {
    assertFails.push('data-demo.sql 未生成（--profile demo/all 却无 [4] 段）')
  } else {
    for (const anchor of [
      ANCHORS.mchMain, ANCHORS.appMain, ANCHORS.mockChannelMchNo, ANCHORS.mockWxAppId,
      ANCHORS.qrCodes[0], ANCHORS.qrCodes[7], ANCHORS.qrCodes[8], ANCHORS.stores[4],
      ANCHORS.terminals[0], ANCHORS.virtualDevices[0],
      // 2026-11-10 新维度锚点：黑名单 IP / 域名校验文件名 / 转账单号前缀 / 种子敏感词 / 平台配置
      '203.0.113.10', 'MP_verify_', 'DEMOTRF', '测试敏感词', 'MP_verify_a1b2c3d4e5f60718',
    ]) {
      if (!demoSql.includes(anchor)) assertFails.push(`data-demo.sql 缺演示锚点 ${anchor}`)
    }
    for (const t of DEMO_STALE_ANCHORS) {
      if (demoSql.includes(t)) assertFails.push(`data-demo.sql 残留 dev 示例商户锚点 ${t}`)
    }
    for (const t of PLUS_ONLY_TABLES) {
      if (new RegExp(`^INSERT INTO public\\.${t}\\b`, 'm').test(demoSql)) {
        assertFails.push(`data-demo.sql 混入 plus 独有表 ${t}（开源版无此表/列）`)
      }
    }
    if (!/^DO \$\$/m.test(demoSection.body)) assertFails.push('演示段缺 [4.6] DO $$ 装机期断言')
    if (!/RAISE EXCEPTION/.test(demoSection.body)) assertFails.push('演示段 DO $$ 块无 RAISE EXCEPTION')
    if (demoSection.txStats.orders < 150) assertFails.push(`演示订单数 ${demoSection.txStats.orders} 低于装机断言下限 150`)
    // 网关订单/异常订单（2026-11-10 新域）：行数下限 + 表必须真实出现
    if (demoSection.txStats.gwOrders < 9) assertFails.push(`网关订单数 ${demoSection.txStats.gwOrders} 低于预期 9`)
    if (demoSection.txStats.abnormals < 3) assertFails.push(`异常订单数 ${demoSection.txStats.abnormals} 低于预期 3`)
    if (!/^INSERT INTO public\.pay_gateway_order /m.test(demoSection.body)) assertFails.push('演示段缺 pay_gateway_order 网关订单')
    if (!/^INSERT INTO public\.pay_abnormal_order /m.test(demoSection.body)) assertFails.push('演示段缺 pay_abnormal_order 异常订单')
    if (!/^INSERT INTO public\.mock_gateway_credential \(id, /m.test(demoSection.body)) {
      assertFails.push('演示段缺 mock_gateway_credential（主键固定 id=1）')
    }
    // 2026-11-10 新维度表必须真实出现（通知全家桶 / 安全与平台 / 转账 / P2 审计）
    for (const t of [
      'notify_notice', 'notify_notice_read', 'notify_message',
      'pay_blacklist', 'pay_risk_hit', 'mch_wx_domain_verify', 'system_platform_config',
      'starter_audit_login_log', 'starter_audit_operate_log', 'starter_audit_unipay_log',
      'system_sensitive_word_hit', 'pay_transfer_trade', 'pay_transfer_order_wechat',
      'pay_transfer_order_alipay', 'pay_transfer_order_douyin',
    ]) {
      if (!new RegExp(`^INSERT INTO public\\.${t} `, 'm').test(demoSection.body)) {
        assertFails.push(`演示段缺 ${t}（2026-11-10 新维度表）`)
      }
    }
    // 黑名单红线构建期兜底：演示段任何黑名单行不得拉黑内网/回环/北京段（构建期断言 22 之外的产物级双保险）
    const blacklistInserts = demoSection.body.split('\n').filter((ln) => ln.startsWith('INSERT INTO public.pay_blacklist '))
    if (blacklistInserts.some((ln) => /'127\.|'192\.168\.|'10\.\d|'localhost|'::1|'11(?:0000)?'/.test(ln))) {
      assertFails.push('演示段黑名单含禁拉段（内网/回环/北京），会拦死本机演示扫码')
    }
    const mockProductCfg = demoSection.body.split('\n').find((ln) => ln.startsWith('INSERT INTO public.pay_md_product_config '))
    if (!mockProductCfg || !mockProductCfg.includes("'mock_pay'") || !mockProductCfg.includes("'sandbox'")) {
      assertFails.push('演示段缺 pay_md_product_config(mock_pay, sandbox)：路由环境一致性校验会拒绝演示支付')
    }
    if (/^CREATE TABLE /m.test(demoSql)) assertFails.push('data-demo.sql 混入建表语句（表结构应只在 table.sql）')
    if (/^CREATE SEQUENCE /m.test(demoSql)) assertFails.push('data-demo.sql 混入 CREATE SEQUENCE')
    if (/^\\restrict /m.test(demoSql) || /^\\unrestrict /m.test(demoSql)) assertFails.push('data-demo.sql 残留 \\restrict / \\unrestrict 元命令')
    if (/^SET transaction_timeout\s*=/m.test(demoSql)) assertFails.push('data-demo.sql 残留 SET transaction_timeout')
  }
}

// ---------- 报告 ----------
report.push(`[table.sql] 表 ${schemaSection.tableCount} 张（库 ${schemaSection.dbTableCount}） / 表注释 ${schemaSection.commentTables} / 列注释 ${schemaSection.commentColumns} / 序列 ${schemaSection.seqCount}`)
report.push(`[data.sql] 纯净种子表 ${seedCounts.size} 张, INSERT ${[...seedCounts.values()].reduce((a, b) => a + b, 0)} 条（不含表结构）`)
for (const [t, c] of [...seedCounts.entries()].sort((a, b) => a[0].localeCompare(b[0]))) {
  report.push(`  ${t.padEnd(34)} ${c}`)
}
report.push(`[菜单段] 菜单 ${menuSection.menuCount} 条 + 角色绑定 ${menuSection.roleMenuCount} 条（演示菜单旁路 ${menuSection.bypassCount} 条, 进 [4.1]）`)
if (demoSection) {
  report.push(`[data-demo.sql] 旁路 ${menuSection.bypassCount + seedSection.bypassCount} 条 + 剧本 ${demoSection.rows} 行，${demoSection.tables} 张表 / INSERT ${demoSection.inserts} 条`)
  report.push(`  交易: ${demoSection.txStats.orders} 单 / ${demoSection.txStats.trades} 交易 / ${demoSection.txStats.refunds} 退款 / ${demoSection.txStats.flows} 资金流水 / ${demoSection.txStats.notices} 通知任务 / ${demoSection.txStats.records} 通知记录`)
  report.push(`  网关: ${demoSection.txStats.gwOrders} 网关订单 / ${demoSection.txStats.gwTrades} gateway 交易；异常订单 ${demoSection.txStats.abnormals} 条`)
  const dimStats = { notify_notice: 6, notify_notice_read: 6, notify_message: 4, pay_blacklist: 5, pay_risk_hit: 4, mch_wx_domain_verify: 4, system_platform_config: 2, starter_audit_login_log: 8, starter_audit_operate_log: 5, starter_audit_unipay_log: 6, system_sensitive_word_hit: 3, pay_transfer_trade: 3, pay_transfer_order_wechat: 1, pay_transfer_order_alipay: 1, pay_transfer_order_douyin: 1 }
  const dimBad = Object.entries(dimStats).filter(([t, want]) => {
    const got = (demoSection.body.match(new RegExp(`^INSERT INTO public\\.${t} `, 'gm')) ?? []).length
    return got !== want
  })
  report.push(`  新维度: ${dimBad.length === 0 ? '行数全部符合预期（通知/安全/转账/审计 15 表）' : `行数不符: ${dimBad.map(([t]) => t).join(', ')}`}`)
}
report.push(`[检查] ${assertFails.length ? '存在不通过项' : '全部通过'}`)

const reportText = report.join('\n')
console.log(reportText)
// 报告只在试跑目录留档；正式目录（_config/sql）不落报告，避免产物目录混杂
// 注意写 outDir 而非 tmpDir: tmpDir 在落盘成功后会被整体删除, 写这里等于没留档
if (outDir !== sqlDir) writeFileSync(join(outDir, 'build-report.txt'), reportText + '\n')

if (assertFails.length) {
  console.error('\n检查不通过明细:')
  for (const f of assertFails) console.error(`  - ${f}`)
  console.error('\n[未落盘] 检查不通过，_config/sql 现有产物未改动')
  exit(1)
}

// ---------- 落盘（.tmp → rename） ----------
function writeAtomic(name, content) {
  const target = join(outDir, name)
  const tmp = join(tmpDir, `${name}.tmp`)
  writeFileSync(tmp, content)
  renameSync(tmp, target)
  console.log(`[产物] ${target} (${(Buffer.byteLength(content) / 1024).toFixed(0)} KB)`)
}

// table.sql 两版共用：任一 profile 都写，避免表结构与数据文件版本错配
if (tableSql) writeAtomic('table.sql', tableSql)
if (wantPure && pureSql) writeAtomic('data.sql', pureSql)
if (wantDemo && demoSql) writeAtomic('data-demo.sql', demoSql)
rmSync(tmpDir, { recursive: true, force: true })
console.log('[完成] 产物已落 ' + outDir)
