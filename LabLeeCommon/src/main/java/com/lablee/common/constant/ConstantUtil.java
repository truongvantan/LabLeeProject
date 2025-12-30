package com.lablee.common.constant;

public class ConstantUtil {
	public static final String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static final String REGEX_PASSWORD_60 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,60}$";
	public static final String REGEX_PASSWORD_20 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$";
	public static final String REGEX_DATE_YYYY_MM_DD = "^\\d{4}-\\d{2}-\\d{2}$";
	public static final String REGEX_YEAR_4_DIGIT = "^(19|20)\\d{2}$";

	public static final long MAX_FILE_SIZE = 1_048_576L; // 1MB
	public static final int PAGE_SIZE_DEFAULT = 5;
	public static final String[] LIST_PAGE_SIZE = {"1", "5", "10", "15"};

	// Common Message
	public static final String MESSAGE_FAIL_INTERNAL_SERVER_ERROR = "An unknown error has occurred in the system. Please try again.";

	// Message biding result validation
	public static final String MESSAGE_FAIL_VALIDATION_EMAIL = "The email is not in the correct format. (e.g: example@gmail.com)";
	public static final String MESSAGE_FAIL_VALIDATION_PASSWORD_20 = "Passwords must be at least 6 characters long and no more than 20 characters long, must contain at least 1 digit, 1 lowercase letter, 1 uppercase letter, and 1 special character";
	public static final String MESSAGE_FAIL_VALIDATION_PASSWORD_60 = "Passwords must be at least 6 characters long and no more than 60 characters long, must contain at least 1 digit, 1 lowercase letter, 1 uppercase letter, and 1 special character";

	public static final String MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_50 = "Please enter a maximum of 50 characters";
	public static final String MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_128 = "Please enter a maximum of 128 characters";
	public static final String MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_255 = "Please enter a maximum of 255 characters";
	public static final String MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_512 = "Please enter a maximum of 512 characters";
	public static final String MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_64 = "Please enter a maximum of 64 characters";
	public static final String MESSAGE_FAIL_VALIDATION_MIN_SIZE_INPUT_TEXT_3 = "Please enter at least 3 characters";
	public static final String MESSAGE_FAIL_VALIDATION_BLANK_INPUT_TEXT = "Please do not leave it blank";
	public static final String MESSAGE_FAIL_VALIDATION_BINDING_RESULT = "A binding result error occurred";
	public static final String MESSAGE_FAIL_VALIDATION_NOT_NULL = "Please do not leave it blank";
	public static final String MESSAGE_FAIL_VALIDATION_DATE = "The date is not in the correct format";
	public static final String MESSAGE_FAIL_VALIDATION_YEAR_4_DIGIT = "The year is not in the correct format (XXXX)";

	// Message FAIL
	// upload file
	public static final String MESSAGE_FAIL_VALIDATION_UPLOAD_FILE_SIZE_1MB = "Please select a file that does not exceed 1MB in size";
	
	//default upload logo
	public static final String PATH_SITE_LOGO_DIR_DEFAULT = "../uploads/site-logo/";
	public static final String PATH_SITE_LOGO_STORED_DEFAULT = "/uploads/site-logo/";

	// default upload user
	public static final String PATH_USER_PHOTO_UPLOAD_DIR_DEFAULT = "../uploads/user-photos/";
	public static final String PATH_USER_PHOTO_STORED_DEFAULT = "/uploads/user-photos/";

	// default upload project
	public static final String PATH_PROJECT_THUMBNAIL_UPLOAD_DIR_DEFAULT = "../uploads/project-thumbnails/";
	public static final String PATH_PROJECT_THUMBNAIL_STORED_DEFAULT = "/uploads/project-thumbnails/";
	public static final String PATH_PROJECT_CONTENT_UPLOAD_DIR_DEFAULT = "../uploads/project-content/";
	public static final String PATH_PROJECT_CONTENT_STORED_DEFAULT = "/uploads/project-content/";

	// default upload publication
	public static final String PATH_PUBLICATION_THUMBNAIL_UPLOAD_DIR_DEFAULT = "../uploads/publication-thumbnails/";
	public static final String PATH_PUBLICATION_THUMBNAIL_STORED_DEFAULT = "/uploads/publication-thumbnails/";
	public static final String PATH_PUBLICATION_CONTENT_UPLOAD_DIR_DEFAULT = "../uploads/publication-content/";
	public static final String PATH_PUBLICATION_CONTENT_STORED_DEFAULT = "/uploads/publication-content/";

	// default upload member lab profile
	public static final String PATH_MEMBER_LAB_AVATAR_UPLOAD_DIR_DEFAULT = "../uploads/member-avatars/";
	public static final String PATH_MEMBER_LAB_AVATAR_STORED_DEFAULT = "/uploads/member-avatars/";
	public static final String PATH_MEMBER_LAB_BIO_UPLOAD_DIR_DEFAULT = "../uploads/members-bio/";
	public static final String PATH_MEMBER_LAB_BIO_STORED_DEFAULT = "/uploads/members-bio/";

	// default upload news
	public static final String PATH_NEWS_THUMBNAIL_UPLOAD_DIR_DEFAULT = "../uploads/news-thumbnails/";
	public static final String PATH_NEWS_THUMBNAIL_STORED_DEFAULT = "/uploads/news-thumbnails/";
	public static final String PATH_NEWS_CONTENT_UPLOAD_DIR_DEFAULT = "../uploads/news-content/";
	public static final String PATH_NEWS_CONTENT_STORED_DEFAULT = "/uploads/news-content/";
	
	// default upload setting about us
	public static final String PATH_ABOUT_US_CONTENT_UPLOAD_DIR_DEFAULT = "../uploads/about-us-content/";
	public static final String PATH_ABOUT_US_CONTENT_STORED_DEFAULT = "/uploads/about-us-content/";

	// Message User
	public static final String MESSAGE_FAIL_VALIDATION_CONFIRM_PASSWORD_PASSWORD_USER = "Password and password confirmation do not match.";
	public static final String MESSAGE_SUCCESS_INSERT_NEW_USER = "Add new user successfully";
	public static final String MESSAGE_FAIL_INSERT_NEW_USER = "Add new user failed";

	public static final String MESSAGE_FAIL_VALIDATION_DUPLICATE_EMAIL_USER = "This email address is already in use. Please use a different email address";
	public static final String MESSAGE_SUCCESS_EDIT_USER = "Edit user successfully";
	public static final String MESSAGE_SUCCESS_EDIT_USER_ACCOUNT = "Edit account information successfully";
	public static final String MESSAGE_FAIL_VALIDATION_CONFIRM_PASSWORD = "Password confirmation does not match";

	// Message Member Lab Profile
	public static final String MESSAGE_SUCCESS_EDIT_MEMBER_LAB_PROFILE = "Edit member lab profile successfully";
	public static final String MESSAGE_FAIL_EDIT_MEMBER_LAB_PROFILE = "Edit member lab profile failed";
	public static final String MESSAGE_SUCCESS_ADD_MEMBER_LAB_PROFILE = "Add new member lab profile successfully";
	public static final String MESSAGE_FAIL_ADD_MEMBER_LAB_PROFILE = "Add new member lab profile failed";
	public static final String FORMAT_DISPLAY_PERIOD_MEMBER_MM_YYYY = "MM/yyyy";
	public static final String MESSAGE_FAIL_VALIDATION_LEAVE_DATE_MEMBER_LAB_PROFILE = "Graduation date must be after join date.";
	public static final String MESSAGE_FAIL_VALIDATION_JOIN_DATE_MEMBER_LAB_PROFILE = "The join date must be before the current date.";

	// Message Publication
	public static final String MESSAGE_SUCCESS_ADD_PUBLICATION = "Add new publication successfully";
	public static final String MESSAGE_FAIL_ADD_PUBLICATION = "Add new publication failed";
	public static final String MESSAGE_SUCCESS_EDIT_PUBLICATION = "Edit publication successfully";
	public static final String MESSAGE_FAIL_EDIT_PUBLICATION = "Edit publication failed";
	public static final String MESSAGE_FAIL_VALIDATION_DUPLICATE_TITLE_PUBLICATION = "This publication's title already exists";
	public static final String MESSAGE_FAIL_VALIDATION_DUPLICATE_DOI_LINK_PUBLICATION = "This DOI's link already exists";
	
	// Message Project
	public static final String MESSAGE_SUCCESS_ADD_PROJECT = "Add new project successfully";
	public static final String MESSAGE_FAIL_ADD_PROJECT = "Add new project failed";
	public static final String MESSAGE_SUCCESS_EDIT_PROJECT = "Edit project successfully";
	public static final String MESSAGE_FAIL_EDIT_PROJECT = "Edit project failed";
	public static final String MESSAGE_FAIL_VALIDATION_END_DATE_PROJECT = "End date must be after start date";
	public static final String MESSAGE_FAIL_VALIDATION_DUPLICATE_TITLE_PROJECT = "This project's title already exists";

	// Message News
	public static final String MESSAGE_SUCCESS_ADD_NEWS = "Add news successfully";
	public static final String MESSAGE_FAIL_ADD_NEWS = "Add news failed";
	public static final String MESSAGE_SUCCESS_EDIT_NEWS = "Edit news successfully";
	public static final String MESSAGE_FAIL_EDIT_NEWS = "Edit news failed";
	public static final String MESSAGE_FAIL_VALIDATION_DUPLICATE_TITLE_NEWS = "This news title already exists";
	
	
}
