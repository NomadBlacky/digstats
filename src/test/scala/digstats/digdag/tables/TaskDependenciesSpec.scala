package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class TaskDependenciesSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val td = TaskDependencies.syntax("td")

  behavior of "TaskDependencies"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = TaskDependencies.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = TaskDependencies.findBy(sqls.eq(td.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = TaskDependencies.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = TaskDependencies.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = TaskDependencies.findAllBy(sqls.eq(td.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = TaskDependencies.countBy(sqls.eq(td.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = TaskDependencies.create(upstreamId = 1L, downstreamId = 1L)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = TaskDependencies.findAll().head
    // TODO modify something
    val modified = entity
    val updated = TaskDependencies.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = TaskDependencies.findAll().head
    val deleted = TaskDependencies.destroy(entity)
    deleted should be(1)
    val shouldBeNone = TaskDependencies.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = TaskDependencies.findAll()
    entities.foreach(e => TaskDependencies.destroy(e))
    val batchInserted = TaskDependencies.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
