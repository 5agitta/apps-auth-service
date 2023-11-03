package com.sagitta.authservice.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect for logging execution of controller, service and repository Spring components.
 *
 * @author Ramesh Fadatare
 */
@Aspect
@Component
public class LoggingAspect {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Pointcut that matches all repositories, services and Web REST endpoints.
	 */
	@Pointcut ("within(@org.springframework.stereotype.Repository *) " +
			" || within(@org.springframework.stereotype.Service *) " +
			" || within(@org.springframework.web.bind.annotation.RestController *) " +
			" || within(@com.sagitta.authservice.logging.annotations.LoggingClass *)" +
			" || within(@org.springframework.cloud.openfeign.FeignClient *)")
	public void springBeanPointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the advices.
	}
	
	@Pointcut("@annotation(com.sagitta.authservice.logging.annotations.NoLogging)")
	public void noLoggingPointcut() {
		// This pointcut matches methods annotated with @NoLogging.
	}
	
	/**
	 * Advice that logs methods throwing exceptions.
	 *
	 * @param joinPoint join point for advice
	 * @param e         exception
	 */
	@AfterThrowing (pointcut = "springBeanPointcut()", throwing = "e")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
		setMDC(joinPoint);
		log.error("Exception in {}.{}() with cause = {}",
		          joinPoint.getSignature()
		                   .getDeclaringTypeName(),
		          joinPoint.getSignature()
		                   .getName(),
		          e.getCause() != null ? e.getCause() : "NULL");
		clearMDC();
	}
	
	/**
	 * Advice that logs when a method is entered and exited.
	 *
	 * @param joinPoint join point for advice
	 *
	 * @return result
	 *
	 * @throws Throwable throws IllegalArgumentException
	 */
	@Around ("springBeanPointcut() && !noLoggingPointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		if (log.isDebugEnabled()) {
			setMDC(joinPoint);
			log.debug("Executing: {}.{}() with argument[s] = {}",
			          joinPoint.getSignature()
			                   .getDeclaringTypeName(),
			          joinPoint.getSignature()
			                   .getName(),
			          Arrays.toString(joinPoint.getArgs()));
			clearMDC();
		}
		try {
			Object result = joinPoint.proceed();
			if (log.isDebugEnabled()) {
				setMDC(joinPoint);
				log.debug("Executed: {}.{}() = {}",
				          joinPoint.getSignature()
				                   .getDeclaringTypeName(),
				          joinPoint.getSignature()
				                   .getName(),
				          result);
				clearMDC();
			}
			return result;
		} catch (IllegalArgumentException e) {
			setMDC(joinPoint);
			log.error("Illegal argument: {} in {}.{}()",
			          Arrays.toString(joinPoint.getArgs()),
			          joinPoint.getSignature()
			                   .getDeclaringTypeName(),
			          joinPoint.getSignature()
			                   .getName());
			clearMDC();
			throw e;
		}
	}
	
	protected void setMDC(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		MDC.put("class", className);
		MDC.put("method", methodName);
	}
	
	protected void clearMDC() {
		MDC.remove("class");
		MDC.remove("method");
	}
	
}
