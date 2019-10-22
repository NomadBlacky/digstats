package digstats.digdag.tables

import scalikejdbc._

case class Queues(
  id: Int,
  maxConcurrency: Int,
  sharedSiteId: Option[Int] = None) {

  def save()(implicit session: DBSession = Queues.autoSession): Queues = Queues.save(this)(session)

  def destroy()(implicit session: DBSession = Queues.autoSession): Int = Queues.destroy(this)(session)

}


object Queues extends SQLSyntaxSupport[Queues] {

  override val tableName = "queues"

  override val columns = Seq("id", "max_concurrency", "shared_site_id")

  def apply(q: SyntaxProvider[Queues])(rs: WrappedResultSet): Queues = apply(q.resultName)(rs)
  def apply(q: ResultName[Queues])(rs: WrappedResultSet): Queues = new Queues(
    id = rs.get(q.id),
    maxConcurrency = rs.get(q.maxConcurrency),
    sharedSiteId = rs.get(q.sharedSiteId)
  )

  val q = Queues.syntax("q")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Queues] = {
    withSQL {
      select.from(Queues as q).where.eq(q.id, id)
    }.map(Queues(q.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Queues] = {
    withSQL(select.from(Queues as q)).map(Queues(q.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Queues as q)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Queues] = {
    withSQL {
      select.from(Queues as q).where.append(where)
    }.map(Queues(q.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Queues] = {
    withSQL {
      select.from(Queues as q).where.append(where)
    }.map(Queues(q.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Queues as q).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    maxConcurrency: Int,
    sharedSiteId: Option[Int] = None)(implicit session: DBSession = autoSession): Queues = {
    val generatedKey = withSQL {
      insert.into(Queues).namedValues(
        column.maxConcurrency -> maxConcurrency,
        column.sharedSiteId -> sharedSiteId
      )
    }.updateAndReturnGeneratedKey.apply()

    Queues(
      id = generatedKey.toInt,
      maxConcurrency = maxConcurrency,
      sharedSiteId = sharedSiteId)
  }

  def batchInsert(entities: collection.Seq[Queues])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("maxConcurrency") -> entity.maxConcurrency,
        Symbol("sharedSiteId") -> entity.sharedSiteId))
    SQL("""insert into queues(
      max_concurrency,
      shared_site_id
    ) values (
      {maxConcurrency},
      {sharedSiteId}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Queues)(implicit session: DBSession = autoSession): Queues = {
    withSQL {
      update(Queues).set(
        column.id -> entity.id,
        column.maxConcurrency -> entity.maxConcurrency,
        column.sharedSiteId -> entity.sharedSiteId
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Queues)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Queues).where.eq(column.id, entity.id) }.update.apply()
  }

}
