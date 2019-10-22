package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class QueuedTaskLocksSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val qtl = QueuedTaskLocks.syntax("qtl")

  behavior of "QueuedTaskLocks"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = QueuedTaskLocks.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = QueuedTaskLocks.findBy(sqls.eq(qtl.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = QueuedTaskLocks.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = QueuedTaskLocks.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = QueuedTaskLocks.findAllBy(sqls.eq(qtl.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = QueuedTaskLocks.countBy(sqls.eq(qtl.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = QueuedTaskLocks.create(priority = 123, retryCount = 123)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = QueuedTaskLocks.findAll().head
    // TODO modify something
    val modified = entity
    val updated = QueuedTaskLocks.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = QueuedTaskLocks.findAll().head
    val deleted = QueuedTaskLocks.destroy(entity)
    deleted should be(1)
    val shouldBeNone = QueuedTaskLocks.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = QueuedTaskLocks.findAll()
    entities.foreach(e => QueuedTaskLocks.destroy(e))
    val batchInserted = QueuedTaskLocks.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
