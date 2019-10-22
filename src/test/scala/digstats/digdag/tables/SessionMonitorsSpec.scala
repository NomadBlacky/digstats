package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class SessionMonitorsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val sm = SessionMonitors.syntax("sm")

  behavior of "SessionMonitors"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = SessionMonitors.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = SessionMonitors.findBy(sqls.eq(sm.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = SessionMonitors.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = SessionMonitors.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = SessionMonitors.findAllBy(sqls.eq(sm.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = SessionMonitors.countBy(sqls.eq(sm.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = SessionMonitors.create(attemptId = 1L, nextRunTime = 1L, `type` = "MyString", createdAt = null, updatedAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = SessionMonitors.findAll().head
    // TODO modify something
    val modified = entity
    val updated = SessionMonitors.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = SessionMonitors.findAll().head
    val deleted = SessionMonitors.destroy(entity)
    deleted should be(1)
    val shouldBeNone = SessionMonitors.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = SessionMonitors.findAll()
    entities.foreach(e => SessionMonitors.destroy(e))
    val batchInserted = SessionMonitors.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
