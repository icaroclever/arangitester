package br.ufmg.lcc.arangitester;

import java.io.File;

import br.ufmg.lcc.arangitester.boot.SeleniumController;
import br.ufmg.lcc.arangitester.config.ConfigEnv;
import br.ufmg.lcc.arangitester.config.ConfigFactory;
import br.ufmg.lcc.arangitester.exceptions.LccException;
import br.ufmg.lcc.arangitester.log.IResult;
import br.ufmg.lcc.arangitester.log.Result;

/**
 * Context of tests. All mainly objects needed to execute a test will be access from here.
 * DP Singleton.
 * @author Lucas Gonçalves
 *
 */
public class Context {
	private static Context instance;
	
	private SeleniumController seleniumController;
	
	private ConfigEnv serverConfig;
	
	private static File tempDir = null;
	private static File screenshotHtmlDir = null;
	private static File screenshotPngDir = null;
	private static File screenshotDefaultDir = null;
	private static final String SCREENSHOT_DEFAULT_DIR = getInstance().getTempDirectory().getAbsolutePath() + "/htmls";
	private static final String SCREENSHOT_HTML_DIR = getInstance().getTempDirectory().getAbsolutePath() + "/htmls";
	private static final String SCREENSHOT_PNG_DIR = getInstance().getTempDirectory().getAbsolutePath() + "/htmls";
	
	
	/**
	 * Result of executions.
	 */
	private IResult result = new Result();

	private Context() {
		serverConfig = ConfigFactory.getEnvSpecificConfig();
		seleniumController = new SeleniumController(serverConfig);
		System.out.println("Diretório temporário " + getTempDirectory().getAbsolutePath());
	}

	public static Context getInstance() {
		if (instance == null) {
			instance = new Context();
		}
		return instance;
	}
	
	
	public File getTempDirectory(){
		if(tempDir==null){
			File resultFile = new File("").getAbsoluteFile();
			tempDir =  new File( resultFile.getParentFile(), "temp");
		}
		
		return tempDir;
	}
	
	public File getScreenshotHtmlDir(){
		if(screenshotHtmlDir==null){
			screenshotHtmlDir = new File(SCREENSHOT_HTML_DIR);
			if ( !screenshotHtmlDir.exists() ){
				if ( !screenshotHtmlDir.mkdirs()  )
					throw new LccException("Erro ao tentar criar o diretorio " + screenshotHtmlDir.getAbsolutePath());
			}
		}
		return screenshotHtmlDir;
	}
	
	public File getScreenshotPngDir(){
		if(screenshotPngDir==null){
			screenshotPngDir = new File(SCREENSHOT_PNG_DIR);
			if ( !screenshotPngDir.exists() ){
				if ( !screenshotPngDir.mkdirs()  )
					throw new LccException("Erro ao tentar criar o diretorio " + screenshotPngDir.getAbsolutePath());
			}
		}
		return screenshotPngDir;
	}
	
	public File getScreenshotDefaultDir(){
		if(screenshotDefaultDir==null){
			screenshotDefaultDir = new File(SCREENSHOT_DEFAULT_DIR);
			if ( !screenshotDefaultDir.exists() ){
				if ( !screenshotDefaultDir.mkdirs()  )
					throw new LccException("Erro ao tentar criar o diretorio " + screenshotDefaultDir.getAbsolutePath());
			}
		}
		return screenshotPngDir;
	}

	public IResult getResult(){
		return result;
	}

	public SeleniumController getSeleniumController(){
		return seleniumController;
	}
	
	public ConfigEnv getConfig() {
		return serverConfig;
	}
}
