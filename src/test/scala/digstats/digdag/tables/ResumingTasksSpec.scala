package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class ResumingTasksSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val rt = ResumingTasks.syntax("rt")

  behavior of "ResumingTasks"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = ResumingTasks.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = ResumingTasks.findBy(sqls.eq(rt.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = ResumingTasks.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = ResumingTasks.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = ResumingTasks.findAllBy(sqls.eq(rt.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = ResumingTasks.countBy(sqls.eq(rt.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = ResumingTasks.create(attemptId = 1L, sourceTaskId = 1L, fullName = "MyString", updatedAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = ResumingTasks.findAll().head
    // TODO modify something
    val modified = entity
    val updated = ResumingTasks.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = ResumingTasks.findAll().head
    val deleted = ResumingTasks.destroy(entity)
    deleted should be(1)
    val shouldBeNone = ResumingTasks.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = ResumingTasks.findAll()
    entities.foreach(e => ResumingTasks.destroy(e))
    val batchInserted = ResumingTasks.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
