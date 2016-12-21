package org.pplm.framework.utils.servlet.filter;

import java.security.Principal;

/**
 * 
 * @author OracleGao
 *
 * @param <T>
 */
public interface PrincipalTrack<T extends PrincipalTrackBeanable> extends FilterProcess {
	
	public T track(Principal principal);
	
}
