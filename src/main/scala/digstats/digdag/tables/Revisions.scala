package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class Revisions(
  id: Int,
  projectId: Int,
  name: String,
  defaultParams: Option[String] = None,
  archiveType: String,
  archivePath: Option[String] = None,
  archiveMd5: Option[Array[Byte]] = None,
  createdAt: ZonedDateTime,
  userInfo: Option[String] = None) {

  def save()(implicit session: DBSession = Revisions.autoSession): Revisions = Revisions.save(this)(session)

  def destroy()(implicit session: DBSession = Revisions.autoSession): Int = Revisions.destroy(this)(session)

}


object Revisions extends SQLSyntaxSupport[Revisions] {

  override val tableName = "revisions"

  override val columns = Seq("id", "project_id", "name", "default_params", "archive_type", "archive_path", "archive_md5", "created_at", "user_info")

  def apply(r: SyntaxProvider[Revisions])(rs: WrappedResultSet): Revisions = apply(r.resultName)(rs)
  def apply(r: ResultName[Revisions])(rs: WrappedResultSet): Revisions = new Revisions(
    id = rs.get(r.id),
    projectId = rs.get(r.projectId),
    name = rs.get(r.name),
    defaultParams = rs.get(r.defaultParams),
    archiveType = rs.get(r.archiveType),
    archivePath = rs.get(r.archivePath),
    archiveMd5 = rs.get(r.archiveMd5),
    createdAt = rs.get(r.createdAt),
    userInfo = rs.get(r.userInfo)
  )

  val r = Revisions.syntax("r")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Revisions] = {
    withSQL {
      select.from(Revisions as r).where.eq(r.id, id)
    }.map(Revisions(r.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Revisions] = {
    withSQL(select.from(Revisions as r)).map(Revisions(r.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Revisions as r)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Revisions] = {
    withSQL {
      select.from(Revisions as r).where.append(where)
    }.map(Revisions(r.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Revisions] = {
    withSQL {
      select.from(Revisions as r).where.append(where)
    }.map(Revisions(r.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Revisions as r).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    projectId: Int,
    name: String,
    defaultParams: Option[String] = None,
    archiveType: String,
    archivePath: Option[String] = None,
    archiveMd5: Option[Array[Byte]] = None,
    createdAt: ZonedDateTime,
    userInfo: Option[String] = None)(implicit session: DBSession = autoSession): Revisions = {
    val generatedKey = withSQL {
      insert.into(Revisions).namedValues(
        column.projectId -> projectId,
        column.name -> name,
        column.defaultParams -> defaultParams,
        column.archiveType -> archiveType,
        column.archivePath -> archivePath,
        column.archiveMd5 -> archiveMd5,
        column.createdAt -> createdAt,
        column.userInfo -> userInfo
      )
    }.updateAndReturnGeneratedKey.apply()

    Revisions(
      id = generatedKey.toInt,
      projectId = projectId,
      name = name,
      defaultParams = defaultParams,
      archiveType = archiveType,
      archivePath = archivePath,
      archiveMd5 = archiveMd5,
      createdAt = createdAt,
      userInfo = userInfo)
  }

  def batchInsert(entities: collection.Seq[Revisions])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("projectId") -> entity.projectId,
        Symbol("name") -> entity.name,
        Symbol("defaultParams") -> entity.defaultParams,
        Symbol("archiveType") -> entity.archiveType,
        Symbol("archivePath") -> entity.archivePath,
        Symbol("archiveMd5") -> entity.archiveMd5,
        Symbol("createdAt") -> entity.createdAt,
        Symbol("userInfo") -> entity.userInfo))
    SQL("""insert into revisions(
      project_id,
      name,
      default_params,
      archive_type,
      archive_path,
      archive_md5,
      created_at,
      user_info
    ) values (
      {projectId},
      {name},
      {defaultParams},
      {archiveType},
      {archivePath},
      {archiveMd5},
      {createdAt},
      {userInfo}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Revisions)(implicit session: DBSession = autoSession): Revisions = {
    withSQL {
      update(Revisions).set(
        column.id -> entity.id,
        column.projectId -> entity.projectId,
        column.name -> entity.name,
        column.defaultParams -> entity.defaultParams,
        column.archiveType -> entity.archiveType,
        column.archivePath -> entity.archivePath,
        column.archiveMd5 -> entity.archiveMd5,
        column.createdAt -> entity.createdAt,
        column.userInfo -> entity.userInfo
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Revisions)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Revisions).where.eq(column.id, entity.id) }.update.apply()
  }

}
