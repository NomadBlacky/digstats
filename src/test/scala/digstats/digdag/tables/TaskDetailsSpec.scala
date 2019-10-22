package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class TaskDetailsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val td = TaskDetails.syntax("td")

  behavior of "TaskDetails"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = TaskDetails.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = TaskDetails.findBy(sqls.eq(td.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = TaskDetails.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = TaskDetails.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = TaskDetails.findAllBy(sqls.eq(td.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = TaskDetails.countBy(sqls.eq(td.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = TaskDetails.create(id = 1L, fullName = "MyString")
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = TaskDetails.findAll().head
    // TODO modify something
    val modified = entity
    val updated = TaskDetails.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = TaskDetails.findAll().head
    val deleted = TaskDetails.destroy(entity)
    deleted should be(1)
    val shouldBeNone = TaskDetails.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = TaskDetails.findAll()
    entities.foreach(e => TaskDetails.destroy(e))
    val batchInserted = TaskDetails.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
