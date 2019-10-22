package digstats.digdag.tables

import scalikejdbc._

case class TaskStateDetails(
  id: Long,
  subtaskConfig: Option[String] = None,
  exportParams: Option[String] = None,
  storeParams: Option[String] = None,
  report: Option[String] = None,
  error: Option[String] = None,
  resetStoreParams: Option[String] = None) {

  def save()(implicit session: DBSession = TaskStateDetails.autoSession): TaskStateDetails = TaskStateDetails.save(this)(session)

  def destroy()(implicit session: DBSession = TaskStateDetails.autoSession): Int = TaskStateDetails.destroy(this)(session)

}


object TaskStateDetails extends SQLSyntaxSupport[TaskStateDetails] {

  override val tableName = "task_state_details"

  override val columns = Seq("id", "subtask_config", "export_params", "store_params", "report", "error", "reset_store_params")

  def apply(tsd: SyntaxProvider[TaskStateDetails])(rs: WrappedResultSet): TaskStateDetails = apply(tsd.resultName)(rs)
  def apply(tsd: ResultName[TaskStateDetails])(rs: WrappedResultSet): TaskStateDetails = new TaskStateDetails(
    id = rs.get(tsd.id),
    subtaskConfig = rs.get(tsd.subtaskConfig),
    exportParams = rs.get(tsd.exportParams),
    storeParams = rs.get(tsd.storeParams),
    report = rs.get(tsd.report),
    error = rs.get(tsd.error),
    resetStoreParams = rs.get(tsd.resetStoreParams)
  )

  val tsd = TaskStateDetails.syntax("tsd")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[TaskStateDetails] = {
    withSQL {
      select.from(TaskStateDetails as tsd).where.eq(tsd.id, id)
    }.map(TaskStateDetails(tsd.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[TaskStateDetails] = {
    withSQL(select.from(TaskStateDetails as tsd)).map(TaskStateDetails(tsd.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(TaskStateDetails as tsd)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[TaskStateDetails] = {
    withSQL {
      select.from(TaskStateDetails as tsd).where.append(where)
    }.map(TaskStateDetails(tsd.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[TaskStateDetails] = {
    withSQL {
      select.from(TaskStateDetails as tsd).where.append(where)
    }.map(TaskStateDetails(tsd.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(TaskStateDetails as tsd).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    id: Long,
    subtaskConfig: Option[String] = None,
    exportParams: Option[String] = None,
    storeParams: Option[String] = None,
    report: Option[String] = None,
    error: Option[String] = None,
    resetStoreParams: Option[String] = None)(implicit session: DBSession = autoSession): TaskStateDetails = {
    withSQL {
      insert.into(TaskStateDetails).namedValues(
        column.id -> id,
        column.subtaskConfig -> subtaskConfig,
        column.exportParams -> exportParams,
        column.storeParams -> storeParams,
        column.report -> report,
        column.error -> error,
        column.resetStoreParams -> resetStoreParams
      )
    }.update.apply()

    TaskStateDetails(
      id = id,
      subtaskConfig = subtaskConfig,
      exportParams = exportParams,
      storeParams = storeParams,
      report = report,
      error = error,
      resetStoreParams = resetStoreParams)
  }

  def batchInsert(entities: collection.Seq[TaskStateDetails])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("id") -> entity.id,
        Symbol("subtaskConfig") -> entity.subtaskConfig,
        Symbol("exportParams") -> entity.exportParams,
        Symbol("storeParams") -> entity.storeParams,
        Symbol("report") -> entity.report,
        Symbol("error") -> entity.error,
        Symbol("resetStoreParams") -> entity.resetStoreParams))
    SQL("""insert into task_state_details(
      id,
      subtask_config,
      export_params,
      store_params,
      report,
      error,
      reset_store_params
    ) values (
      {id},
      {subtaskConfig},
      {exportParams},
      {storeParams},
      {report},
      {error},
      {resetStoreParams}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: TaskStateDetails)(implicit session: DBSession = autoSession): TaskStateDetails = {
    withSQL {
      update(TaskStateDetails).set(
        column.id -> entity.id,
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

  def destroy(entity: TaskStateDetails)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(TaskStateDetails).where.eq(column.id, entity.id) }.update.apply()
  }

}
