package org.slizaa.neo4j.hierarchicalgraph.ui.mapping.functions;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedNodeSource;

public class MethodDescriptorFromProperty {

  //
  private static final String PATTERN = "([^\\s]+) ([^\\s]+)(\\(([^\\s]*)\\))";

  //
  private Pattern             pattern = Pattern.compile(PATTERN);

  /**
   * <p>
   * </p>
   *
   * @param node
   * @param propertyKey
   * @return
   */
  public String methodDescriptorFromProperty(HGNode node, String propertyKey) {
    Optional<Neo4JBackedNodeSource> nodeSource = node.getNodeSource(Neo4JBackedNodeSource.class);
    return nodeSource.isPresent() && nodeSource.get().getProperties().containsKey(propertyKey)
        ? parse(node, nodeSource.get().getProperties().get(propertyKey)) : node.getIdentifier().toString();
  }

  /**
   * <p>
   * </p>
   *
   * @param line
   * @return
   */
  private String parse(HGNode node, String line) {

    // Now create matcher object.
    Matcher matcher = pattern.matcher(line);
    if (matcher.find()) {

      String returnType = simpleName(matcher.group(1));
      String name = simpleName(matcher.group(2));

      //
      if ("<init>".equals(name)) {
        try {
          String className = node.getParent().getNodeSource(Neo4JBackedNodeSource.class).get().getProperties().get("name");
          name = name.replace("<init>", className);
        } catch (Exception e) {
          //
        }
      }

      //
      StringBuilder builder = new StringBuilder();
      String[] arguments = split(matcher.group(4));
      for (int i = 0; i < arguments.length; i++) {
        builder.append(simpleName(arguments[i]));
        if (i + 1 < arguments.length) {
          builder.append(", ");
        }
      }

      return name + "(" + builder.toString() + ") : " + returnType;
    } else {
      return line;
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param names
   * @return
   */
  private static String[] split(String names) {

    //
    if (names == null) {
      return new String[0];
    }

    //
    names = names.trim();

    //
    if (names.length() == 0) {
      return new String[0];
    }

    //
    return names.split(",");
  }

  /**
   * <p>
   * </p>
   *
   * @param name
   * @return
   */
  private static String simpleName(String name) {

    //
    if (name == null) {
      return name;
    }

    //
    int lastIndex = name.lastIndexOf('.');
    if (lastIndex != -1) {
      return name.substring(lastIndex + 1, name.length());
    }

    //
    return name;
  }
}
