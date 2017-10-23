package org.slizaa.neo4j.testfwk;

import org.junit.After;
import org.junit.Before;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.graphdb.testfwk.AbstractNeo4JServerTest;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractRemoteRepositoryTest extends AbstractNeo4JServerTest {

  /** - */
  private Neo4jClient _neo4jClient;

  @Before
  public void init() throws Exception {
    _neo4jClient = TestModelFactory.createNeo4JRemoteRepository("bolt://localhost:5001");
    _neo4jClient.connect();
  }

  @After
  public void dispose() {
    _neo4jClient.disconnect();
  }

  /**
   * <p>
   * </p>
   *
   * @return the neo4JRemoteRepository
   */
  public Neo4jClient getNeo4JRemoteRepository() {
    return _neo4jClient;
  }

  /**
   * <p>
   * </p>
   *
   * @param remoteRepository
   * @return
   * @throws Exception
   */
  public HGRootNode createGraphFromDefaultMapping(Neo4jClient remoteRepository) throws Exception {
    return TestModelFactory.createGraphFromDefaultMapping(remoteRepository);
  }
}
