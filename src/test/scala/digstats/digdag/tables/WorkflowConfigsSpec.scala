package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class WorkflowConfigsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val wc = WorkflowConfigs.syntax("wc")

  behavior of "WorkflowConfigs"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = WorkflowConfigs.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = WorkflowConfigs.findBy(sqls.eq(wc.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = WorkflowConfigs.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = WorkflowConfigs.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = WorkflowConfigs.findAllBy(sqls.eq(wc.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = WorkflowConfigs.countBy(sqls.eq(wc.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = WorkflowConfigs.create(projectId = 123, configDigest = 1L, timezone = "MyString", config = "MyString")
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = WorkflowConfigs.findAll().head
    // TODO modify something
    val modified = entity
    val updated = WorkflowConfigs.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = WorkflowConfigs.findAll().head
    val deleted = WorkflowConfigs.destroy(entity)
    deleted should be(1)
    val shouldBeNone = WorkflowConfigs.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = WorkflowConfigs.findAll()
    entities.foreach(e => WorkflowConfigs.destroy(e))
    val batchInserted = WorkflowConfigs.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
