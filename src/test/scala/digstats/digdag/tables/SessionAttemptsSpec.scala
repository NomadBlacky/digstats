package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class SessionAttemptsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val sa = SessionAttempts.syntax("sa")

  behavior of "SessionAttempts"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = SessionAttempts.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = SessionAttempts.findBy(sqls.eq(sa.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = SessionAttempts.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = SessionAttempts.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = SessionAttempts.findAllBy(sqls.eq(sa.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = SessionAttempts.countBy(sqls.eq(sa.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = SessionAttempts.create(sessionId = 1L, siteId = 123, projectId = 123, attemptName = "MyString", stateFlags = 123, timezone = "MyString", createdAt = null, index = 123)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = SessionAttempts.findAll().head
    // TODO modify something
    val modified = entity
    val updated = SessionAttempts.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = SessionAttempts.findAll().head
    val deleted = SessionAttempts.destroy(entity)
    deleted should be(1)
    val shouldBeNone = SessionAttempts.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = SessionAttempts.findAll()
    entities.foreach(e => SessionAttempts.destroy(e))
    val batchInserted = SessionAttempts.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
