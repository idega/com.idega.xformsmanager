package com.idega.xformsmanager.business;

/**
 * indicates, that the submission, that was accessed is invalidated
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $ Last modified: $Date: 2009/01/20 17:36:15 $ by $Author: civilis $
 */
public class InvalidSubmissionException extends RuntimeException {
	
	public InvalidSubmissionException(String msg) {
		super(msg);
	}
}