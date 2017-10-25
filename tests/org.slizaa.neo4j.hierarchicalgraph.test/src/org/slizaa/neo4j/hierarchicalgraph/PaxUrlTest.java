package org.slizaa.neo4j.hierarchicalgraph;

import java.io.IOException;

import org.junit.Test;
import org.ops4j.pax.url.mvn.MavenResolvers;

public class PaxUrlTest {

  @Test
  public void testTest() throws IOException {
    
    MavenResolvers.createMavenResolver(null, null).resolve("org.neo4j.test:neo4j-harness:3.2.1");
  }
}
