package digstats.digdag.tables

import scalikejdbc._
import java.time.{ZonedDateTime}

case class SchemaMigrations(
  name: String,
  createdAt: ZonedDateTime) {

  def save()(implicit session: DBSession = SchemaMigrations.autoSession): SchemaMigrations = SchemaMigrations.save(this)(session)

  def destroy()(implicit session: DBSession = SchemaMigrations.autoSession): Int = SchemaMigrations.destroy(this)(session)

}


object SchemaMigrations extends SQLSyntaxSupport[SchemaMigrations] {

  override val tableName = "schema_migrations"

  override val columns = Seq("name", "created_at")

  def apply(sm: SyntaxProvider[SchemaMigrations])(rs: WrappedResultSet): SchemaMigrations = apply(sm.resultName)(rs)
  def apply(sm: ResultName[SchemaMigrations])(rs: WrappedResultSet): SchemaMigrations = new SchemaMigrations(
    name = rs.get(sm.name),
    createdAt = rs.get(sm.createdAt)
  )

  val sm = SchemaMigrations.syntax("sm")

  override val autoSession = AutoSession

  def find(name: String, createdAt: ZonedDateTime)(implicit session: DBSession = autoSession): Option[SchemaMigrations] = {
    withSQL {
      select.from(SchemaMigrations as sm).where.eq(sm.name, name).and.eq(sm.createdAt, createdAt)
    }.map(SchemaMigrations(sm.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[SchemaMigrations] = {
    withSQL(select.from(SchemaMigrations as sm)).map(SchemaMigrations(sm.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(SchemaMigrations as sm)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[SchemaMigrations] = {
    withSQL {
      select.from(SchemaMigrations as sm).where.append(where)
    }.map(SchemaMigrations(sm.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[SchemaMigrations] = {
    withSQL {
      select.from(SchemaMigrations as sm).where.append(where)
    }.map(SchemaMigrations(sm.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(SchemaMigrations as sm).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: String,
    createdAt: ZonedDateTime)(implicit session: DBSession = autoSession): SchemaMigrations = {
    withSQL {
      insert.into(SchemaMigrations).namedValues(
        column.name -> name,
        column.createdAt -> createdAt
      )
    }.update.apply()

    SchemaMigrations(
      name = name,
      createdAt = createdAt)
  }

  def batchInsert(entities: collection.Seq[SchemaMigrations])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("name") -> entity.name,
        Symbol("createdAt") -> entity.createdAt))
    SQL("""insert into schema_migrations(
      name,
      created_at
    ) values (
      {name},
      {createdAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: SchemaMigrations)(implicit session: DBSession = autoSession): SchemaMigrations = {
    withSQL {
      update(SchemaMigrations).set(
        column.name -> entity.name,
        column.createdAt -> entity.createdAt
      ).where.eq(column.name, entity.name).and.eq(column.createdAt, entity.createdAt)
    }.update.apply()
    entity
  }

  def destroy(entity: SchemaMigrations)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(SchemaMigrations).where.eq(column.name, entity.name).and.eq(column.createdAt, entity.createdAt) }.update.apply()
  }

}
