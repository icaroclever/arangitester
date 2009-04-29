/*
 * Copyright 2000 Universidade Federal de Minas Gerais.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.ufmg.lcc.arangitester.boot;

import static br.ufmg.lcc.arangitester.Context.getInstance;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.ITestCase;
import br.ufmg.lcc.arangitester.annotations.Obs;
import br.ufmg.lcc.arangitester.annotations.Test;
import br.ufmg.lcc.arangitester.config.ConfigFactory;
import br.ufmg.lcc.arangitester.db.DbUnitController;
import br.ufmg.lcc.arangitester.exceptions.LccException;
import br.ufmg.lcc.arangitester.exceptions.FatalException;
import br.ufmg.lcc.arangitester.util.Refletions;
import br.ufmg.lcc.arangitester.util.StackTraceUtil;

import com.thoughtworks.selenium.SeleniumException;

/**
 * Entry point for tests. This class must control witch classes and
 * methods execute. It control the lifecycle of method execution
 * including login and database tasks.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class Reactor {
	private Logger LOG = Logger.getLogger(Reactor.class);
	private ILoginController loginController = LoginControllerFactory.getLoginController();
	private DbUnitController dbController = new DbUnitController();
	
	/**
	 * Start execution of Tests. 
	 * <p>Tree ways to execute: 
	 * <ol>
	 * <li>Without parameters: Execute all classes that name end with FunctionalTest</li>
	 * <li>With class name: Execute the class</li>
	 * <li>With class + method name. ex:. com.test.UserFunctionalTest.emptyName: Execute only the method in the class</li>
	 * </ol>
	 */
	public static void main(String[] args) throws Exception {
		Reactor lccReactor = new Reactor();
		ExecutionOptions executionOptions = new ExecutionOptions(args);
		if (ConfigFactory.getEnvSpecificConfig() == null ) {
		    throw new Exception(String.format("Environment to user %s don´t exist", System.getProperty("user.name")));
		}
		lccReactor.startExecution(executionOptions);
	}

	/**
	 * Stops selenium only when all test cases are finished.
	 * Note that test case is related to test class, not with a test method.
	 * Test class contains one or many test method. 
	 */
	public void startExecution(ExecutionOptions executionOptions) throws Exception{
		try {
			//Logger.getLogger("br.ufmg.lcc").setLevel((Level) Level.DEBUG);
			List<Class<?>> arrayList = new ArrayList<Class<?>>();
			Method method = null;
			if (executionOptions.isToExecuteAllClasses()) {
				Scanner scanner = new Scanner();
				scanner.scan();
				arrayList = scanner.getTests();
			} else {
				arrayList.add(executionOptions.getClassFromCommand());
			}
			method = executionOptions.getMethodFromCommand();
			
			for (Class<?> test : arrayList) {
				executeTestClass(test, method);
			}

		} finally {
			Context.getInstance().getSeleniumController().stop();
			File resultFile = new File(Context.getInstance().getTempDirectory(),"functionalTests.xml");
			LOG.info("");
			LOG.info("###################################################################################################");
			LOG.info("");
			getInstance().getResult().save(resultFile);
		}
	}

	private void executeTestClass(Class<?> test, Method onlyMethod) {
		Obs obs = test.getAnnotation(Obs.class);
		if ( obs == null ){
			getInstance().getResult().startUseCase(test.getSimpleName(), null);	
		}else{
			getInstance().getResult().startUseCase(test.getSimpleName(), obs.value());
		}
		try {
			dbController.reload(test);
			if (this.loginController != null) {
			    loginController.forceLogOff();
			}
			List<LccMethod> methods = createLccMethodsList(test);
			
			Object testObj = Refletions.createTestClassInstance(test);
			if (this.loginController != null) {
			    loginController.loginIfNeed(testObj, null);
			}
			
			if (onlyMethod != null) {
				for (LccMethod method : methods) {
					if (method.getMethod().equals(onlyMethod)) {
						executeMethodTest(testObj, methods, method);
					}
				}
			} else {
				for (LccMethod method : methods) {
					executeMethodTest(testObj, methods, method);
				}
			}
		} catch (FatalException e) {
			Context.getInstance().getResult().addError("Erro fatal.", e);
		}finally {
			getInstance().getResult().endUseCase();
			
		}

	}

	private void executeMethodTest(Object target, List<LccMethod> methods, LccMethod method) {
		if (method.isExecuted() || method.isSkip())
			return;

		if (method.getDependency() != null) {
			if (!method.getDependency().isExecuted()
					&& !method.getDependency().isError()
					&& !method.getDependency().isSkip()) {
				executeMethodTest(target, methods, method.getDependency());
			} else if ((method.getDependency().isError() || method.getDependency().isSkip())) {
				method.setSkip(true);
				Context.getInstance().getResult().skipTestCase(method.getMethod().getName(), getTestName(method.getMethod()));
				return;
			}
		}
		
		if ( target instanceof ITestCase ){
			if ( !((ITestCase)target).isToExecute(method.getMethod().getName())){
				return;
			}
		}
		
		getInstance().getResult().startTestCase(method.getMethod().getName(), getTestName(method.getMethod()));
		try {
			method.setExecuted(true);
			try {
				if (this.loginController != null) {
				    loginController.loginIfNeed(target, method.getMethod());
				}
				method.getMethod().invoke(target);
			}
			catch (Throwable e) {
				if(method.getMethod().getAnnotation(Test.class)==null || !(e instanceof LccException)){
					
					if (e.getCause() != null && !(e.getCause() instanceof LccException)){
						
						if ( e instanceof InvocationTargetException && e.getCause() != null ){
							e = e.getCause();
						}
						
						if (e instanceof SeleniumException && e.getMessage() != null) {
							//if(e.getMessage().matches(".*Element.*not.*found.*")) LOG.info("Erro de elemento não encontrado, danado!");
						}
						
						String stacktrace = StackTraceUtil.getStackTrace(e);
						getInstance().getResult().addError("erro nao identificado: " + stacktrace);
					}
				}
				
				method.setError(true);
			}
		} finally {
			getInstance().getResult().endTestCase();
		}

	}


	public static String getTestName(Method method) {
		Test annotation = method.getAnnotation(Test.class);
		if (annotation != null) {
			return annotation.value();
		}
		return method.getName();
	}

	/**
	 * Create a list of method and set depedencies on each method.
	 */
	private List<LccMethod> createLccMethodsList(Class<?> clazz) {
		List<LccMethod> methods = new ArrayList<LccMethod>();

		for (Method method : Refletions.getTestMethodsOrdered(clazz)) {
			methods.add(new LccMethod(method));
		}

		// Set Depedencies
		for (LccMethod method : methods) {
			Test lccTest = method.getMethod().getAnnotation(Test.class);
			if (StringUtils.isNotBlank(lccTest.dependency())) {
				for (LccMethod in : methods) {
					if (in.getMethod().getName().equals(lccTest.dependency())) {
						method.setDependency(in);
					}
				}
			}
		}
		return methods;
	}

}

class LccMethod {
	private boolean executed = false;
	private Method method = null;
	private LccMethod dependency = null;
	private boolean error = false;
	private boolean skip = false;

	public LccMethod(Method method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return method.getName();
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public LccMethod getDependency() {
		return dependency;
	}

	public void setDependency(LccMethod dependency) {
		this.dependency = dependency;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}
}
