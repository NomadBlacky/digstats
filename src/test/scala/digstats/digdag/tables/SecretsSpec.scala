package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class SecretsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val s = Secrets.syntax("s")

  behavior of "Secrets"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Secrets.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Secrets.findBy(sqls.eq(s.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Secrets.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Secrets.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Secrets.findAllBy(sqls.eq(s.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Secrets.countBy(sqls.eq(s.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Secrets.create(siteId = 1L, projectId = 1L, scope = "MyString", engine = "MyString", key = "MyString", value = "MyString", updatedAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Secrets.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Secrets.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Secrets.findAll().head
    val deleted = Secrets.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Secrets.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Secrets.findAll()
    entities.foreach(e => Secrets.destroy(e))
    val batchInserted = Secrets.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
