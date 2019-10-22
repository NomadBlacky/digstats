package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class SchemaMigrationsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val sm = SchemaMigrations.syntax("sm")

  behavior of "SchemaMigrations"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = SchemaMigrations.find("MyString", null)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = SchemaMigrations.findBy(sqls.eq(sm.name, "MyString"))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = SchemaMigrations.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = SchemaMigrations.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = SchemaMigrations.findAllBy(sqls.eq(sm.name, "MyString"))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = SchemaMigrations.countBy(sqls.eq(sm.name, "MyString"))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = SchemaMigrations.create(name = "MyString", createdAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = SchemaMigrations.findAll().head
    // TODO modify something
    val modified = entity
    val updated = SchemaMigrations.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = SchemaMigrations.findAll().head
    val deleted = SchemaMigrations.destroy(entity)
    deleted should be(1)
    val shouldBeNone = SchemaMigrations.find("MyString", null)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = SchemaMigrations.findAll()
    entities.foreach(e => SchemaMigrations.destroy(e))
    val batchInserted = SchemaMigrations.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
