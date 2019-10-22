package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class SchedulesSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val s = Schedules.syntax("s")

  behavior of "Schedules"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Schedules.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Schedules.findBy(sqls.eq(s.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Schedules.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Schedules.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Schedules.findAllBy(sqls.eq(s.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Schedules.countBy(sqls.eq(s.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Schedules.create(projectId = 123, workflowDefinitionId = 1L, nextRunTime = 1L, nextScheduleTime = 1L, createdAt = null, updatedAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Schedules.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Schedules.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Schedules.findAll().head
    val deleted = Schedules.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Schedules.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Schedules.findAll()
    entities.foreach(e => Schedules.destroy(e))
    val batchInserted = Schedules.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
