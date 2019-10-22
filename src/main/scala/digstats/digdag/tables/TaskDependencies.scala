package digstats.digdag.tables

import scalikejdbc._

case class TaskDependencies(
  id: Long,
  upstreamId: Long,
  downstreamId: Long) {

  def save()(implicit session: DBSession = TaskDependencies.autoSession): TaskDependencies = TaskDependencies.save(this)(session)

  def destroy()(implicit session: DBSession = TaskDependencies.autoSession): Int = TaskDependencies.destroy(this)(session)

}


object TaskDependencies extends SQLSyntaxSupport[TaskDependencies] {

  override val tableName = "task_dependencies"

  override val columns = Seq("id", "upstream_id", "downstream_id")

  def apply(td: SyntaxProvider[TaskDependencies])(rs: WrappedResultSet): TaskDependencies = apply(td.resultName)(rs)
  def apply(td: ResultName[TaskDependencies])(rs: WrappedResultSet): TaskDependencies = new TaskDependencies(
    id = rs.get(td.id),
    upstreamId = rs.get(td.upstreamId),
    downstreamId = rs.get(td.downstreamId)
  )

  val td = TaskDependencies.syntax("td")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[TaskDependencies] = {
    withSQL {
      select.from(TaskDependencies as td).where.eq(td.id, id)
    }.map(TaskDependencies(td.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[TaskDependencies] = {
    withSQL(select.from(TaskDependencies as td)).map(TaskDependencies(td.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(TaskDependencies as td)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[TaskDependencies] = {
    withSQL {
      select.from(TaskDependencies as td).where.append(where)
    }.map(TaskDependencies(td.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[TaskDependencies] = {
    withSQL {
      select.from(TaskDependencies as td).where.append(where)
    }.map(TaskDependencies(td.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(TaskDependencies as td).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    upstreamId: Long,
    downstreamId: Long)(implicit session: DBSession = autoSession): TaskDependencies = {
    val generatedKey = withSQL {
      insert.into(TaskDependencies).namedValues(
        column.upstreamId -> upstreamId,
        column.downstreamId -> downstreamId
      )
    }.updateAndReturnGeneratedKey.apply()

    TaskDependencies(
      id = generatedKey,
      upstreamId = upstreamId,
      downstreamId = downstreamId)
  }

  def batchInsert(entities: collection.Seq[TaskDependencies])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("upstreamId") -> entity.upstreamId,
        Symbol("downstreamId") -> entity.downstreamId))
    SQL("""insert into task_dependencies(
      upstream_id,
      downstream_id
    ) values (
      {upstreamId},
      {downstreamId}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: TaskDependencies)(implicit session: DBSession = autoSession): TaskDependencies = {
    withSQL {
      update(TaskDependencies).set(
        column.id -> entity.id,
        column.upstreamId -> entity.upstreamId,
        column.downstreamId -> entity.downstreamId
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: TaskDependencies)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(TaskDependencies).where.eq(column.id, entity.id) }.update.apply()
  }

}
