package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class Tasks(
  id: Long,
  attemptId: Long,
  parentId: Option[Long] = None,
  taskType: Short,
  state: Short,
  stateFlags: Short,
  updatedAt: ZonedDateTime,
  retryAt: Option[ZonedDateTime] = None,
  stateParams: Option[String] = None,
  retryCount: Option[Int] = None,
  startedAt: Option[ZonedDateTime] = None) {

  def save()(implicit session: DBSession = Tasks.autoSession): Tasks = Tasks.save(this)(session)

  def destroy()(implicit session: DBSession = Tasks.autoSession): Int = Tasks.destroy(this)(session)

}


object Tasks extends SQLSyntaxSupport[Tasks] {

  override val tableName = "tasks"

  override val columns = Seq("id", "attempt_id", "parent_id", "task_type", "state", "state_flags", "updated_at", "retry_at", "state_params", "retry_count", "started_at")

  def apply(t: SyntaxProvider[Tasks])(rs: WrappedResultSet): Tasks = apply(t.resultName)(rs)
  def apply(t: ResultName[Tasks])(rs: WrappedResultSet): Tasks = new Tasks(
    id = rs.get(t.id),
    attemptId = rs.get(t.attemptId),
    parentId = rs.get(t.parentId),
    taskType = rs.get(t.taskType),
    state = rs.get(t.state),
    stateFlags = rs.get(t.stateFlags),
    updatedAt = rs.get(t.updatedAt),
    retryAt = rs.get(t.retryAt),
    stateParams = rs.get(t.stateParams),
    retryCount = rs.get(t.retryCount),
    startedAt = rs.get(t.startedAt)
  )

  val t = Tasks.syntax("t")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Tasks] = {
    withSQL {
      select.from(Tasks as t).where.eq(t.id, id)
    }.map(Tasks(t.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Tasks] = {
    withSQL(select.from(Tasks as t)).map(Tasks(t.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Tasks as t)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Tasks] = {
    withSQL {
      select.from(Tasks as t).where.append(where)
    }.map(Tasks(t.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Tasks] = {
    withSQL {
      select.from(Tasks as t).where.append(where)
    }.map(Tasks(t.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Tasks as t).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    attemptId: Long,
    parentId: Option[Long] = None,
    taskType: Short,
    state: Short,
    stateFlags: Short,
    updatedAt: ZonedDateTime,
    retryAt: Option[ZonedDateTime] = None,
    stateParams: Option[String] = None,
    retryCount: Option[Int] = None,
    startedAt: Option[ZonedDateTime] = None)(implicit session: DBSession = autoSession): Tasks = {
    val generatedKey = withSQL {
      insert.into(Tasks).namedValues(
        column.attemptId -> attemptId,
        column.parentId -> parentId,
        column.taskType -> taskType,
        column.state -> state,
        column.stateFlags -> stateFlags,
        column.updatedAt -> updatedAt,
        column.retryAt -> retryAt,
        column.stateParams -> stateParams,
        column.retryCount -> retryCount,
        column.startedAt -> startedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    Tasks(
      id = generatedKey,
      attemptId = attemptId,
      parentId = parentId,
      taskType = taskType,
      state = state,
      stateFlags = stateFlags,
      updatedAt = updatedAt,
      retryAt = retryAt,
      stateParams = stateParams,
      retryCount = retryCount,
      startedAt = startedAt)
  }

  def batchInsert(entities: collection.Seq[Tasks])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("attemptId") -> entity.attemptId,
        Symbol("parentId") -> entity.parentId,
        Symbol("taskType") -> entity.taskType,
        Symbol("state") -> entity.state,
        Symbol("stateFlags") -> entity.stateFlags,
        Symbol("updatedAt") -> entity.updatedAt,
        Symbol("retryAt") -> entity.retryAt,
        Symbol("stateParams") -> entity.stateParams,
        Symbol("retryCount") -> entity.retryCount,
        Symbol("startedAt") -> entity.startedAt))
    SQL("""insert into tasks(
      attempt_id,
      parent_id,
      task_type,
      state,
      state_flags,
      updated_at,
      retry_at,
      state_params,
      retry_count,
      started_at
    ) values (
      {attemptId},
      {parentId},
      {taskType},
      {state},
      {stateFlags},
      {updatedAt},
      {retryAt},
      {stateParams},
      {retryCount},
      {startedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Tasks)(implicit session: DBSession = autoSession): Tasks = {
    withSQL {
      update(Tasks).set(
        column.id -> entity.id,
        column.attemptId -> entity.attemptId,
        column.parentId -> entity.parentId,
        column.taskType -> entity.taskType,
        column.state -> entity.state,
        column.stateFlags -> entity.stateFlags,
        column.updatedAt -> entity.updatedAt,
        column.retryAt -> entity.retryAt,
        column.stateParams -> entity.stateParams,
        column.retryCount -> entity.retryCount,
        column.startedAt -> entity.startedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Tasks)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Tasks).where.eq(column.id, entity.id) }.update.apply()
  }

}
