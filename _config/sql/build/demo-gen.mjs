/**
 * demo-gen.mjs — 演示数据编译器（锚点/剧本 → 带列名 INSERT + 断言）
 *
 * 自 DaxPay Plus 商业版 _config/sql/build/demo-gen.mjs 移植（2026-11-02），
 * 时间工具按开源版口径对齐：全库时间列为 `timestamp(6) with time zone`，
 * 因此演示数据的时间表达式一律产出 timestamptz（`now()` / `now() - interval '…'`），
 * 不再像 plus 版那样先 `AT TIME ZONE 'Asia/Shanghai'` 降成 naive 时间戳。
 * （阶段 A 建模块、阶段 B 起由 demo-dataset.mjs 完整使用，见 export-sql.mjs 的 [4] 段。）
 *
 * 设计要点：
 *   1. 列契约来自 table.sql（仓内构建产物），不连库。新增列不影响旧剧本（未声明的列走 NULL/默认值）；
 *      NOT NULL 且无默认值的列未声明则构建期报错，不再出现"装机才发现少列"。
 *   2. 引用完整性显式声明（daxpay 无物理外键），生成期校验，避免造出悬空引用。
 *   3. 金额/时间一致性由剧本侧断言（见 demo-dataset.mjs），本模块只负责渲染与结构性校验。
 *   4. 时间用 `now()` / `now() - interval '…'` 表达式（raw()），装机即新鲜。
 *
 * 用法（由 export-sql.mjs 调用）：
 *   const gen = createGenerator(schemaText)
 *   gen.add('mch_info', { id: 1n, mch_no: 'M1', ... })
 *   gen.build()  →  { sql, lines, stats, fails }
 */

import { readFileSync } from 'node:fs'

// ---------- 值类型 ----------

/** 原样 SQL 表达式（时间偏移、函数调用等），渲染时不加引号 */
export class Raw {
  constructor(sql) {
    this.sql = sql
  }
}

export const raw = (sql) => new Raw(sql)

/** jsonb 字面量 */
export class Json {
  constructor(value) {
    this.value = value
  }
}

export const json = (value) => new Json(value)

/** 渲染单个值为 SQL 字面量 */
export function sqlLiteral(v) {
  if (v === null || v === undefined) return 'NULL'
  if (v instanceof Raw) return v.sql
  if (v instanceof Json) return `'${JSON.stringify(v.value).replace(/'/g, "''")}'::jsonb`
  if (typeof v === 'boolean') return v ? 'true' : 'false'
  if (typeof v === 'number' || typeof v === 'bigint') return String(v)
  return `'${String(v).replace(/'/g, "''")}'`
}

// ---------- table.sql 列契约解析 ----------

const CREATE_RE = /^CREATE TABLE public\.(\w+) \($/

/**
 * 解析 table.sql，得到 表 → 列 → { type, notNull, hasDefault }
 * @param {string} text table.sql 全文
 * @returns {Map<string, Map<string, {type: string, notNull: boolean, hasDefault: boolean}>>}
 */
export function parseTableSql(text) {
  const schema = new Map()
  let cur = null
  for (const rawLine of text.split('\n')) {
    const line = rawLine.trimEnd()
    const m = line.match(CREATE_RE)
    if (m) {
      cur = new Map()
      schema.set(m[1], cur)
      continue
    }
    if (!cur) continue
    if (line === ');') {
      cur = null
      continue
    }
    // 列定义行：`    name type ... ,`（类型里可能有 (10,4) 这类括号，但整行以 , 结尾）
    const cm = line.match(/^\s{4}(\w+) (.+?)(,)?$/)
    if (!cm) continue
    const [, name, rest] = cm
    cur.set(name, {
      type: rest.replace(/,\s*$/, '').replace(/\s+(NOT NULL|DEFAULT .*?)$/g, '').trim(),
      notNull: /\bNOT NULL\b/.test(rest),
      hasDefault: /\bDEFAULT\b/.test(rest),
    })
  }
  return schema
}

// ---------- 生成器 ----------

/**
 * @param {string} schemaText table.sql 全文
 * @param {object} [opts]
 * @param {Set<string>} [opts.track] 需要建索引供引用校验的 `表.列`（如 `mch_info.mch_no`）
 * @param {Array<{table:string,col:string,to:string,toCol?:string,optional?:boolean}>} [opts.refs]
 */
export function createGenerator(schemaText, opts = {}) {
  const schema = parseTableSql(schemaText)
  const rows = [] // { table, obj }
  const fails = []
  const track = opts.track ?? new Set()
  const refs = opts.refs ?? []

  function table(name) {
    const t = schema.get(name)
    if (!t) {
      fails.push(`表 ${name} 在 table.sql 中不存在（拼错表名或结构未导出）`)
      return null
    }
    return t
  }

  /** 登记一行 */
  function add(tbl, obj) {
    if (!table(tbl)) return
    rows.push({ table: tbl, obj })
  }

  /** 批量登记 */
  function addAll(tbl, list) {
    for (const o of list) add(tbl, o)
  }

  /** 占位主键分配（锚点段连续编号） */
  let nextId = null
  function idStart(start) {
    nextId = BigInt(start)
  }
  function next() {
    if (nextId === null) throw new Error('尚未调用 idStart()')
    const v = nextId
    nextId += 1n
    return v
  }

  /** 已登记行数（供分段拼装用） */
  function count() {
    return rows.length
  }

  /** 取某表已登记的全部行（供一致性断言使用） */
  function all(tbl) {
    return rows.filter((r) => r.table === tbl).map((r) => r.obj)
  }

  /** 结构性校验：列存在 / NOT NULL 覆盖 */
  function validate() {
    const byTable = new Map()
    for (const r of rows) {
      if (!byTable.has(r.table)) byTable.set(r.table, [])
      byTable.get(r.table).push(r.obj)
    }
    for (const [tbl, list] of byTable) {
      const cols = schema.get(tbl)
      if (!cols) continue
      const required = [...cols.entries()].filter(([, c]) => c.notNull && !c.hasDefault).map(([n]) => n)
      const declared = new Set(list.flatMap((o) => Object.keys(o)))
      for (const r of list) {
        for (const k of Object.keys(r)) {
          if (!cols.has(k)) fails.push(`${tbl} 声明的列 ${k} 在 table.sql 中不存在（列名拼错或被删列）`)
        }
      }
      for (const col of required) {
        if (!declared.has(col)) fails.push(`${tbl} 缺少必填列 ${col}（NOT NULL 且无默认值，必须声明）`)
      }
    }
  }

  /** 引用完整性校验 */
  function validateRefs() {
    const idx = new Map() // `table.col` → Set
    for (const key of track) {
      const [t, c] = key.split('.')
      const set = new Set()
      for (const r of rows) {
        if (r.table === t && r.obj[c] !== undefined && r.obj[c] !== null) set.add(String(r.obj[c]))
      }
      idx.set(key, set)
    }
    for (const ref of refs) {
      const target = idx.get(`${ref.to}.${ref.toCol ?? 'id'}`)
      if (!target) {
        fails.push(`引用规则 ${ref.table}.${ref.col} → ${ref.to} 的索引未建立（track 缺少 ${ref.to}.${ref.toCol ?? 'id'}）`)
        continue
      }
      for (const r of rows) {
        if (r.table !== ref.table) continue
        const v = r.obj[ref.col]
        if (v === undefined || v === null) continue
        if (!target.has(String(v))) {
          fails.push(`${ref.table}.${ref.col} = ${v} 在 ${ref.to} 中不存在（悬空引用）`)
        }
      }
    }
  }

  /** 渲染 SQL（按登记顺序，父表先行由剧本保证） */
  function emit() {
    const lines = []
    for (const r of rows) {
      const cols = Object.keys(r.obj)
      const vals = cols.map((c) => sqlLiteral(r.obj[c]))
      lines.push(`INSERT INTO public.${r.table} (${cols.join(', ')}) VALUES (${vals.join(', ')});`)
    }
    const stats = {}
    for (const r of rows) stats[r.table] = (stats[r.table] ?? 0) + 1
    return { sql: lines.join('\n'), lines, stats, fails }
  }

  /** 全量校验 + 渲染，失败时 throws */
  function build() {
    validate()
    validateRefs()
    if (fails.length) {
      const err = new Error(`[demo-gen] 校验失败 ${fails.length} 项:\n  - ${fails.slice(0, 30).join('\n  - ')}`)
      err.fails = fails
      throw err
    }
    return emit()
  }

  return { add, addAll, idStart, next, build, emit, validate, validateRefs, all, count, get schema() { return schema } }
}

// ---------- 时间工具（开源版口径：全库时间列为 timestamp(6) with time zone） ----------

/** 相对安装时刻的时间表达式：ago('3 day 2 hour') → now() - interval '3 day 2 hour'（timestamptz） */
export const ago = (interval) => raw(`now() - interval '${interval}'`)

/** 安装时刻的时间表达式（timestamptz） */
export const now = () => raw('now()')

/**
 * 截断到东八区当日 00:00 再偏移，用于「某天 x 点」的确定性落点。
 * date_trunc 先按 Asia/Shanghai 取墙钟日界（naive），加减 interval 后再用
 * `AT TIME ZONE 'Asia/Shanghai'` 还原成 timestamptz，保证装机时刻在任意时区会话下都落在东八区同一钟点。
 */
export const dayAt = (daysAgo, hour, minute = 0) =>
  raw(`((date_trunc('day', now() AT TIME ZONE 'Asia/Shanghai') - interval '${daysAgo} day' + interval '${hour} hour ${minute} minute') AT TIME ZONE 'Asia/Shanghai')`)

// ---------- 便捷：从文件读 schema ----------

export function loadSchemaText(tableSqlPath) {
  return readFileSync(tableSqlPath, 'utf8')
}
