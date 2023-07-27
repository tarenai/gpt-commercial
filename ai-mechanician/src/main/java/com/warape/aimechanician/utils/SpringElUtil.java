package com.warape.aimechanician.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author apeto
 * @create 2021/9/18 3:49 下午
 */
public class SpringElUtil {

  /**
   * 用于SpEL表达式解析.
   */
  private static final SpelExpressionParser parser = new SpelExpressionParser();
  /**
   * 用于获取方法参数定义名字.
   */
  private static final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

  public static String generateKeyBySpEL (String spELString, ProceedingJoinPoint joinPoint) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
    Expression expression = parser.parseExpression(spELString);
    EvaluationContext context = new StandardEvaluationContext();
    Object[] args = joinPoint.getArgs();
    for (int i = 0; i < args.length; i++) {
      context.setVariable(paramNames[i], args[i]);
    }
    return expression.getValue(context).toString();
  }

}
