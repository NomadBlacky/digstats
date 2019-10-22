package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class TaskArchives(
  id: Long,
  tasks: String,
  createdAt: ZonedDateTime) {

  def save()(implicit session: DBSession = TaskArchives.autoSession): TaskArchives = TaskArchives.save(this)(session)

  def destroy()(implicit session: DBSession = TaskArchives.autoSession): Int = TaskArchives.destroy(this)(session)

}


object TaskArchives extends SQLSyntaxSupport[TaskArchives] {

  override val tableName = "task_archives"

  override val columns = Seq("id", "tasks", "created_at")

  def apply(ta: SyntaxProvider[TaskArchives])(rs: WrappedResultSet): TaskArchives = apply(ta.resultName)(rs)
  def apply(ta: ResultName[TaskArchives])(rs: WrappedResultSet): TaskArchives = new TaskArchives(
    id = rs.get(ta.id),
    tasks = rs.get(ta.tasks),
    createdAt = rs.get(ta.createdAt)
  )

  val ta = TaskArchives.syntax("ta")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession = autoSession): Option[TaskArchives] = {
    withSQL {
      select.from(TaskArchives as ta).where.eq(ta.id, id)
    }.map(TaskArchives(ta.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[TaskArchives] = {
    withSQL(select.from(TaskArchives as ta)).map(TaskArchives(ta.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(TaskArchives as ta)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[TaskArchives] = {
    withSQL {
      select.from(TaskArchives as ta).where.append(where)
    }.map(TaskArchives(ta.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[TaskArchives] = {
    withSQL {
      select.from(TaskArchives as ta).where.append(where)
    }.map(TaskArchives(ta.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(TaskArchives as ta).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    id: Long,
    tasks: String,
    createdAt: ZonedDateTime)(implicit session: DBSession = autoSession): TaskArchives = {
    withSQL {
      insert.into(TaskArchives).namedValues(
        column.id -> id,
        column.tasks -> tasks,
        column.createdAt -> createdAt
      )
    }.update.apply()

    TaskArchives(
      id = id,
      tasks = tasks,
      createdAt = createdAt)
  }

  def batchInsert(entities: collection.Seq[TaskArchives])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("id") -> entity.id,
        Symbol("tasks") -> entity.tasks,
        Symbol("createdAt") -> entity.createdAt))
    SQL("""insert into task_archives(
      id,
      tasks,
      created_at
    ) values (
      {id},
      {tasks},
      {createdAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: TaskArchives)(implicit session: DBSession = autoSession): TaskArchives = {
    withSQL {
      update(TaskArchives).set(
        column.id -> entity.id,
        column.tasks -> entity.tasks,
        column.createdAt -> entity.createdAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: TaskArchives)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(TaskArchives).where.eq(column.id, entity.id) }.update.apply()
  }

}
