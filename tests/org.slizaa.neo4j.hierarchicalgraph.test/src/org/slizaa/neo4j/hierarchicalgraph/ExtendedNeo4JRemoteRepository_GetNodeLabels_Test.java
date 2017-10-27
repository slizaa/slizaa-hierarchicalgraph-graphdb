package org.slizaa.neo4j.hierarchicalgraph;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slizaa.neo4j.graphdb.testfwk.BoltClientConnectionRule;
import org.slizaa.neo4j.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.neo4j.graphdb.testfwk.TestDB;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
 * <p>
 * https://github.com/lukas-krecan/JsonUnit
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@RunWith(value = Parameterized.class)
public class ExtendedNeo4JRemoteRepository_GetNodeLabels_Test {

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(
      TestDB.MAPSTRUCT);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  /** - */
  private long                              _nodeId;

  /** - */
  private String[]                            _expected;

  /**
   * <p>
   * Creates a new instance of type {@link ExtendedNeo4JRemoteRepository_GetNodeLabels_Test}.
   * </p>
   *
   * @param nodeId
   * @param expectedJsonString
   */
  public ExtendedNeo4JRemoteRepository_GetNodeLabels_Test(long nodeId, String[] expected) {
    this._nodeId = nodeId;
    this._expected = expected;
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void getNodeProperties() {

    assertThat(Lists.newArrayList(_boltClientConnection.getBoltClient().getNode(_nodeId).labels()))
        .containsExactly(_expected);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Parameters(name = "{index}: getNodeLabels({0}) = {1}")
  public static Collection<Object[]> data() {

    return Arrays.asList(new Object[][] { { 4532, new String[] { "Java", "Member", "Method" } } });
    
    // return Arrays
    // .asList(new Object[][] { { 4532, new String["Java", "Member", "Method"]" }, { 5146, "['Java', 'Member',
    // 'Method']" },
    // { 7282, "['Java', 'Member', 'Method']" }, { 6438, "['Java', 'Member', 'Field']" },
    // { 1, "['File', 'Artifact', 'Container', 'Archive', 'Zip', 'Java', 'Jar']" } });
  }
}
