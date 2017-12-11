package org.slizaa.neo4j.hierarchicalgraph;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.neo4j.graphdb.testfwk.BoltClientConnectionRule;
import org.slizaa.neo4j.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.neo4j.graphdb.testfwk.TestDB;
import org.slizaa.neo4j.hierarchicalgraph.fwk.TestModelFactory;

import com.google.common.base.Stopwatch;

public class PerformanceTest {

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(TestDB.MAPSTRUCT,
      5001);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  @Test
  public void testMapping() throws Exception {

    //
    System.out.println("Start");

    //
    Stopwatch stopwatch = Stopwatch.createStarted();

    //
    HGRootNode rootNode = TestModelFactory.createGraphFromDefaultMapping(_boltClientConnection.getBoltClient());

    //
    System.out.println("Create graph from default mapping: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    stopwatch.reset().start();

    //
    rootNode.getChildren().forEach((node) -> {
      node.getIncomingDependenciesFrom(rootNode.getChildren());
    });

    //
    System.out.println("Get incoming dependencies matrix: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    stopwatch.reset().start();

    rootNode.invalidateAllCaches();

    System.out.println("Invalidate caches: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    stopwatch.reset().start();

    //
    rootNode.getChildren().forEach((node) -> {
      node.getIncomingDependenciesFrom(rootNode.getChildren());
    });

    //
    System.out.println("Get incoming dependencies matrix: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    stopwatch.stop();
  }
}
