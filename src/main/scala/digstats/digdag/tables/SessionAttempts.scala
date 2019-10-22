package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class SessionAttempts(
  id: Long,
  sessionId: Long,
  siteId: Int,
  projectId: Int,
  attemptName: String,
  workflowDefinitionId: Option[Long] = None,
  stateFlags: Short,
  timezone: String,
  params: Option[String] = None,
  createdAt: ZonedDateTime,
  finishedAt: Option[ZonedDateTime] = None,
  index: Int) {

  def save()(implicit session: DBSession = SessionAttempts.autoSession): SessionAttempts = SessionAttempts.save(this)(session)

  def destroy()(implicit session: DBSession = SessionAttempts.autoSession): Int = SessionAttempts.destroy(this)(session)

}


object SessionAttempts extends SQLSyntaxSupport[SessionAttempts] {

  override val tableName = "session_attempts"

  override val columns = Seq("id", "session_id", "site_id", "project_id", "attempt_name", "workflow_definition_id", "state_flags", "timezone", "params", "created_at", "finished_at", "index")

  def apply(sa: SyntaxProvider[SessionAttempts])(rs: WrappedResultSet): SessionAttempts = apply(sa.resultName)(rs)
  def apply(sa: ResultName[SessionAttempts])(rs: WrappedResultSet): SessionAttempts = new SessionAttempts(
    id = rs.get(sa.id),
    sessionId = rs.get(sa.sessionId),
    siteId = rs.get(sa.siteId),
    projectId = rs.get(sa.projectId),
    attemptName = rs.get(sa.attemptName),
    workflowDefinitionId = rs.get(sa.workflowDefinitionId),
    stateFlags = rs.get(sa.stateFlags),
    timezone = rs.get(sa.timezone),
    params = rs.get(sa.params),
    createdAt = rs.get(sa.createdAt),
    finishedAt = rs.get(sa.finishedAt),
    index = rs.get(sa.index)
  )

  val sa = SessionAttempts.syntax("sa")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[SessionAttempts] = {
    withSQL {
      select.from(SessionAttempts as sa).where.eq(sa.id, id)
    }.map(SessionAttempts(sa.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[SessionAttempts] = {
    withSQL(select.from(SessionAttempts as sa)).map(SessionAttempts(sa.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(SessionAttempts as sa)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[SessionAttempts] = {
    withSQL {
      select.from(SessionAttempts as sa).where.append(where)
    }.map(SessionAttempts(sa.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[SessionAttempts] = {
    withSQL {
      select.from(SessionAttempts as sa).where.append(where)
    }.map(SessionAttempts(sa.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(SessionAttempts as sa).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    sessionId: Long,
    siteId: Int,
    projectId: Int,
    attemptName: String,
    workflowDefinitionId: Option[Long] = None,
    stateFlags: Short,
    timezone: String,
    params: Option[String] = None,
    createdAt: ZonedDateTime,
    finishedAt: Option[ZonedDateTime] = None,
    index: Int)(implicit session: DBSession = autoSession): SessionAttempts = {
    val generatedKey = withSQL {
      insert.into(SessionAttempts).namedValues(
        column.sessionId -> sessionId,
        column.siteId -> siteId,
        column.projectId -> projectId,
        column.attemptName -> attemptName,
        column.workflowDefinitionId -> workflowDefinitionId,
        column.stateFlags -> stateFlags,
        column.timezone -> timezone,
        column.params -> params,
        column.createdAt -> createdAt,
        column.finishedAt -> finishedAt,
        column.index -> index
      )
    }.updateAndReturnGeneratedKey.apply()

    SessionAttempts(
      id = generatedKey,
      sessionId = sessionId,
      siteId = siteId,
      projectId = projectId,
      attemptName = attemptName,
      workflowDefinitionId = workflowDefinitionId,
      stateFlags = stateFlags,
      timezone = timezone,
      params = params,
      createdAt = createdAt,
      finishedAt = finishedAt,
      index = index)
  }

  def batchInsert(entities: collection.Seq[SessionAttempts])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("sessionId") -> entity.sessionId,
        Symbol("siteId") -> entity.siteId,
        Symbol("projectId") -> entity.projectId,
        Symbol("attemptName") -> entity.attemptName,
        Symbol("workflowDefinitionId") -> entity.workflowDefinitionId,
        Symbol("stateFlags") -> entity.stateFlags,
        Symbol("timezone") -> entity.timezone,
        Symbol("params") -> entity.params,
        Symbol("createdAt") -> entity.createdAt,
        Symbol("finishedAt") -> entity.finishedAt,
        Symbol("index") -> entity.index))
    SQL("""insert into session_attempts(
      session_id,
      site_id,
      project_id,
      attempt_name,
      workflow_definition_id,
      state_flags,
      timezone,
      params,
      created_at,
      finished_at,
      index
    ) values (
      {sessionId},
      {siteId},
      {projectId},
      {attemptName},
      {workflowDefinitionId},
      {stateFlags},
      {timezone},
      {params},
      {createdAt},
      {finishedAt},
      {index}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: SessionAttempts)(implicit session: DBSession = autoSession): SessionAttempts = {
    withSQL {
      update(SessionAttempts).set(
        column.id -> entity.id,
        column.sessionId -> entity.sessionId,
        column.siteId -> entity.siteId,
        column.projectId -> entity.projectId,
        column.attemptName -> entity.attemptName,
        column.workflowDefinitionId -> entity.workflowDefinitionId,
        column.stateFlags -> entity.stateFlags,
        column.timezone -> entity.timezone,
        column.params -> entity.params,
        column.createdAt -> entity.createdAt,
        column.finishedAt -> entity.finishedAt,
        column.index -> entity.index
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: SessionAttempts)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(SessionAttempts).where.eq(column.id, entity.id) }.update.apply()
  }

}
