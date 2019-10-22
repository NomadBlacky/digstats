package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class QueuesSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val q = Queues.syntax("q")

  behavior of "Queues"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Queues.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Queues.findBy(sqls.eq(q.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Queues.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Queues.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Queues.findAllBy(sqls.eq(q.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Queues.countBy(sqls.eq(q.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Queues.create(maxConcurrency = 123)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Queues.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Queues.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Queues.findAll().head
    val deleted = Queues.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Queues.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Queues.findAll()
    entities.foreach(e => Queues.destroy(e))
    val batchInserted = Queues.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
