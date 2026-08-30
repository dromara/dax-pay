#!/usr/bin/env node
/**
 * table.sql 结构规范分析脚本（适配 Navicat 导出格式）
 *
 * 检查维度（对照 AGENTS.md 数据库 SQL 规则）：
 *   1. timestamp without time zone 违规（项目规定统一 timestamptz）
 *   2. 缺表注释 / 缺列注释 / 缺索引注释（COMMENT ON 覆盖差集）
 *   3. 索引注释规则：纯主键约束豁免，UNIQUE 约束与业务索引必须有注释
 *   4. 非中文注释（表/字段注释要求中文说明）
 *   5. 时间精度不统一（timestamptz 有无精度声明混用）
 *
 * 格式假设（兼容两种导出格式, 2026-08-30 起 table.sql 已切换为 pg_dump）：
 *   Navicat "Premium Data Transfer" 导出：
 *     CREATE TABLE "public"."t" ( ... ) 换行 ;
 *     CREATE [UNIQUE] INDEX "name" ON ... USING btree ( 可能跨行 );
 *     ALTER TABLE ... ADD CONSTRAINT "name" PRIMARY KEY/UNIQUE (...);
 *     COMMENT ON TABLE|COLUMN|INDEX "public"."x"[."y"] IS '...';
 *   pg_dump --schema-only 导出：
 *     CREATE TABLE public.t ( 四空格缩进无引号列 );
 *     CREATE [UNIQUE] INDEX name ON public.t USING btree (...);
 *     ALTER TABLE ONLY public.t 换行 ADD CONSTRAINT name PRIMARY KEY/UNIQUE (...);
 *     COMMENT ON TABLE|COLUMN|INDEX public.x[.y] IS '...';
 *
 * 用法：node analyze-table.mjs <table.sql>
 */

import { createReadStream } from 'node:fs'
import { createInterface } from 'node:readline'
import { argv, exit } from 'node:process'

const inputPath = argv[2]
if (!inputPath) {
  console.error('用法：node analyze-table.mjs <table.sql>')
  exit(1)
}

// ---------- 解析结果容器 ----------
const tables = new Map()        // 表名 -> 列数组 [{ name, type }]
const tableComments = new Map() // 表名 -> 注释文本
const columnComments = new Map()// "表.列" -> 注释文本
const indexComments = new Map() // 索引名 -> 注释文本
const indexDefs = new Map()     // 索引/约束名 -> { kind: 'index'|'unique'|'pkey', table, cols }
const sequences = new Set()     // CREATE SEQUENCE 名

// ---------- 双格式类型归一 ----------
// pg_dump 把 timestamptz 写作三词形式 `timestamp(6) with time zone`, Navicat 写作 `timestamptz(6)`
// 捕获截断后可能带 NOT 等尾词, 归一到 Navicat 短形式后再做规范检查
function normalizeType(t) {
  return t
    .replace(/^timestamp(\(\d+\))? with time zone.*$/i, 'timestamptz$1')
    .replace(/^timestamp(\(\d+\))? without time zone.*$/i, 'timestamp$1')
}

// ---------- 状态机解析 ----------
const lines = await new Promise((resolve) => {
  const arr = []
  const rl = createInterface({ input: createReadStream(inputPath, 'utf8'), crlfDelay: Infinity })
  rl.on('line', (l) => arr.push(l))
  rl.on('close', () => resolve(arr))
})

let i = 0
while (i < lines.length) {
  const line = lines[i]

  // CREATE TABLE 块（到 ")\s*;" 或 ")" 单独成行为止）
  // 双格式: Navicat `CREATE TABLE "public"."t" (` / pg_dump `CREATE TABLE public.t (`
  let m = line.match(/^CREATE TABLE (?:public\.|"public"\.)"?(\w+)"? \($/)
  if (m) {
    const table = m[1]
    const cols = []
    i++
    while (i < lines.length && !/^\)/.test(lines[i])) {
      // 双格式列行: Navicat `  "col" type` / pg_dump `    col type`; 跳过内联 CONSTRAINT 行
      // 类型捕获最多 4 词, 容纳 pg_dump 的 `timestamp(6) with time zone`
      const cm = lines[i].match(/^ {2,4}(?!CONSTRAINT\b)"?(\w+)"? (\S+(?: \w+){0,3}(?:\(\d+\))?).*/)
      if (cm) cols.push({ name: cm[1], type: normalizeType(cm[2].trim()) })
      i++
    }
    tables.set(table, cols)
    i++
    continue
  }

  // CREATE [UNIQUE] INDEX（索引名在首行，语句可能跨行）
  // 双格式: Navicat `CREATE INDEX "name" ON "public"."t"` / pg_dump `CREATE INDEX name ON public.t`
  m = line.match(/^CREATE (UNIQUE )?INDEX "?(\w+)"? ON (?:public\.|"public"\.)"?(\w+)"?/)
  if (m) {
    indexDefs.set(m[2], { kind: m[1] ? 'unique' : 'index', table: m[3] })
    while (i < lines.length && !/;\s*$/.test(lines[i])) i++
    i++
    continue
  }

  // ALTER TABLE ADD CONSTRAINT（主键/唯一约束）
  // 双格式: Navicat 单行 `ALTER TABLE "public"."t" ADD CONSTRAINT "name" PRIMARY KEY ...`
  //         pg_dump 两行 `ALTER TABLE ONLY public.t` + 换行 `ADD CONSTRAINT name PRIMARY KEY ...`
  m = line.match(/^ALTER TABLE (?:ONLY )?(?:public\.|"public"\.)"?(\w+)"? ADD CONSTRAINT "?(\w+)"? (PRIMARY KEY|UNIQUE)\b/)
  if (m) {
    indexDefs.set(m[2], { kind: m[3] === 'PRIMARY KEY' ? 'pkey' : 'unique', table: m[1] })
    i++
    continue
  }
  m = line.match(/^ALTER TABLE ONLY (?:public\.|"public"\.)"?(\w+)"?$/)
  if (m && i + 1 < lines.length) {
    const cm = lines[i + 1].match(/^\s+ADD CONSTRAINT "?(\w+)"? (PRIMARY KEY|UNIQUE)\b/)
    if (cm) {
      indexDefs.set(cm[1], { kind: cm[2] === 'PRIMARY KEY' ? 'pkey' : 'unique', table: m[1] })
      i += 2
      continue
    }
  }

  // CREATE SEQUENCE
  // 双格式: Navicat `CREATE SEQUENCE "public"."x"` / pg_dump `CREATE SEQUENCE public.x`
  m = line.match(/^CREATE SEQUENCE (?:public\.|"public"\.)"?(\w+)"?/)
  if (m) sequences.add(m[1])

  // COMMENT ON 三类（双格式: 带引号 Navicat / 无引号 pg_dump）
  m = line.match(/^COMMENT ON TABLE (?:public\.|"public"\.)"?(\w+)"? IS '(.*)';/)
  if (m) { tableComments.set(m[1], m[2]); i++; continue }
  m = line.match(/^COMMENT ON COLUMN (?:public\.|"public"\.)"?(\w+)"?\."?(\w+)"? IS '(.*)';/)
  if (m) { columnComments.set(`${m[1]}.${m[2]}`, m[3]); i++; continue }
  m = line.match(/^COMMENT ON INDEX (?:public\.|"public"\.)"?(\w+)"? IS '(.*)';/)
  if (m) { indexComments.set(m[1], m[2]); i++; continue }

  i++
}

// ---------- 检查 1: timestamp without time zone ----------
const noTzCols = []
for (const [t, cols] of tables) {
  for (const c of cols) {
    if (/^timestamp/i.test(c.type) && !/^timestamptz/i.test(c.type)) {
      noTzCols.push(`${t}.${c.name}  ${c.type}`)
    }
  }
}

// ---------- 检查 2/3: 注释覆盖差集 ----------
const missingTableComment = [...tables.keys()].filter((t) => !tableComments.has(t))
const missingColumnComments = []
for (const [t, cols] of tables) {
  for (const c of cols) {
    if (!columnComments.has(`${t}.${c.name}`)) missingColumnComments.push(`${t}.${c.name}`)
  }
}
// 索引注释: 纯主键豁免（AGENTS: 纯外键/主键无需单独注释）
const needCommentIndexes = [...indexDefs].filter(([, d]) => d.kind !== 'pkey').map(([n]) => n)
const missingIndexComments = needCommentIndexes.filter((n) => !indexComments.has(n))

// ---------- 检查 4: 非中文注释 ----------
const hasChinese = (s) => /[\u4e00-\u9fff]/.test(s)
const nonChineseComments = []
for (const [t, c] of tableComments) if (!hasChinese(c)) nonChineseComments.push(`表 ${t}: "${c}"`)
for (const [tc, c] of columnComments) if (!hasChinese(c)) nonChineseComments.push(`列 ${tc}: "${c}"`)
for (const [n, c] of indexComments) if (!hasChinese(c)) nonChineseComments.push(`索引 ${n}: "${c}"`)

// ---------- 检查 5: timestamptz 精度声明统一性 ----------
const tzPrecision = new Map()
for (const [, cols] of tables) {
  for (const c of cols) {
    if (/^timestamptz/i.test(c.type)) tzPrecision.set(c.type, (tzPrecision.get(c.type) ?? 0) + 1)
  }
}

// ---------- 报告 ----------
const out = []
out.push('================ table.sql 规范分析报告 ================')
out.push(`表: ${tables.size}  列: ${[...tables.values()].reduce((a, c) => a + c.length, 0)}  索引/约束: ${indexDefs.size}  序列: ${sequences.size}`)
out.push(`注释: 表 ${tableComments.size}  列 ${columnComments.size}  索引 ${indexComments.size}`)
out.push('')

out.push(`[1] timestamp without time zone 违规: ${noTzCols.length} 列`)
noTzCols.forEach((c) => out.push(`    ${c}`))
out.push('')

out.push(`[2] 缺表注释: ${missingTableComment.length} 张`)
missingTableComment.forEach((t) => out.push(`    ${t}`))
out.push('')

out.push(`[3] 缺列注释: ${missingColumnComments.length} 列`)
missingColumnComments.forEach((c) => out.push(`    ${c}`))
out.push('')

out.push(`[4] 缺索引注释(纯主键已豁免): ${missingIndexComments.length} 个`)
missingIndexComments.forEach((n) => {
  const d = indexDefs.get(n)
  out.push(`    ${n}  [${d.kind}] on ${d.table}`)
})
out.push('')

out.push(`[5] 非中文注释: ${nonChineseComments.length} 条`)
nonChineseComments.slice(0, 60).forEach((c) => out.push(`    ${c}`))
if (nonChineseComments.length > 60) out.push(`    ... 其余 ${nonChineseComments.length - 60} 条略`)
out.push('')

out.push('[6] timestamptz 精度声明分布(写法统一性):')
for (const [t, n] of [...tzPrecision].sort((a, b) => b[1] - a[1])) out.push(`    ${t}: ${n} 列`)
out.push('======================== 结束 ========================')
console.log(out.join('\n'))
