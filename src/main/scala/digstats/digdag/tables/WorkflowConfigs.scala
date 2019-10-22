package digstats.digdag.tables

import scalikejdbc._

case class WorkflowConfigs(
  id: Int,
  projectId: Int,
  configDigest: Long,
  timezone: String,
  config: String) {

  def save()(implicit session: DBSession = WorkflowConfigs.autoSession): WorkflowConfigs = WorkflowConfigs.save(this)(session)

  def destroy()(implicit session: DBSession = WorkflowConfigs.autoSession): Int = WorkflowConfigs.destroy(this)(session)

}


object WorkflowConfigs extends SQLSyntaxSupport[WorkflowConfigs] {

  override val tableName = "workflow_configs"

  override val columns = Seq("id", "project_id", "config_digest", "timezone", "config")

  def apply(wc: SyntaxProvider[WorkflowConfigs])(rs: WrappedResultSet): WorkflowConfigs = apply(wc.resultName)(rs)
  def apply(wc: ResultName[WorkflowConfigs])(rs: WrappedResultSet): WorkflowConfigs = new WorkflowConfigs(
    id = rs.get(wc.id),
    projectId = rs.get(wc.projectId),
    configDigest = rs.get(wc.configDigest),
    timezone = rs.get(wc.timezone),
    config = rs.get(wc.config)
  )

  val wc = WorkflowConfigs.syntax("wc")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[WorkflowConfigs] = {
    withSQL {
      select.from(WorkflowConfigs as wc).where.eq(wc.id, id)
    }.map(WorkflowConfigs(wc.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[WorkflowConfigs] = {
    withSQL(select.from(WorkflowConfigs as wc)).map(WorkflowConfigs(wc.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(WorkflowConfigs as wc)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[WorkflowConfigs] = {
    withSQL {
      select.from(WorkflowConfigs as wc).where.append(where)
    }.map(WorkflowConfigs(wc.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[WorkflowConfigs] = {
    withSQL {
      select.from(WorkflowConfigs as wc).where.append(where)
    }.map(WorkflowConfigs(wc.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(WorkflowConfigs as wc).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    projectId: Int,
    configDigest: Long,
    timezone: String,
    config: String)(implicit session: DBSession = autoSession): WorkflowConfigs = {
    val generatedKey = withSQL {
      insert.into(WorkflowConfigs).namedValues(
        column.projectId -> projectId,
        column.configDigest -> configDigest,
        column.timezone -> timezone,
        column.config -> config
      )
    }.updateAndReturnGeneratedKey.apply()

    WorkflowConfigs(
      id = generatedKey.toInt,
      projectId = projectId,
      configDigest = configDigest,
      timezone = timezone,
      config = config)
  }

  def batchInsert(entities: collection.Seq[WorkflowConfigs])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("projectId") -> entity.projectId,
        Symbol("configDigest") -> entity.configDigest,
        Symbol("timezone") -> entity.timezone,
        Symbol("config") -> entity.config))
    SQL("""insert into workflow_configs(
      project_id,
      config_digest,
      timezone,
      config
    ) values (
      {projectId},
      {configDigest},
      {timezone},
      {config}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: WorkflowConfigs)(implicit session: DBSession = autoSession): WorkflowConfigs = {
    withSQL {
      update(WorkflowConfigs).set(
        column.id -> entity.id,
        column.projectId -> entity.projectId,
        column.configDigest -> entity.configDigest,
        column.timezone -> entity.timezone,
        column.config -> entity.config
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: WorkflowConfigs)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(WorkflowConfigs).where.eq(column.id, entity.id) }.update.apply()
  }

}
