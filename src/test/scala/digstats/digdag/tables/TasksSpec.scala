package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class TasksSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val t = Tasks.syntax("t")

  behavior of "Tasks"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Tasks.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Tasks.findBy(sqls.eq(t.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Tasks.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Tasks.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Tasks.findAllBy(sqls.eq(t.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Tasks.countBy(sqls.eq(t.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Tasks.create(attemptId = 1L, taskType = 123, state = 123, stateFlags = 123, updatedAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Tasks.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Tasks.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Tasks.findAll().head
    val deleted = Tasks.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Tasks.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Tasks.findAll()
    entities.foreach(e => Tasks.destroy(e))
    val batchInserted = Tasks.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
