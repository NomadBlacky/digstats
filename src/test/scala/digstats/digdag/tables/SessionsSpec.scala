package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class SessionsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val s = Sessions.syntax("s")

  behavior of "Sessions"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Sessions.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Sessions.findBy(sqls.eq(s.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Sessions.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Sessions.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Sessions.findAllBy(sqls.eq(s.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Sessions.countBy(sqls.eq(s.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Sessions.create(projectId = 123, workflowName = "MyString", sessionTime = 1L, sessionUuid = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Sessions.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Sessions.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Sessions.findAll().head
    val deleted = Sessions.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Sessions.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Sessions.findAll()
    entities.foreach(e => Sessions.destroy(e))
    val batchInserted = Sessions.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
