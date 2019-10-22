package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class DelayedSessionAttempts(
  id: Long,
  dependentSessionId: Option[Long] = None,
  nextRunTime: Long,
  updatedAt: ZonedDateTime) {

  def save()(implicit session: DBSession = DelayedSessionAttempts.autoSession): DelayedSessionAttempts = DelayedSessionAttempts.save(this)(session)

  def destroy()(implicit session: DBSession = DelayedSessionAttempts.autoSession): Int = DelayedSessionAttempts.destroy(this)(session)

}


object DelayedSessionAttempts extends SQLSyntaxSupport[DelayedSessionAttempts] {

  override val tableName = "delayed_session_attempts"

  override val columns = Seq("id", "dependent_session_id", "next_run_time", "updated_at")

  def apply(dsa: SyntaxProvider[DelayedSessionAttempts])(rs: WrappedResultSet): DelayedSessionAttempts = apply(dsa.resultName)(rs)
  def apply(dsa: ResultName[DelayedSessionAttempts])(rs: WrappedResultSet): DelayedSessionAttempts = new DelayedSessionAttempts(
    id = rs.get(dsa.id),
    dependentSessionId = rs.get(dsa.dependentSessionId),
    nextRunTime = rs.get(dsa.nextRunTime),
    updatedAt = rs.get(dsa.updatedAt)
  )

  val dsa = DelayedSessionAttempts.syntax("dsa")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[DelayedSessionAttempts] = {
    withSQL {
      select.from(DelayedSessionAttempts as dsa).where.eq(dsa.id, id)
    }.map(DelayedSessionAttempts(dsa.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[DelayedSessionAttempts] = {
    withSQL(select.from(DelayedSessionAttempts as dsa)).map(DelayedSessionAttempts(dsa.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(DelayedSessionAttempts as dsa)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[DelayedSessionAttempts] = {
    withSQL {
      select.from(DelayedSessionAttempts as dsa).where.append(where)
    }.map(DelayedSessionAttempts(dsa.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[DelayedSessionAttempts] = {
    withSQL {
      select.from(DelayedSessionAttempts as dsa).where.append(where)
    }.map(DelayedSessionAttempts(dsa.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(DelayedSessionAttempts as dsa).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    id: Long,
    dependentSessionId: Option[Long] = None,
    nextRunTime: Long,
    updatedAt: ZonedDateTime)(implicit session: DBSession = autoSession): DelayedSessionAttempts = {
    withSQL {
      insert.into(DelayedSessionAttempts).namedValues(
        column.id -> id,
        column.dependentSessionId -> dependentSessionId,
        column.nextRunTime -> nextRunTime,
        column.updatedAt -> updatedAt
      )
    }.update.apply()

    DelayedSessionAttempts(
      id = id,
      dependentSessionId = dependentSessionId,
      nextRunTime = nextRunTime,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: collection.Seq[DelayedSessionAttempts])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("id") -> entity.id,
        Symbol("dependentSessionId") -> entity.dependentSessionId,
        Symbol("nextRunTime") -> entity.nextRunTime,
        Symbol("updatedAt") -> entity.updatedAt))
    SQL("""insert into delayed_session_attempts(
      id,
      dependent_session_id,
      next_run_time,
      updated_at
    ) values (
      {id},
      {dependentSessionId},
      {nextRunTime},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: DelayedSessionAttempts)(implicit session: DBSession = autoSession): DelayedSessionAttempts = {
    withSQL {
      update(DelayedSessionAttempts).set(
        column.id -> entity.id,
        column.dependentSessionId -> entity.dependentSessionId,
        column.nextRunTime -> entity.nextRunTime,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: DelayedSessionAttempts)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(DelayedSessionAttempts).where.eq(column.id, entity.id) }.update.apply()
  }

}
