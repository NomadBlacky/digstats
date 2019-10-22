package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class DelayedSessionAttemptsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val dsa = DelayedSessionAttempts.syntax("dsa")

  behavior of "DelayedSessionAttempts"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = DelayedSessionAttempts.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = DelayedSessionAttempts.findBy(sqls.eq(dsa.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = DelayedSessionAttempts.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = DelayedSessionAttempts.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = DelayedSessionAttempts.findAllBy(sqls.eq(dsa.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = DelayedSessionAttempts.countBy(sqls.eq(dsa.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = DelayedSessionAttempts.create(id = 1L, nextRunTime = 1L, updatedAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = DelayedSessionAttempts.findAll().head
    // TODO modify something
    val modified = entity
    val updated = DelayedSessionAttempts.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = DelayedSessionAttempts.findAll().head
    val deleted = DelayedSessionAttempts.destroy(entity)
    deleted should be(1)
    val shouldBeNone = DelayedSessionAttempts.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = DelayedSessionAttempts.findAll()
    entities.foreach(e => DelayedSessionAttempts.destroy(e))
    val batchInserted = DelayedSessionAttempts.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
