package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class ResumingTasks(
  id: Long,
  attemptId: Long,
  sourceTaskId: Long,
  fullName: String,
  updatedAt: ZonedDateTime,
  localConfig: Option[String] = None,
  exportConfig: Option[String] = None,
  subtaskConfig: Option[String] = None,
  exportParams: Option[String] = None,
  storeParams: Option[String] = None,
  report: Option[String] = None,
  error: Option[String] = None,
  resetStoreParams: Option[String] = None) {

  def save()(implicit session: DBSession = ResumingTasks.autoSession): ResumingTasks = ResumingTasks.save(this)(session)

  def destroy()(implicit session: DBSession = ResumingTasks.autoSession): Int = ResumingTasks.destroy(this)(session)

}


object ResumingTasks extends SQLSyntaxSupport[ResumingTasks] {

  override val tableName = "resuming_tasks"

  override val columns = Seq("id", "attempt_id", "source_task_id", "full_name", "updated_at", "local_config", "export_config", "subtask_config", "export_params", "store_params", "report", "error", "reset_store_params")

  def apply(rt: SyntaxProvider[ResumingTasks])(rs: WrappedResultSet): ResumingTasks = apply(rt.resultName)(rs)
  def apply(rt: ResultName[ResumingTasks])(rs: WrappedResultSet): ResumingTasks = new ResumingTasks(
    id = rs.get(rt.id),
    attemptId = rs.get(rt.attemptId),
    sourceTaskId = rs.get(rt.sourceTaskId),
    fullName = rs.get(rt.fullName),
    updatedAt = rs.get(rt.updatedAt),
    localConfig = rs.get(rt.localConfig),
    exportConfig = rs.get(rt.exportConfig),
    subtaskConfig = rs.get(rt.subtaskConfig),
    exportParams = rs.get(rt.exportParams),
    storeParams = rs.get(rt.storeParams),
    report = rs.get(rt.report),
    error = rs.get(rt.error),
    resetStoreParams = rs.get(rt.resetStoreParams)
  )

  val rt = ResumingTasks.syntax("rt")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[ResumingTasks] = {
    withSQL {
      select.from(ResumingTasks as rt).where.eq(rt.id, id)
    }.map(ResumingTasks(rt.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[ResumingTasks] = {
    withSQL(select.from(ResumingTasks as rt)).map(ResumingTasks(rt.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(ResumingTasks as rt)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[ResumingTasks] = {
    withSQL {
      select.from(ResumingTasks as rt).where.append(where)
    }.map(ResumingTasks(rt.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[ResumingTasks] = {
    withSQL {
      select.from(ResumingTasks as rt).where.append(where)
    }.map(ResumingTasks(rt.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(ResumingTasks as rt).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    attemptId: Long,
    sourceTaskId: Long,
    fullName: String,
    updatedAt: ZonedDateTime,
    localConfig: Option[String] = None,
    exportConfig: Option[String] = None,
    subtaskConfig: Option[String] = None,
    exportParams: Option[String] = None,
    storeParams: Option[String] = None,
    report: Option[String] = None,
    error: Option[String] = None,
    resetStoreParams: Option[String] = None)(implicit session: DBSession = autoSession): ResumingTasks = {
    val generatedKey = withSQL {
      insert.into(ResumingTasks).namedValues(
        column.attemptId -> attemptId,
        column.sourceTaskId -> sourceTaskId,
        column.fullName -> fullName,
        column.updatedAt -> updatedAt,
        column.localConfig -> localConfig,
        column.exportConfig -> exportConfig,
        column.subtaskConfig -> subtaskConfig,
        column.exportParams -> exportParams,
        column.storeParams -> storeParams,
        column.report -> report,
        column.error -> error,
        column.resetStoreParams -> resetStoreParams
      )
    }.updateAndReturnGeneratedKey.apply()

    ResumingTasks(
      id = generatedKey,
      attemptId = attemptId,
      sourceTaskId = sourceTaskId,
      fullName = fullName,
      updatedAt = updatedAt,
      localConfig = localConfig,
      exportConfig = exportConfig,
      subtaskConfig = subtaskConfig,
      exportParams = exportParams,
      storeParams = storeParams,
      report = report,
      error = error,
      resetStoreParams = resetStoreParams)
  }

  def batchInsert(entities: collection.Seq[ResumingTasks])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("attemptId") -> entity.attemptId,
        Symbol("sourceTaskId") -> entity.sourceTaskId,
        Symbol("fullName") -> entity.fullName,
        Symbol("updatedAt") -> entity.updatedAt,
        Symbol("localConfig") -> entity.localConfig,
        Symbol("exportConfig") -> entity.exportConfig,
        Symbol("subtaskConfig") -> entity.subtaskConfig,
        Symbol("exportParams") -> entity.exportParams,
        Symbol("storeParams") -> entity.storeParams,
        Symbol("report") -> entity.report,
        Symbol("error") -> entity.error,
        Symbol("resetStoreParams") -> entity.resetStoreParams))
    SQL("""insert into resuming_tasks(
      attempt_id,
      source_task_id,
      full_name,
      updated_at,
      local_config,
      export_config,
      subtask_config,
      export_params,
      store_params,
      report,
      error,
      reset_store_params
    ) values (
      {attemptId},
      {sourceTaskId},
      {fullName},
      {updatedAt},
      {localConfig},
      {exportConfig},
      {subtaskConfig},
      {exportParams},
      {storeParams},
      {report},
      {error},
      {resetStoreParams}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: ResumingTasks)(implicit session: DBSession = autoSession): ResumingTasks = {
    withSQL {
      update(ResumingTasks).set(
        column.id -> entity.id,
        column.attemptId -> entity.attemptId,
        column.sourceTaskId -> entity.sourceTaskId,
        column.fullName -> entity.fullName,
        column.updatedAt -> entity.updatedAt,
        column.localConfig -> entity.localConfig,
        column.exportConfig -> entity.exportConfig,
        column.subtaskConfig -> entity.subtaskConfig,
        column.exportParams -> entity.exportParams,
        column.storeParams -> entity.storeParams,
        column.report -> entity.report,
        column.error -> entity.error,
        column.resetStoreParams -> entity.resetStoreParams
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: ResumingTasks)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(ResumingTasks).where.eq(column.id, entity.id) }.update.apply()
  }

}
