package digstats.digdag.tables

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class RevisionArchivesSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val ra = RevisionArchives.syntax("ra")

  behavior of "RevisionArchives"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = RevisionArchives.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = RevisionArchives.findBy(sqls.eq(ra.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = RevisionArchives.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = RevisionArchives.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = RevisionArchives.findAllBy(sqls.eq(ra.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = RevisionArchives.countBy(sqls.eq(ra.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = RevisionArchives.create(archiveData = Array[Byte]())
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = RevisionArchives.findAll().head
    // TODO modify something
    val modified = entity
    val updated = RevisionArchives.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = RevisionArchives.findAll().head
    val deleted = RevisionArchives.destroy(entity)
    deleted should be(1)
    val shouldBeNone = RevisionArchives.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = RevisionArchives.findAll()
    entities.foreach(e => RevisionArchives.destroy(e))
    val batchInserted = RevisionArchives.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
