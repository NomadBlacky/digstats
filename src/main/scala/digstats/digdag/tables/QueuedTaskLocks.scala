package digstats.digdag.tables

import scalikejdbc._

case class QueuedTaskLocks(
  id: Long,
  siteId: Option[Int] = None,
  queueId: Option[Int] = None,
  priority: Int,
  retryCount: Int,
  lockExpireTime: Option[Long] = None,
  lockAgentId: Option[String] = None) {

  def save()(implicit session: DBSession = QueuedTaskLocks.autoSession): QueuedTaskLocks = QueuedTaskLocks.save(this)(session)

  def destroy()(implicit session: DBSession = QueuedTaskLocks.autoSession): Int = QueuedTaskLocks.destroy(this)(session)

}


object QueuedTaskLocks extends SQLSyntaxSupport[QueuedTaskLocks] {

  override val tableName = "queued_task_locks"

  override val columns = Seq("id", "site_id", "queue_id", "priority", "retry_count", "lock_expire_time", "lock_agent_id")

  def apply(qtl: SyntaxProvider[QueuedTaskLocks])(rs: WrappedResultSet): QueuedTaskLocks = apply(qtl.resultName)(rs)
  def apply(qtl: ResultName[QueuedTaskLocks])(rs: WrappedResultSet): QueuedTaskLocks = new QueuedTaskLocks(
    id = rs.get(qtl.id),
    siteId = rs.get(qtl.siteId),
    queueId = rs.get(qtl.queueId),
    priority = rs.get(qtl.priority),
    retryCount = rs.get(qtl.retryCount),
    lockExpireTime = rs.get(qtl.lockExpireTime),
    lockAgentId = rs.get(qtl.lockAgentId)
  )

  val qtl = QueuedTaskLocks.syntax("qtl")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[QueuedTaskLocks] = {
    withSQL {
      select.from(QueuedTaskLocks as qtl).where.eq(qtl.id, id)
    }.map(QueuedTaskLocks(qtl.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[QueuedTaskLocks] = {
    withSQL(select.from(QueuedTaskLocks as qtl)).map(QueuedTaskLocks(qtl.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(QueuedTaskLocks as qtl)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[QueuedTaskLocks] = {
    withSQL {
      select.from(QueuedTaskLocks as qtl).where.append(where)
    }.map(QueuedTaskLocks(qtl.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[QueuedTaskLocks] = {
    withSQL {
      select.from(QueuedTaskLocks as qtl).where.append(where)
    }.map(QueuedTaskLocks(qtl.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(QueuedTaskLocks as qtl).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    siteId: Option[Int] = None,
    queueId: Option[Int] = None,
    priority: Int,
    retryCount: Int,
    lockExpireTime: Option[Long] = None,
    lockAgentId: Option[String] = None)(implicit session: DBSession = autoSession): QueuedTaskLocks = {
    val generatedKey = withSQL {
      insert.into(QueuedTaskLocks).namedValues(
        column.siteId -> siteId,
        column.queueId -> queueId,
        column.priority -> priority,
        column.retryCount -> retryCount,
        column.lockExpireTime -> lockExpireTime,
        column.lockAgentId -> lockAgentId
      )
    }.updateAndReturnGeneratedKey.apply()

    QueuedTaskLocks(
      id = generatedKey,
      siteId = siteId,
      queueId = queueId,
      priority = priority,
      retryCount = retryCount,
      lockExpireTime = lockExpireTime,
      lockAgentId = lockAgentId)
  }

  def batchInsert(entities: collection.Seq[QueuedTaskLocks])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("siteId") -> entity.siteId,
        Symbol("queueId") -> entity.queueId,
        Symbol("priority") -> entity.priority,
        Symbol("retryCount") -> entity.retryCount,
        Symbol("lockExpireTime") -> entity.lockExpireTime,
        Symbol("lockAgentId") -> entity.lockAgentId))
    SQL("""insert into queued_task_locks(
      site_id,
      queue_id,
      priority,
      retry_count,
      lock_expire_time,
      lock_agent_id
    ) values (
      {siteId},
      {queueId},
      {priority},
      {retryCount},
      {lockExpireTime},
      {lockAgentId}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: QueuedTaskLocks)(implicit session: DBSession = autoSession): QueuedTaskLocks = {
    withSQL {
      update(QueuedTaskLocks).set(
        column.id -> entity.id,
        column.siteId -> entity.siteId,
        column.queueId -> entity.queueId,
        column.priority -> entity.priority,
        column.retryCount -> entity.retryCount,
        column.lockExpireTime -> entity.lockExpireTime,
        column.lockAgentId -> entity.lockAgentId
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: QueuedTaskLocks)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(QueuedTaskLocks).where.eq(column.id, entity.id) }.update.apply()
  }

}
