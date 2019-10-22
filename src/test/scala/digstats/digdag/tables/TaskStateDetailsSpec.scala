package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class TaskStateDetailsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val tsd = TaskStateDetails.syntax("tsd")

  behavior of "TaskStateDetails"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = TaskStateDetails.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = TaskStateDetails.findBy(sqls.eq(tsd.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = TaskStateDetails.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = TaskStateDetails.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = TaskStateDetails.findAllBy(sqls.eq(tsd.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = TaskStateDetails.countBy(sqls.eq(tsd.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = TaskStateDetails.create(id = 1L)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = TaskStateDetails.findAll().head
    // TODO modify something
    val modified = entity
    val updated = TaskStateDetails.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = TaskStateDetails.findAll().head
    val deleted = TaskStateDetails.destroy(entity)
    deleted should be(1)
    val shouldBeNone = TaskStateDetails.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = TaskStateDetails.findAll()
    entities.foreach(e => TaskStateDetails.destroy(e))
    val batchInserted = TaskStateDetails.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
