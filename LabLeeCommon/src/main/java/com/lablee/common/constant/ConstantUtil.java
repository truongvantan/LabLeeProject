package com.lablee.common.constant;

public class ConstantUtil {
	public static final String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static final String REGEX_PASSWORD_60 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,60}$";
	public static final String REGEX_PASSWORD_20 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$";
	public static final String REGEX_DATE_YYYY_MM_DD = "^\\d{4}-\\d{2}-\\d{2}$";
	public static final String REGEX_YEAR_4_DIGIT = "^(19|20)\\d{2}$";

	public static final long MAX_FILE_SIZE = 1_048_576L; // 1MB
	public static final int PAGE_SIZE_DEFAULT = 5;

	// Common Message
	public static final String MESSAGE_FAIL_INTERNAL_SERVER_ERROR = "Đã xảy ra lỗi không xác định trong hệ thống. Vui lòng thử lại";

	// Message biding result validation
	public static final String MESSAGE_FAIL_VALIDATION_EMAIL = "Email không đúng định dạng (vd: example@gmail.com)";
	public static final String MESSAGE_FAIL_VALIDATION_PASSWORD_20 = "Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 20 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.";
	public static final String MESSAGE_FAIL_VALIDATION_PASSWORD_60 = "Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 60 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.";

	public static final String MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_50 = "Vui lòng nhập tối đa 50 kí tự";
	public static final String MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_128 = "Vui lòng nhập tối đa 128 kí tự";
	public static final String MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_255 = "Vui lòng nhập tối đa 255 kí tự";
	public static final String MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_512 = "Vui lòng nhập tối đa 512 kí tự";
	public static final String MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_64 = "Vui lòng nhập tối đa 64 kí tự";
	public static final String MESSAGE_FAIL_VALIDATION_MIN_SIZE_INPUT_TEXT_3 = "Vui lòng nhập tối thiểu 03 kí tự";
	public static final String MESSAGE_FAIL_VALIDATION_BLANK_INPUT_TEXT = "Vui lòng không được để trống";
	public static final String MESSAGE_FAIL_VALIDATION_BINDING_RESULT = "Xảy ra lỗi binding result form";
	public static final String MESSAGE_FAIL_VALIDATION_NOT_NULL = "Vui lòng không được để trống";
	public static final String MESSAGE_FAIL_VALIDATION_DATE = "Ngày không đúng định dạng";
	public static final String MESSAGE_FAIL_VALIDATION_YEAR_4_DIGIT = "Năm không đúng định dạng (XXXX)";

	// Message FAIL
	// upload file
	public static final String MESSAGE_FAIL_VALIDATION_UPLOAD_FILE_SIZE_1MB = "Vui lòng chọn tệp có kích thước không vượt quá 1MB";
	
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

	// Message User
	public static final String MESSAGE_FAIL_VALIDATION_REPASSWORD_PASSWORD_USER = "Mật khẩu và xác nhận mật khẩu không trùng khớp";
	public static final String MESSAGE_SUCCESS_INSERT_NEW_USER = "Thêm mới người dùng thành công";
	public static final String MESSAGE_FAIL_INSERT_NEW_USER = "Thêm mới người dùng thất bại";

	public static final String MESSAGE_FAIL_VALIDATION_DUPLICATE_EMAIL_USER = "Email đã được sử dụng. Vui lòng chọn email khác";
	public static final String MESSAGE_SUCCESS_EDIT_USER = "Cập nhật người dùng thành công";
	public static final String MESSAGE_SUCCESS_EDIT_USER_ACCOUNT = "Thông tin tài khoản đã được cập nhật thành công";
	public static final String MESSAGE_FAIL_VALIDATION_CONFIRM_PASSWORD = "Xác nhận mật khẩu không trùng khớp";

	// Message Member Lab Profile
	public static final String MESSAGE_SUCCESS_EDIT_MEMBER_LAB_PROFILE = "Cập nhật thông tin thành viên lab thành công";
	public static final String MESSAGE_FAIL_EDIT_MEMBER_LAB_PROFILE = "Cập nhật thông tin thành viên lab thất bại";
	public static final String MESSAGE_SUCCESS_ADD_MEMBER_LAB_PROFILE = "Thêm mới thông tin thành viên lab thành công";
	public static final String MESSAGE_FAIL_ADD_MEMBER_LAB_PROFILE = "Thêm mới thông tin thành viên lab thất bại";
	public static final String FORMAT_DISPLAY_PERIOD_MEMBER_MM_YYYY = "MM/yyyy";
	public static final String MESSAGE_FAIL_VALIDATION_LEAVE_DATE_MEMBER_LAB_PROFILE = "Ngày tốt nghiệp phải sau ngày vào lab";
	public static final String MESSAGE_FAIL_VALIDATION_JOIN_DATE_MEMBER_LAB_PROFILE = "Ngày vào lab phải trước ngày hiện tại";

	// Message Publication
	public static final String MESSAGE_SUCCESS_ADD_PUBLICATION = "Thêm mới bài báo thành công";
	public static final String MESSAGE_FAIL_ADD_PUBLICATION = "Thêm mới bài báo thất bại";
	public static final String MESSAGE_SUCCESS_EDIT_PUBLICATION = "Cập nhật bài báo thành công";
	public static final String MESSAGE_FAIL_EDIT_PUBLICATION = "Cập nhật bài báo thất bại";

	// Message Project
	public static final String MESSAGE_SUCCESS_ADD_PROJECT = "Thêm mới dự án thành công";
	public static final String MESSAGE_FAIL_ADD_PROJECT = "Thêm mới dự án thất bại";
	public static final String MESSAGE_SUCCESS_EDIT_PROJECT = "Cập nhật dự án thành công";
	public static final String MESSAGE_FAIL_EDIT_PROJECT = "Cập nhật dự án thất bại";
	public static final String MESSAGE_FAIL_VALIDATION_END_DATE_PROJECT = "End date phải sau ngày Start Date";

	// Message News
	public static final String MESSAGE_SUCCESS_ADD_NEWS = "Thêm mới bài đăng thành công";
	public static final String MESSAGE_FAIL_ADD_NEWS = "Thêm mới bài đăng thất bại";
	public static final String MESSAGE_SUCCESS_EDIT_NEWS = "Cập nhật bài đăng thành công";
	public static final String MESSAGE_FAIL_EDIT_NEWS = "Cập nhật bài đăng thất bại";
	public static final String MESSAGE_FAIL_VALIDATION_DUPLICATE_TITLE_NEWS = "Tiêu đề đã tồn tại";
}
