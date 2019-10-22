package digstats.digdag.tables

import scalikejdbc._

case class RevisionArchives(
  id: Int,
  archiveData: Array[Byte]) {

  def save()(implicit session: DBSession = RevisionArchives.autoSession): RevisionArchives = RevisionArchives.save(this)(session)

  def destroy()(implicit session: DBSession = RevisionArchives.autoSession): Int = RevisionArchives.destroy(this)(session)

}


object RevisionArchives extends SQLSyntaxSupport[RevisionArchives] {

  override val tableName = "revision_archives"

  override val columns = Seq("id", "archive_data")

  def apply(ra: SyntaxProvider[RevisionArchives])(rs: WrappedResultSet): RevisionArchives = apply(ra.resultName)(rs)
  def apply(ra: ResultName[RevisionArchives])(rs: WrappedResultSet): RevisionArchives = new RevisionArchives(
    id = rs.get(ra.id),
    archiveData = rs.get(ra.archiveData)
  )

  val ra = RevisionArchives.syntax("ra")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[RevisionArchives] = {
    withSQL {
      select.from(RevisionArchives as ra).where.eq(ra.id, id)
    }.map(RevisionArchives(ra.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[RevisionArchives] = {
    withSQL(select.from(RevisionArchives as ra)).map(RevisionArchives(ra.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(RevisionArchives as ra)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[RevisionArchives] = {
    withSQL {
      select.from(RevisionArchives as ra).where.append(where)
    }.map(RevisionArchives(ra.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[RevisionArchives] = {
    withSQL {
      select.from(RevisionArchives as ra).where.append(where)
    }.map(RevisionArchives(ra.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(RevisionArchives as ra).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    archiveData: Array[Byte])(implicit session: DBSession = autoSession): RevisionArchives = {
    val generatedKey = withSQL {
      insert.into(RevisionArchives).namedValues(
        column.archiveData -> archiveData
      )
    }.updateAndReturnGeneratedKey.apply()

    RevisionArchives(
      id = generatedKey.toInt,
      archiveData = archiveData)
  }

  def batchInsert(entities: collection.Seq[RevisionArchives])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("archiveData") -> entity.archiveData))
    SQL("""insert into revision_archives(
      archive_data
    ) values (
      {archiveData}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: RevisionArchives)(implicit session: DBSession = autoSession): RevisionArchives = {
    withSQL {
      update(RevisionArchives).set(
        column.id -> entity.id,
        column.archiveData -> entity.archiveData
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: RevisionArchives)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(RevisionArchives).where.eq(column.id, entity.id) }.update.apply()
  }

}
