package org.korek.spring.controllers.helpers;

public class RequestMap
{
	public static final String CLIENT_VISIT_NEW = "client/visit/new";

	public static final String CLIENT_SELECT_PLACE = "selectPlace";

	public static final String CLIENT_CHECK_VISIT_POST = "checkForVisit";

	public static final String CLIENT_CONFIRM_VISIT_POST = "confirmVisit";

	public static final String CLIENT_PROFILE = "client/profile";

	public static final String CLIENT_EDIT_PROFILE = "editProfileClient";

	public static final String CLIENT_VISIT_UPCOMING = "client/visit/upcoming";

	public static final String CLIENT_VISIT_PAST = "client/visit/past";

	public static final String CLIENT_VISIT_RATE = "client/visit/{id}/rate";

	public static final String CLIENT_VISIT_CANCEL = "client/visit/{id}/cancel";

	public static final String CLIENT_CHECK_VISIT = "client/place/{id}/check_visit";

	public static String clientCheckVisit(long placeID)
	{
		return "client/place/" + placeID + "/check_visit";
	}

	public static final String CLIENT_CONFIRM_VISIT = "client/place/{id}/confirm_visit";

	public static String clientConfirmVisit(long placeID)
	{
		return "client/place/" + placeID + "/confirm_visit";
	}

	public static final String EMPLOYEE_PROFILE = "employee/profile";

	public static final String EMPLOYEE_EDIT_PROFILE = "editProfileEmployee";

	public static final String EMPLOYEE_VISIT_NEW = "employee/visit/new";

	public static final String EMPLOYEE_CHECK_VISIT_POST = "checkForVisitEmployee";

	public static final String EMPLOYEE_CONFIRM_VISIT = "employee/confirm_visit";

	public static final String EMPLOYEE_CONFIRM_VISIT_POST = "confirmVisitEmployee";

	public static final String EMPLOYEE_VISIT_PAST = "employee/visit/past";

	public static final String EMPLOYEE_VISIT_UPCOMING = "employee/visit/upcoming";

	public static final String EMPLOYEE_VISIT_CONFIRM = "employee/visit/{id}/confirm";

	public static final String EMPLOYEE_VISIT_CANCEL = "employee/visit/{id}/cancel";

	public static final String ADMIN_PLACE_ALL = "admin/place/all";

	public static final String ADMIN_PLACE_NEW = "admin/place/new";

	public static final String ADMIN_EMPLOYEE_NEW = "admin/employee/new";

	public static final String ADMIN_ADD_EMPLOYEE = "addEmployee";

	public static final String ADMIN_ADD_PLACE = "addNewPlace";

	public static final String ADMIN_PLACE_EDIT = "admin/place/{id}/edit";

	public static final String ADMIN_PLACE_EDIT_POST = "admin/place/edit";

	public static final String ADMIN_PLACE_VISITS = "admin/place/{id}/visits";

	public static final String ADMIN_PLACE_VISITS_PAST = "admin/place/{id}/visits/past";

	public static final String ADMIN_PLACE_VISIT_NEW = "admin/place/{id}/visit/new";

	public static final String ADMIN_CHECK_VISIT = "checkForVisitAdmin";

	public static final String ADMIN_CONFIRM_PLACE_VISIT = "admin/place/{id}/confirm_visit";

	public static final String ADMIN_CONFIRM_VISIT = "confirmVisitAdmin";

	public static final String ADMIN_EMPLOYEES = "admin/employees";

	public static final String ADMIN_EMPLOYEE_EDIT = "admin/employee/{id}/edit";

	public static final String ADMIN_EMPLOYEE_EDIT_POST = "admin/employee/edit";

	public static final String ADMIN_PLACE_EMPLOYEES = "admin/place/{id}/employees";

	public static final String ADMIN_PLACE_EMPLOYEE_EDIT = "admin/place/{placeID}/employee/{id}/edit";

	public static final String ADMIN_PLACE_EMPLOYEE_EDIT_POST = "admin/place/{id}/employee/edit";

	public static final String ADMIN_EMPLOYEE_VISITS = "admin/employee/{id}/visits";

	public static final String ADMIN_EMPLOYEE_VISITS_PAST = "admin/employee/{id}/visits/past";

	public static final String ADMIN_VISIT_CANCEL = "admin/visit/{id}/cancel";

	public static final String ADMIN_VISIT_CONFIRM = "admin/visit/{id}/confirm";

	public static final String PASSWORD = "password";

	public static final String DETAILS_CLIENT = "client/{id}";

	public static final String DETAILS_EMPLOYEE = "employee/{id}";

	public static final String DETAILS_PLACE = "place/{id}";

	public static final String LANGUAGE = "language/{locale}";

	public static final String LOGIN = "login";

	public static final String LOGIN_ERROR = "login-error";

	public static final String LOGIN_EXPIRED = "login-expired";

	public static final String REGISTER = "register";

	public static final String HOME = "home";

	public static String adminPlaceEdit(long placeID)
	{
		return "admin/place/" + placeID + "/edit";
	}

	public static String adminPlaceNewVisit(long placeID)
	{
		return "admin/place/" + placeID + "/visit/new";
	}

	public static String adminPlaceVisits(long placeID)
	{
		return "admin/place/" + placeID + "/visits";
	}

	public static String adminEmployeeEdit(long emmployeeID)
	{
		return  "admin/employee/" + emmployeeID + "/edit";
	}

	public static String adminPlaceEmployeeEdit(long placeID, long employeeID)
	{
		return "admin/place/" + placeID + "/employee/" + employeeID + "/edit";
	}

}
