package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class Sessions(
  id: Long,
  projectId: Int,
  workflowName: String,
  sessionTime: Long,
  sessionUuid: Any,
  lastAttemptId: Option[Long] = None,
  lastAttemptCreatedAt: Option[ZonedDateTime] = None) {

  def save()(implicit session: DBSession = Sessions.autoSession): Sessions = Sessions.save(this)(session)

  def destroy()(implicit session: DBSession = Sessions.autoSession): Int = Sessions.destroy(this)(session)

}


object Sessions extends SQLSyntaxSupport[Sessions] {

  override val tableName = "sessions"

  override val columns = Seq("id", "project_id", "workflow_name", "session_time", "session_uuid", "last_attempt_id", "last_attempt_created_at")

  def apply(s: SyntaxProvider[Sessions])(rs: WrappedResultSet): Sessions = apply(s.resultName)(rs)
  def apply(s: ResultName[Sessions])(rs: WrappedResultSet): Sessions = new Sessions(
    id = rs.get(s.id),
    projectId = rs.get(s.projectId),
    workflowName = rs.get(s.workflowName),
    sessionTime = rs.get(s.sessionTime),
    sessionUuid = rs.any(s.sessionUuid),
    lastAttemptId = rs.get(s.lastAttemptId),
    lastAttemptCreatedAt = rs.get(s.lastAttemptCreatedAt)
  )

  val s = Sessions.syntax("s")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Sessions] = {
    withSQL {
      select.from(Sessions as s).where.eq(s.id, id)
    }.map(Sessions(s.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Sessions] = {
    withSQL(select.from(Sessions as s)).map(Sessions(s.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Sessions as s)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Sessions] = {
    withSQL {
      select.from(Sessions as s).where.append(where)
    }.map(Sessions(s.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Sessions] = {
    withSQL {
      select.from(Sessions as s).where.append(where)
    }.map(Sessions(s.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Sessions as s).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    projectId: Int,
    workflowName: String,
    sessionTime: Long,
    sessionUuid: Any,
    lastAttemptId: Option[Long] = None,
    lastAttemptCreatedAt: Option[ZonedDateTime] = None)(implicit session: DBSession = autoSession): Sessions = {
    val generatedKey = withSQL {
      insert.into(Sessions).namedValues(
        column.projectId -> projectId,
        column.workflowName -> workflowName,
        column.sessionTime -> sessionTime,
        (column.sessionUuid, ParameterBinder(sessionUuid, (ps, i) => ps.setObject(i, sessionUuid))),
        column.lastAttemptId -> lastAttemptId,
        column.lastAttemptCreatedAt -> lastAttemptCreatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    Sessions(
      id = generatedKey,
      projectId = projectId,
      workflowName = workflowName,
      sessionTime = sessionTime,
      sessionUuid = sessionUuid,
      lastAttemptId = lastAttemptId,
      lastAttemptCreatedAt = lastAttemptCreatedAt)
  }

  def batchInsert(entities: collection.Seq[Sessions])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("projectId") -> entity.projectId,
        Symbol("workflowName") -> entity.workflowName,
        Symbol("sessionTime") -> entity.sessionTime,
        Symbol("sessionUuid") -> entity.sessionUuid,
        Symbol("lastAttemptId") -> entity.lastAttemptId,
        Symbol("lastAttemptCreatedAt") -> entity.lastAttemptCreatedAt))
    SQL("""insert into sessions(
      project_id,
      workflow_name,
      session_time,
      session_uuid,
      last_attempt_id,
      last_attempt_created_at
    ) values (
      {projectId},
      {workflowName},
      {sessionTime},
      {sessionUuid},
      {lastAttemptId},
      {lastAttemptCreatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Sessions)(implicit session: DBSession = autoSession): Sessions = {
    withSQL {
      update(Sessions).set(
        column.id -> entity.id,
        column.projectId -> entity.projectId,
        column.workflowName -> entity.workflowName,
        column.sessionTime -> entity.sessionTime,
        (column.sessionUuid, ParameterBinder(entity.sessionUuid, (ps, i) => ps.setObject(i, entity.sessionUuid))),
        column.lastAttemptId -> entity.lastAttemptId,
        column.lastAttemptCreatedAt -> entity.lastAttemptCreatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Sessions)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Sessions).where.eq(column.id, entity.id) }.update.apply()
  }

}
