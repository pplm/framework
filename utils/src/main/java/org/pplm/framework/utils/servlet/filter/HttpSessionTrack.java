package org.pplm.framework.utils.servlet.filter;

import javax.servlet.http.HttpSession;

/**
 * 
 * @author OracleGao
 *
 */
public interface HttpSessionTrack<T extends HttpSessionTrackBeanable> extends FilterProcess {

	public T track(HttpSession httpSession);

}
