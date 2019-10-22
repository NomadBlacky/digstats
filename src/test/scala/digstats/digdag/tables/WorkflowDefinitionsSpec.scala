package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class WorkflowDefinitionsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val wd = WorkflowDefinitions.syntax("wd")

  behavior of "WorkflowDefinitions"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = WorkflowDefinitions.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = WorkflowDefinitions.findBy(sqls.eq(wd.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = WorkflowDefinitions.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = WorkflowDefinitions.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = WorkflowDefinitions.findAllBy(sqls.eq(wd.id, 1L))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = WorkflowDefinitions.countBy(sqls.eq(wd.id, 1L))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = WorkflowDefinitions.create(configId = 123, revisionId = 123, name = "MyString")
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = WorkflowDefinitions.findAll().head
    // TODO modify something
    val modified = entity
    val updated = WorkflowDefinitions.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = WorkflowDefinitions.findAll().head
    val deleted = WorkflowDefinitions.destroy(entity)
    deleted should be(1)
    val shouldBeNone = WorkflowDefinitions.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = WorkflowDefinitions.findAll()
    entities.foreach(e => WorkflowDefinitions.destroy(e))
    val batchInserted = WorkflowDefinitions.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
