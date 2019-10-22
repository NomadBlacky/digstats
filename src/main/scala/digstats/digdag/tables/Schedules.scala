package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class Schedules(
  id: Int,
  projectId: Int,
  workflowDefinitionId: Long,
  nextRunTime: Long,
  nextScheduleTime: Long,
  lastSessionTime: Option[Long] = None,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime,
  disabledAt: Option[ZonedDateTime] = None) {

  def save()(implicit session: DBSession = Schedules.autoSession): Schedules = Schedules.save(this)(session)

  def destroy()(implicit session: DBSession = Schedules.autoSession): Int = Schedules.destroy(this)(session)

}


object Schedules extends SQLSyntaxSupport[Schedules] {

  override val tableName = "schedules"

  override val columns = Seq("id", "project_id", "workflow_definition_id", "next_run_time", "next_schedule_time", "last_session_time", "created_at", "updated_at", "disabled_at")

  def apply(s: SyntaxProvider[Schedules])(rs: WrappedResultSet): Schedules = apply(s.resultName)(rs)
  def apply(s: ResultName[Schedules])(rs: WrappedResultSet): Schedules = new Schedules(
    id = rs.get(s.id),
    projectId = rs.get(s.projectId),
    workflowDefinitionId = rs.get(s.workflowDefinitionId),
    nextRunTime = rs.get(s.nextRunTime),
    nextScheduleTime = rs.get(s.nextScheduleTime),
    lastSessionTime = rs.get(s.lastSessionTime),
    createdAt = rs.get(s.createdAt),
    updatedAt = rs.get(s.updatedAt),
    disabledAt = rs.get(s.disabledAt)
  )

  val s = Schedules.syntax("s")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Schedules] = {
    withSQL {
      select.from(Schedules as s).where.eq(s.id, id)
    }.map(Schedules(s.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Schedules] = {
    withSQL(select.from(Schedules as s)).map(Schedules(s.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Schedules as s)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Schedules] = {
    withSQL {
      select.from(Schedules as s).where.append(where)
    }.map(Schedules(s.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Schedules] = {
    withSQL {
      select.from(Schedules as s).where.append(where)
    }.map(Schedules(s.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Schedules as s).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    projectId: Int,
    workflowDefinitionId: Long,
    nextRunTime: Long,
    nextScheduleTime: Long,
    lastSessionTime: Option[Long] = None,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime,
    disabledAt: Option[ZonedDateTime] = None)(implicit session: DBSession = autoSession): Schedules = {
    val generatedKey = withSQL {
      insert.into(Schedules).namedValues(
        column.projectId -> projectId,
        column.workflowDefinitionId -> workflowDefinitionId,
        column.nextRunTime -> nextRunTime,
        column.nextScheduleTime -> nextScheduleTime,
        column.lastSessionTime -> lastSessionTime,
        column.createdAt -> createdAt,
        column.updatedAt -> updatedAt,
        column.disabledAt -> disabledAt
      )
    }.updateAndReturnGeneratedKey.apply()

    Schedules(
      id = generatedKey.toInt,
      projectId = projectId,
      workflowDefinitionId = workflowDefinitionId,
      nextRunTime = nextRunTime,
      nextScheduleTime = nextScheduleTime,
      lastSessionTime = lastSessionTime,
      createdAt = createdAt,
      updatedAt = updatedAt,
      disabledAt = disabledAt)
  }

  def batchInsert(entities: collection.Seq[Schedules])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("projectId") -> entity.projectId,
        Symbol("workflowDefinitionId") -> entity.workflowDefinitionId,
        Symbol("nextRunTime") -> entity.nextRunTime,
        Symbol("nextScheduleTime") -> entity.nextScheduleTime,
        Symbol("lastSessionTime") -> entity.lastSessionTime,
        Symbol("createdAt") -> entity.createdAt,
        Symbol("updatedAt") -> entity.updatedAt,
        Symbol("disabledAt") -> entity.disabledAt))
    SQL("""insert into schedules(
      project_id,
      workflow_definition_id,
      next_run_time,
      next_schedule_time,
      last_session_time,
      created_at,
      updated_at,
      disabled_at
    ) values (
      {projectId},
      {workflowDefinitionId},
      {nextRunTime},
      {nextScheduleTime},
      {lastSessionTime},
      {createdAt},
      {updatedAt},
      {disabledAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Schedules)(implicit session: DBSession = autoSession): Schedules = {
    withSQL {
      update(Schedules).set(
        column.id -> entity.id,
        column.projectId -> entity.projectId,
        column.workflowDefinitionId -> entity.workflowDefinitionId,
        column.nextRunTime -> entity.nextRunTime,
        column.nextScheduleTime -> entity.nextScheduleTime,
        column.lastSessionTime -> entity.lastSessionTime,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt,
        column.disabledAt -> entity.disabledAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Schedules)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Schedules).where.eq(column.id, entity.id) }.update.apply()
  }

}
