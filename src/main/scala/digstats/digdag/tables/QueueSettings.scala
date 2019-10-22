package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class QueueSettings(
  id: Int,
  siteId: Int,
  name: String,
  config: Option[String] = None,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime) {

  def save()(implicit session: DBSession = QueueSettings.autoSession): QueueSettings = QueueSettings.save(this)(session)

  def destroy()(implicit session: DBSession = QueueSettings.autoSession): Int = QueueSettings.destroy(this)(session)

}


object QueueSettings extends SQLSyntaxSupport[QueueSettings] {

  override val tableName = "queue_settings"

  override val columns = Seq("id", "site_id", "name", "config", "created_at", "updated_at")

  def apply(qs: SyntaxProvider[QueueSettings])(rs: WrappedResultSet): QueueSettings = apply(qs.resultName)(rs)
  def apply(qs: ResultName[QueueSettings])(rs: WrappedResultSet): QueueSettings = new QueueSettings(
    id = rs.get(qs.id),
    siteId = rs.get(qs.siteId),
    name = rs.get(qs.name),
    config = rs.get(qs.config),
    createdAt = rs.get(qs.createdAt),
    updatedAt = rs.get(qs.updatedAt)
  )

  val qs = QueueSettings.syntax("qs")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[QueueSettings] = {
    withSQL {
      select.from(QueueSettings as qs).where.eq(qs.id, id)
    }.map(QueueSettings(qs.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[QueueSettings] = {
    withSQL(select.from(QueueSettings as qs)).map(QueueSettings(qs.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(QueueSettings as qs)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[QueueSettings] = {
    withSQL {
      select.from(QueueSettings as qs).where.append(where)
    }.map(QueueSettings(qs.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[QueueSettings] = {
    withSQL {
      select.from(QueueSettings as qs).where.append(where)
    }.map(QueueSettings(qs.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(QueueSettings as qs).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    siteId: Int,
    name: String,
    config: Option[String] = None,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime)(implicit session: DBSession = autoSession): QueueSettings = {
    val generatedKey = withSQL {
      insert.into(QueueSettings).namedValues(
        column.siteId -> siteId,
        column.name -> name,
        column.config -> config,
        column.createdAt -> createdAt,
        column.updatedAt -> updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    QueueSettings(
      id = generatedKey.toInt,
      siteId = siteId,
      name = name,
      config = config,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: collection.Seq[QueueSettings])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("siteId") -> entity.siteId,
        Symbol("name") -> entity.name,
        Symbol("config") -> entity.config,
        Symbol("createdAt") -> entity.createdAt,
        Symbol("updatedAt") -> entity.updatedAt))
    SQL("""insert into queue_settings(
      site_id,
      name,
      config,
      created_at,
      updated_at
    ) values (
      {siteId},
      {name},
      {config},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: QueueSettings)(implicit session: DBSession = autoSession): QueueSettings = {
    withSQL {
      update(QueueSettings).set(
        column.id -> entity.id,
        column.siteId -> entity.siteId,
        column.name -> entity.name,
        column.config -> entity.config,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: QueueSettings)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(QueueSettings).where.eq(column.id, entity.id) }.update.apply()
  }

}
