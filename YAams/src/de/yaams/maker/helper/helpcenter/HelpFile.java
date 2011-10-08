/**
 * 
 */
package de.yaams.maker.helper.helpcenter;

import java.net.URL;

/**
 * @author abt
 * 
 */
public class HelpFile {
	
	protected String icon, title, id;
	protected URL url;
	
	/**
	 * Create a new HelpFile
	 * 
	 * @param id
	 * @param title
	 * @param icon
	 * @param url
	 */
	public HelpFile(String id, String title, String icon, URL url) {
		super();
		this.id = id;
		this.title = title;
		this.icon = icon;
		this.url = url;
	}
	
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	
	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title
	 *            the title to set
	 */
	public HelpFile setTitle(String title) {
		this.title = title;
		return this;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}
	
	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}
	
}
