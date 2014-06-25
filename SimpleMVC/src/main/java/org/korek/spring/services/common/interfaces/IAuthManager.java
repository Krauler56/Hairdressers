package org.korek.spring.services.common.interfaces;

import org.korek.spring.controllers.models.ChangePassword;
import org.korek.spring.services.common.helpers.LoginDetails;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

public interface IAuthManager
{
	@Transactional
	LoginDetails doAuth(Authentication authentication);

	@Transactional
	boolean changePass(ChangePassword changePassword, Authentication authentication);
}
