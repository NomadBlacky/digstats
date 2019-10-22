package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class RevisionsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val r = Revisions.syntax("r")

  behavior of "Revisions"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Revisions.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Revisions.findBy(sqls.eq(r.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Revisions.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Revisions.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Revisions.findAllBy(sqls.eq(r.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Revisions.countBy(sqls.eq(r.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Revisions.create(projectId = 123, name = "MyString", archiveType = "MyString", createdAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Revisions.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Revisions.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Revisions.findAll().head
    val deleted = Revisions.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Revisions.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Revisions.findAll()
    entities.foreach(e => Revisions.destroy(e))
    val batchInserted = Revisions.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
