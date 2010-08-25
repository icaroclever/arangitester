package hudson.plugins.arangitesterhp;

import hudson.model.AbstractBuild;
import hudson.model.Action;

public class ATPublishAction implements Action{
	
	private String html;
	private AbstractBuild<?, ?> build;
	
	public String getIconFileName() {
		return ATPublisher.ATDescriptor.ICON_FILE_NAME;   
    }
	
	protected Class<? extends ATPublishAction> getBuildActionClass() {
        return ATPublishAction.class;
    }
	
	public String getDisplayName() 
	{
		return ATPublisher.ATDescriptor.DISPLAY_NAME;
	}

	public String getSearchUrl() {
		return ATPublisher.ATDescriptor.URL;
	}

	public String getUrlName() {
		return ATPublisher.ATDescriptor.URL;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getHtml() {
		
		return html;
	}

	public synchronized void setBuild(AbstractBuild<?, ?> build) {
		this.build = build;
	}

	public synchronized AbstractBuild<?, ?> getBuild() {
		return build;
	}
	
}
