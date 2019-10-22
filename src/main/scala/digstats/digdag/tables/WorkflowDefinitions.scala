package digstats.digdag.tables

import scalikejdbc._

case class WorkflowDefinitions(
  id: Long,
  configId: Int,
  revisionId: Int,
  name: String) {

  def save()(implicit session: DBSession = WorkflowDefinitions.autoSession): WorkflowDefinitions = WorkflowDefinitions.save(this)(session)

  def destroy()(implicit session: DBSession = WorkflowDefinitions.autoSession): Int = WorkflowDefinitions.destroy(this)(session)

}


object WorkflowDefinitions extends SQLSyntaxSupport[WorkflowDefinitions] {

  override val tableName = "workflow_definitions"

  override val columns = Seq("id", "config_id", "revision_id", "name")

  def apply(wd: SyntaxProvider[WorkflowDefinitions])(rs: WrappedResultSet): WorkflowDefinitions = apply(wd.resultName)(rs)
  def apply(wd: ResultName[WorkflowDefinitions])(rs: WrappedResultSet): WorkflowDefinitions = new WorkflowDefinitions(
    id = rs.get(wd.id),
    configId = rs.get(wd.configId),
    revisionId = rs.get(wd.revisionId),
    name = rs.get(wd.name)
  )

  val wd = WorkflowDefinitions.syntax("wd")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[WorkflowDefinitions] = {
    withSQL {
      select.from(WorkflowDefinitions as wd).where.eq(wd.id, id)
    }.map(WorkflowDefinitions(wd.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[WorkflowDefinitions] = {
    withSQL(select.from(WorkflowDefinitions as wd)).map(WorkflowDefinitions(wd.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(WorkflowDefinitions as wd)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[WorkflowDefinitions] = {
    withSQL {
      select.from(WorkflowDefinitions as wd).where.append(where)
    }.map(WorkflowDefinitions(wd.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[WorkflowDefinitions] = {
    withSQL {
      select.from(WorkflowDefinitions as wd).where.append(where)
    }.map(WorkflowDefinitions(wd.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(WorkflowDefinitions as wd).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    configId: Int,
    revisionId: Int,
    name: String)(implicit session: DBSession = autoSession): WorkflowDefinitions = {
    val generatedKey = withSQL {
      insert.into(WorkflowDefinitions).namedValues(
        column.configId -> configId,
        column.revisionId -> revisionId,
        column.name -> name
      )
    }.updateAndReturnGeneratedKey.apply()

    WorkflowDefinitions(
      id = generatedKey,
      configId = configId,
      revisionId = revisionId,
      name = name)
  }

  def batchInsert(entities: collection.Seq[WorkflowDefinitions])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("configId") -> entity.configId,
        Symbol("revisionId") -> entity.revisionId,
        Symbol("name") -> entity.name))
    SQL("""insert into workflow_definitions(
      config_id,
      revision_id,
      name
    ) values (
      {configId},
      {revisionId},
      {name}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: WorkflowDefinitions)(implicit session: DBSession = autoSession): WorkflowDefinitions = {
    withSQL {
      update(WorkflowDefinitions).set(
        column.id -> entity.id,
        column.configId -> entity.configId,
        column.revisionId -> entity.revisionId,
        column.name -> entity.name
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: WorkflowDefinitions)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(WorkflowDefinitions).where.eq(column.id, entity.id) }.update.apply()
  }

}
