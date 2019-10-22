package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class QueuedTasks(
  id: Long,
  siteId: Int,
  queueId: Option[Int] = None,
  uniqueName: String,
  createdAt: ZonedDateTime,
  data: Option[Array[Byte]] = None) {

  def save()(implicit session: DBSession = QueuedTasks.autoSession): QueuedTasks = QueuedTasks.save(this)(session)

  def destroy()(implicit session: DBSession = QueuedTasks.autoSession): Int = QueuedTasks.destroy(this)(session)

}


object QueuedTasks extends SQLSyntaxSupport[QueuedTasks] {

  override val tableName = "queued_tasks"

  override val columns = Seq("id", "site_id", "queue_id", "unique_name", "created_at", "data")

  def apply(qt: SyntaxProvider[QueuedTasks])(rs: WrappedResultSet): QueuedTasks = apply(qt.resultName)(rs)
  def apply(qt: ResultName[QueuedTasks])(rs: WrappedResultSet): QueuedTasks = new QueuedTasks(
    id = rs.get(qt.id),
    siteId = rs.get(qt.siteId),
    queueId = rs.get(qt.queueId),
    uniqueName = rs.get(qt.uniqueName),
    createdAt = rs.get(qt.createdAt),
    data = rs.get(qt.data)
  )

  val qt = QueuedTasks.syntax("qt")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[QueuedTasks] = {
    withSQL {
      select.from(QueuedTasks as qt).where.eq(qt.id, id)
    }.map(QueuedTasks(qt.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[QueuedTasks] = {
    withSQL(select.from(QueuedTasks as qt)).map(QueuedTasks(qt.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(QueuedTasks as qt)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[QueuedTasks] = {
    withSQL {
      select.from(QueuedTasks as qt).where.append(where)
    }.map(QueuedTasks(qt.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[QueuedTasks] = {
    withSQL {
      select.from(QueuedTasks as qt).where.append(where)
    }.map(QueuedTasks(qt.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(QueuedTasks as qt).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    siteId: Int,
    queueId: Option[Int] = None,
    uniqueName: String,
    createdAt: ZonedDateTime,
    data: Option[Array[Byte]] = None)(implicit session: DBSession = autoSession): QueuedTasks = {
    val generatedKey = withSQL {
      insert.into(QueuedTasks).namedValues(
        column.siteId -> siteId,
        column.queueId -> queueId,
        column.uniqueName -> uniqueName,
        column.createdAt -> createdAt,
        column.data -> data
      )
    }.updateAndReturnGeneratedKey.apply()

    QueuedTasks(
      id = generatedKey,
      siteId = siteId,
      queueId = queueId,
      uniqueName = uniqueName,
      createdAt = createdAt,
      data = data)
  }

  def batchInsert(entities: collection.Seq[QueuedTasks])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("siteId") -> entity.siteId,
        Symbol("queueId") -> entity.queueId,
        Symbol("uniqueName") -> entity.uniqueName,
        Symbol("createdAt") -> entity.createdAt,
        Symbol("data") -> entity.data))
    SQL("""insert into queued_tasks(
      site_id,
      queue_id,
      unique_name,
      created_at,
      data
    ) values (
      {siteId},
      {queueId},
      {uniqueName},
      {createdAt},
      {data}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: QueuedTasks)(implicit session: DBSession = autoSession): QueuedTasks = {
    withSQL {
      update(QueuedTasks).set(
        column.id -> entity.id,
        column.siteId -> entity.siteId,
        column.queueId -> entity.queueId,
        column.uniqueName -> entity.uniqueName,
        column.createdAt -> entity.createdAt,
        column.data -> entity.data
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: QueuedTasks)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(QueuedTasks).where.eq(column.id, entity.id) }.update.apply()
  }

}
