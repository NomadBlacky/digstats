package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class QueuedTasksSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val qt = QueuedTasks.syntax("qt")

  behavior of "QueuedTasks"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = QueuedTasks.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = QueuedTasks.findBy(sqls.eq(qt.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = QueuedTasks.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = QueuedTasks.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = QueuedTasks.findAllBy(sqls.eq(qt.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = QueuedTasks.countBy(sqls.eq(qt.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = QueuedTasks.create(siteId = 123, uniqueName = "MyString", createdAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = QueuedTasks.findAll().head
    // TODO modify something
    val modified = entity
    val updated = QueuedTasks.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = QueuedTasks.findAll().head
    val deleted = QueuedTasks.destroy(entity)
    deleted should be(1)
    val shouldBeNone = QueuedTasks.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = QueuedTasks.findAll()
    entities.foreach(e => QueuedTasks.destroy(e))
    val batchInserted = QueuedTasks.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
