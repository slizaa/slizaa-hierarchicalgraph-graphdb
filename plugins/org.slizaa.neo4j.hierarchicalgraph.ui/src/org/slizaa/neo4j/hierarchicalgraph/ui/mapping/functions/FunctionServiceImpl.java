package org.slizaa.neo4j.hierarchicalgraph.ui.mapping.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.mapping.dsl.mappingDsl.Expression;
import org.slizaa.neo4j.hierarchicalgraph.mapping.dsl.mappingDsl.Function;
import org.slizaa.neo4j.hierarchicalgraph.mapping.dsl.mappingDsl.StringConstant;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FunctionServiceImpl implements IFunctionService {

  /** - */
  private LoadingCache<String, List<Object>> _functionImplementations;

  /**
   * <p>
   * Creates a new instance of type {@link FunctionServiceImpl}.
   * </p>
   */
  public FunctionServiceImpl() {

    //
    _functionImplementations = CacheBuilder.newBuilder().build(new CacheLoader<String, List<Object>>() {
      public List<Object> load(String key) {
        return new ArrayList<>();
      }
    });

    //
    _functionImplementations.getUnchecked("methodDescriptorFromProperty").add(new MethodDescriptorFromProperty());
    _functionImplementations.getUnchecked("propertyValue").add(new PropertyValue());
    _functionImplementations.getUnchecked("propertyExists").add(new PropertyExists());
    _functionImplementations.getUnchecked("propertyHasValue").add(new PropertyHasValue());
    _functionImplementations.getUnchecked("hasLabel").add(new HasLabel());
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @param function
   * @return
   */
  @Override
  public <T> T evaluate(HGNode node, Function function, Class<T> returnType) {

    //
    checkNotNull(node);
    checkNotNull(function);
    checkNotNull(returnType);

    //
    String functionName = function.getName();

    // create parameter list
    List<Object> parameters = new ArrayList<>();
    parameters.add(node);
    for (Expression expression : function.getParameters()) {
      if (expression instanceof StringConstant) {
        parameters.add(((StringConstant) expression).getValue());
      } else {
        throw new UnsupportedOperationException();
      }
    }

    // find the handler
    Handler handler = findHandler(functionName, parameters);

    if (handler == null) {
      // TODO
      throw new RuntimeException();
    }

    //
    if (!handler.hasReturnType(returnType)) {
      // TODO
      throw new RuntimeException();
    }

    return (T) handler.invoke(parameters.toArray());
  }

  /**
   * <p>
   * </p>
   *
   * @param name
   * @param arguments
   * @return
   */
  private <T> Handler findHandler(String name, List<Object> arguments) {

    //
    List<Object> functions = _functionImplementations.getIfPresent(checkNotNull(name));

    // return if no function exists
    if (functions == null || functions.isEmpty()) {
      return null;
    }

    // create
    Class<?>[] argTypes = new Class[arguments.size()];
    argTypes[0] = HGNode.class;
    for (int i = 1; i < argTypes.length; i++) {
      argTypes[i] = String.class;
    }

    //
    for (Object function : functions) {
      try {
        return new Handler(function, function.getClass().getMethod(name, argTypes));
      } catch (Exception e) {
        // do nothing
      }
    }

    //
    return null;
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private static final class Handler {

    /** - */
    private Method _method;

    /** - */
    private Object _instance;

    /**
     * <p>
     * Creates a new instance of type {@link Handler}.
     * </p>
     *
     * @param instance
     * @param method
     */
    public Handler(Object instance, Method method) {
      _instance = checkNotNull(instance);
      _method = checkNotNull(method);
    }

    public boolean hasReturnType(Class<?> type) {
      return checkNotNull(type).isAssignableFrom(_method.getReturnType());
    }

    /**
     * <p>
     * </p>
     *
     * @param parameters
     * @return
     */
    public <T> T invoke(Object... parameters) {
      //
      try {
        return (T) _method.invoke(_instance, parameters);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    }
  }
}
