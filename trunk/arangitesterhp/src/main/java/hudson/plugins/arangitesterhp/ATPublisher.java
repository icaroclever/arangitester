package hudson.plugins.arangitesterhp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;


public class ATPublisher extends Recorder{

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.BUILD;
	}
	
	@DataBoundConstructor
	public ATPublisher() {
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException{
		
		final URL xsl = getClass().getClassLoader().getResource("hudson/plugins/arangitesterhp/ATPublisher/functionaltests.xsl");
		final File html = new File(build.getArtifactsDir().getParent()+"/functionalTests.html");
		final File xml = new File(build.getArtifactsDir().getParent()+"/functionalTests.xml");
		final File screenshots = new File(build.getArtifactsDir().getParent()+"/screenshots");
		
		File screenshotsPath = new File(xsl.getPath().replace("WEB-INF/classes/hudson/plugins/arangitesterhp/ATPublisher/functionaltests.xsl","")+"/screenshots");
		screenshotsPath.mkdirs();
		
		Runtime.getRuntime().exec ("ln -s "+screenshots.getPath()+" "+screenshotsPath.getPath()+"/"+build.getNumber());
				
		if(xsl == null)
			throw new IOException("File 'functionaltests.xsl' cannot be opened.");
		if(xml == null)
			throw new IOException("File 'functionaltests.xml' cannot be created.");
		if(html == null)
			throw new IOException("File 'functionaltests.html' cannot be created.");
		if(screenshots == null)
			throw new IOException("Directory 'screenshots' cannot be created.");
			
		savingRawReport(build,listener,xml,screenshots);
		
		generatingHTMLReport(listener,xml,xsl,html);
		
		publishingReport(build,listener,html);
				  
		return true;
	}
	
	private void savingRawReport(final AbstractBuild<?, ?> build,final BuildListener listener, final File xml, final File screenshots) throws InterruptedException, IOException
	{
		final FilePath srcXml = new FilePath(new File(build.getWorkspace().toURI().getPath()+"/temp/functionalTests.xml"));
		final FilePath srcScreenShots = new FilePath(new File(build.getWorkspace().toURI().getPath()+"/temp/htmls"));
		
		listener.getLogger().print("Saving Report folder ...");
		screenshots.mkdirs();
		srcScreenShots.copyRecursiveTo(new FilePath(screenshots));
		srcXml.copyTo(new FilePath(xml));
		listener.getLogger().println("OK");	
	}
	
	private void generatingHTMLReport(final BuildListener listener,final File xml,final URL xslURL, final File html) throws IOException{
		final File xsl;
		try {
			listener.getLogger().print("Generating HTML page ...");
			xsl = new File(xslURL.toURI());
			ATUtils.xmlToHtml(xml, xsl, html);
			listener.getLogger().println("OK");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void publishingReport(final AbstractBuild<?, ?> build, final BuildListener listener, final File html)
	{
		listener.getLogger().print("Publishing Arangi Tester report ...");
		ATPublishAction action = new ATPublishAction();
		action.setHtml(ATUtils.getContents(html,"/plugin/arangitesterhp/screenshots/"+build.number));
		action.setBuild(build);
		build.getActions().add(action);
		listener.getLogger().println("OK");	
	}
	
	/** {@inheritDoc} */
	@Override
	public ATDescriptor getDescriptor() {
		return (ATDescriptor)super.getDescriptor();
	}
	
	/**
	 * Descriptor for the class {@link ATPublisher}. Used as a singleton. The
	 * class is marked as public so that it can be accessed from views.
	 *
	 * @author Ícaro Clever
	 */
	@Extension
	public static final class ATDescriptor extends BuildStepDescriptor<Publisher>{
		
		public static final String DISPLAY_NAME = "Arangi Tester";
	    public static final String URL = "arangitesterhp";
	    public static final String ICON_FILE_NAME = "graph.gif";

		/** {@inheritDoc} */
		@SuppressWarnings("unchecked")
		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

		/** {@inheritDoc} */
		@Override
		public String getDisplayName() {
			return DISPLAY_NAME;
		}
	}
}
