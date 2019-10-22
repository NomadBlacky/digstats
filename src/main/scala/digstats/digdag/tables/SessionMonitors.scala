package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class SessionMonitors(
  id: Long,
  attemptId: Long,
  nextRunTime: Long,
  `type`: String,
  config: Option[String] = None,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime) {

  def save()(implicit session: DBSession = SessionMonitors.autoSession): SessionMonitors = SessionMonitors.save(this)(session)

  def destroy()(implicit session: DBSession = SessionMonitors.autoSession): Int = SessionMonitors.destroy(this)(session)

}


object SessionMonitors extends SQLSyntaxSupport[SessionMonitors] {

  override val tableName = "session_monitors"

  override val columns = Seq("id", "attempt_id", "next_run_time", "type", "config", "created_at", "updated_at")

  def apply(sm: SyntaxProvider[SessionMonitors])(rs: WrappedResultSet): SessionMonitors = apply(sm.resultName)(rs)
  def apply(sm: ResultName[SessionMonitors])(rs: WrappedResultSet): SessionMonitors = new SessionMonitors(
    id = rs.get(sm.id),
    attemptId = rs.get(sm.attemptId),
    nextRunTime = rs.get(sm.nextRunTime),
    `type` = rs.get(sm.`type`),
    config = rs.get(sm.config),
    createdAt = rs.get(sm.createdAt),
    updatedAt = rs.get(sm.updatedAt)
  )

  val sm = SessionMonitors.syntax("sm")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[SessionMonitors] = {
    withSQL {
      select.from(SessionMonitors as sm).where.eq(sm.id, id)
    }.map(SessionMonitors(sm.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[SessionMonitors] = {
    withSQL(select.from(SessionMonitors as sm)).map(SessionMonitors(sm.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(SessionMonitors as sm)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[SessionMonitors] = {
    withSQL {
      select.from(SessionMonitors as sm).where.append(where)
    }.map(SessionMonitors(sm.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[SessionMonitors] = {
    withSQL {
      select.from(SessionMonitors as sm).where.append(where)
    }.map(SessionMonitors(sm.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(SessionMonitors as sm).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    attemptId: Long,
    nextRunTime: Long,
    `type`: String,
    config: Option[String] = None,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime)(implicit session: DBSession = autoSession): SessionMonitors = {
    val generatedKey = withSQL {
      insert.into(SessionMonitors).namedValues(
        column.attemptId -> attemptId,
        column.nextRunTime -> nextRunTime,
        column.`type` -> `type`,
        column.config -> config,
        column.createdAt -> createdAt,
        column.updatedAt -> updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    SessionMonitors(
      id = generatedKey,
      attemptId = attemptId,
      nextRunTime = nextRunTime,
      `type` = `type`,
      config = config,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: collection.Seq[SessionMonitors])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("attemptId") -> entity.attemptId,
        Symbol("nextRunTime") -> entity.nextRunTime,
        Symbol("type") -> entity.`type`,
        Symbol("config") -> entity.config,
        Symbol("createdAt") -> entity.createdAt,
        Symbol("updatedAt") -> entity.updatedAt))
    SQL("""insert into session_monitors(
      attempt_id,
      next_run_time,
      type,
      config,
      created_at,
      updated_at
    ) values (
      {attemptId},
      {nextRunTime},
      {type},
      {config},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: SessionMonitors)(implicit session: DBSession = autoSession): SessionMonitors = {
    withSQL {
      update(SessionMonitors).set(
        column.id -> entity.id,
        column.attemptId -> entity.attemptId,
        column.nextRunTime -> entity.nextRunTime,
        column.`type` -> entity.`type`,
        column.config -> entity.config,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: SessionMonitors)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(SessionMonitors).where.eq(column.id, entity.id) }.update.apply()
  }

}
