package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class QueueSettingsSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val qs = QueueSettings.syntax("qs")

  behavior of "QueueSettings"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = QueueSettings.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = QueueSettings.findBy(sqls.eq(qs.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = QueueSettings.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = QueueSettings.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = QueueSettings.findAllBy(sqls.eq(qs.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = QueueSettings.countBy(sqls.eq(qs.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = QueueSettings.create(siteId = 123, name = "MyString", createdAt = null, updatedAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = QueueSettings.findAll().head
    // TODO modify something
    val modified = entity
    val updated = QueueSettings.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = QueueSettings.findAll().head
    val deleted = QueueSettings.destroy(entity)
    deleted should be(1)
    val shouldBeNone = QueueSettings.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = QueueSettings.findAll()
    entities.foreach(e => QueueSettings.destroy(e))
    val batchInserted = QueueSettings.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
