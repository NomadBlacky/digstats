package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class Secrets(
  id: Long,
  siteId: Long,
  projectId: Long,
  scope: String,
  engine: String,
  key: String,
  value: String,
  updatedAt: ZonedDateTime) {

  def save()(implicit session: DBSession = Secrets.autoSession): Secrets = Secrets.save(this)(session)

  def destroy()(implicit session: DBSession = Secrets.autoSession): Int = Secrets.destroy(this)(session)

}


object Secrets extends SQLSyntaxSupport[Secrets] {

  override val tableName = "secrets"

  override val columns = Seq("id", "site_id", "project_id", "scope", "engine", "key", "value", "updated_at")

  def apply(s: SyntaxProvider[Secrets])(rs: WrappedResultSet): Secrets = apply(s.resultName)(rs)
  def apply(s: ResultName[Secrets])(rs: WrappedResultSet): Secrets = new Secrets(
    id = rs.get(s.id),
    siteId = rs.get(s.siteId),
    projectId = rs.get(s.projectId),
    scope = rs.get(s.scope),
    engine = rs.get(s.engine),
    key = rs.get(s.key),
    value = rs.get(s.value),
    updatedAt = rs.get(s.updatedAt)
  )

  val s = Secrets.syntax("s")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Secrets] = {
    withSQL {
      select.from(Secrets as s).where.eq(s.id, id)
    }.map(Secrets(s.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Secrets] = {
    withSQL(select.from(Secrets as s)).map(Secrets(s.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Secrets as s)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Secrets] = {
    withSQL {
      select.from(Secrets as s).where.append(where)
    }.map(Secrets(s.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Secrets] = {
    withSQL {
      select.from(Secrets as s).where.append(where)
    }.map(Secrets(s.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Secrets as s).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    siteId: Long,
    projectId: Long,
    scope: String,
    engine: String,
    key: String,
    value: String,
    updatedAt: ZonedDateTime)(implicit session: DBSession = autoSession): Secrets = {
    val generatedKey = withSQL {
      insert.into(Secrets).namedValues(
        column.siteId -> siteId,
        column.projectId -> projectId,
        column.scope -> scope,
        column.engine -> engine,
        column.key -> key,
        column.value -> value,
        column.updatedAt -> updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    Secrets(
      id = generatedKey,
      siteId = siteId,
      projectId = projectId,
      scope = scope,
      engine = engine,
      key = key,
      value = value,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: collection.Seq[Secrets])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("siteId") -> entity.siteId,
        Symbol("projectId") -> entity.projectId,
        Symbol("scope") -> entity.scope,
        Symbol("engine") -> entity.engine,
        Symbol("key") -> entity.key,
        Symbol("value") -> entity.value,
        Symbol("updatedAt") -> entity.updatedAt))
    SQL("""insert into secrets(
      site_id,
      project_id,
      scope,
      engine,
      key,
      value,
      updated_at
    ) values (
      {siteId},
      {projectId},
      {scope},
      {engine},
      {key},
      {value},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Secrets)(implicit session: DBSession = autoSession): Secrets = {
    withSQL {
      update(Secrets).set(
        column.id -> entity.id,
        column.siteId -> entity.siteId,
        column.projectId -> entity.projectId,
        column.scope -> entity.scope,
        column.engine -> entity.engine,
        column.key -> entity.key,
        column.value -> entity.value,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Secrets)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Secrets).where.eq(column.id, entity.id) }.update.apply()
  }

}
