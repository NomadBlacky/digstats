package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class Projects(
  id: Int,
  siteId: Int,
  name: Option[String] = None,
  createdAt: ZonedDateTime,
  deletedAt: Option[ZonedDateTime] = None,
  deletedName: Option[String] = None) {

  def save()(implicit session: DBSession = Projects.autoSession): Projects = Projects.save(this)(session)

  def destroy()(implicit session: DBSession = Projects.autoSession): Int = Projects.destroy(this)(session)

}


object Projects extends SQLSyntaxSupport[Projects] {

  override val tableName = "projects"

  override val columns = Seq("id", "site_id", "name", "created_at", "deleted_at", "deleted_name")

  def apply(p: SyntaxProvider[Projects])(rs: WrappedResultSet): Projects = apply(p.resultName)(rs)
  def apply(p: ResultName[Projects])(rs: WrappedResultSet): Projects = new Projects(
    id = rs.get(p.id),
    siteId = rs.get(p.siteId),
    name = rs.get(p.name),
    createdAt = rs.get(p.createdAt),
    deletedAt = rs.get(p.deletedAt),
    deletedName = rs.get(p.deletedName)
  )

  val p = Projects.syntax("p")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Projects] = {
    withSQL {
      select.from(Projects as p).where.eq(p.id, id)
    }.map(Projects(p.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Projects] = {
    withSQL(select.from(Projects as p)).map(Projects(p.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Projects as p)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Projects] = {
    withSQL {
      select.from(Projects as p).where.append(where)
    }.map(Projects(p.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Projects] = {
    withSQL {
      select.from(Projects as p).where.append(where)
    }.map(Projects(p.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Projects as p).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    siteId: Int,
    name: Option[String] = None,
    createdAt: ZonedDateTime,
    deletedAt: Option[ZonedDateTime] = None,
    deletedName: Option[String] = None)(implicit session: DBSession = autoSession): Projects = {
    val generatedKey = withSQL {
      insert.into(Projects).namedValues(
        column.siteId -> siteId,
        column.name -> name,
        column.createdAt -> createdAt,
        column.deletedAt -> deletedAt,
        column.deletedName -> deletedName
      )
    }.updateAndReturnGeneratedKey.apply()

    Projects(
      id = generatedKey.toInt,
      siteId = siteId,
      name = name,
      createdAt = createdAt,
      deletedAt = deletedAt,
      deletedName = deletedName)
  }

  def batchInsert(entities: collection.Seq[Projects])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("siteId") -> entity.siteId,
        Symbol("name") -> entity.name,
        Symbol("createdAt") -> entity.createdAt,
        Symbol("deletedAt") -> entity.deletedAt,
        Symbol("deletedName") -> entity.deletedName))
    SQL("""insert into projects(
      site_id,
      name,
      created_at,
      deleted_at,
      deleted_name
    ) values (
      {siteId},
      {name},
      {createdAt},
      {deletedAt},
      {deletedName}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Projects)(implicit session: DBSession = autoSession): Projects = {
    withSQL {
      update(Projects).set(
        column.id -> entity.id,
        column.siteId -> entity.siteId,
        column.name -> entity.name,
        column.createdAt -> entity.createdAt,
        column.deletedAt -> entity.deletedAt,
        column.deletedName -> entity.deletedName
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Projects)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Projects).where.eq(column.id, entity.id) }.update.apply()
  }

}
