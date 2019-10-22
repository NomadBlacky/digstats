package digstats.digdag.tables

import scalikejdbc._

case class TaskDetails(
  id: Long,
  fullName: String,
  localConfig: Option[String] = None,
  exportConfig: Option[String] = None,
  resumingTaskId: Option[Long] = None) {

  def save()(implicit session: DBSession = TaskDetails.autoSession): TaskDetails = TaskDetails.save(this)(session)

  def destroy()(implicit session: DBSession = TaskDetails.autoSession): Int = TaskDetails.destroy(this)(session)

}


object TaskDetails extends SQLSyntaxSupport[TaskDetails] {

  override val tableName = "task_details"

  override val columns = Seq("id", "full_name", "local_config", "export_config", "resuming_task_id")

  def apply(td: SyntaxProvider[TaskDetails])(rs: WrappedResultSet): TaskDetails = apply(td.resultName)(rs)
  def apply(td: ResultName[TaskDetails])(rs: WrappedResultSet): TaskDetails = new TaskDetails(
    id = rs.get(td.id),
    fullName = rs.get(td.fullName),
    localConfig = rs.get(td.localConfig),
    exportConfig = rs.get(td.exportConfig),
    resumingTaskId = rs.get(td.resumingTaskId)
  )

  val td = TaskDetails.syntax("td")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[TaskDetails] = {
    withSQL {
      select.from(TaskDetails as td).where.eq(td.id, id)
    }.map(TaskDetails(td.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[TaskDetails] = {
    withSQL(select.from(TaskDetails as td)).map(TaskDetails(td.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(TaskDetails as td)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[TaskDetails] = {
    withSQL {
      select.from(TaskDetails as td).where.append(where)
    }.map(TaskDetails(td.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[TaskDetails] = {
    withSQL {
      select.from(TaskDetails as td).where.append(where)
    }.map(TaskDetails(td.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(TaskDetails as td).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    id: Long,
    fullName: String,
    localConfig: Option[String] = None,
    exportConfig: Option[String] = None,
    resumingTaskId: Option[Long] = None)(implicit session: DBSession = autoSession): TaskDetails = {
    withSQL {
      insert.into(TaskDetails).namedValues(
        column.id -> id,
        column.fullName -> fullName,
        column.localConfig -> localConfig,
        column.exportConfig -> exportConfig,
        column.resumingTaskId -> resumingTaskId
      )
    }.update.apply()

    TaskDetails(
      id = id,
      fullName = fullName,
      localConfig = localConfig,
      exportConfig = exportConfig,
      resumingTaskId = resumingTaskId)
  }

  def batchInsert(entities: collection.Seq[TaskDetails])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("id") -> entity.id,
        Symbol("fullName") -> entity.fullName,
        Symbol("localConfig") -> entity.localConfig,
        Symbol("exportConfig") -> entity.exportConfig,
        Symbol("resumingTaskId") -> entity.resumingTaskId))
    SQL("""insert into task_details(
      id,
      full_name,
      local_config,
      export_config,
      resuming_task_id
    ) values (
      {id},
      {fullName},
      {localConfig},
      {exportConfig},
      {resumingTaskId}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: TaskDetails)(implicit session: DBSession = autoSession): TaskDetails = {
    withSQL {
      update(TaskDetails).set(
        column.id -> entity.id,
        column.fullName -> entity.fullName,
        column.localConfig -> entity.localConfig,
        column.exportConfig -> entity.exportConfig,
        column.resumingTaskId -> entity.resumingTaskId
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: TaskDetails)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(TaskDetails).where.eq(column.id, entity.id) }.update.apply()
  }

}
