package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class TaskArchivesSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val ta = TaskArchives.syntax("ta")

  behavior of "TaskArchives"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = TaskArchives.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = TaskArchives.findBy(sqls.eq(ta.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = TaskArchives.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = TaskArchives.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = TaskArchives.findAllBy(sqls.eq(ta.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = TaskArchives.countBy(sqls.eq(ta.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = TaskArchives.create(id = 1L, tasks = "MyString", createdAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = TaskArchives.findAll().head
    // TODO modify something
    val modified = entity
    val updated = TaskArchives.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = TaskArchives.findAll().head
    val deleted = TaskArchives.destroy(entity)
    deleted should be(1)
    val shouldBeNone = TaskArchives.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = TaskArchives.findAll()
    entities.foreach(e => TaskArchives.destroy(e))
    val batchInserted = TaskArchives.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
