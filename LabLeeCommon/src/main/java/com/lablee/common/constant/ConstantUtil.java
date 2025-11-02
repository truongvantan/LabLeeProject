package com.lablee.common.constant;

public class ConstantUtil {
	public static final String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static final String REGEX_PASSWORD_60 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,60}$";
	public static final String REGEX_PASSWORD_20 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$";
	
	public static final long MAX_FILE_SIZE = 1_048_576L; // 1MB
	public static final int USER_PAGE_SIZE = 5;
	
	public static final String MESSAGE_VALIDATION_EMAIL_FAIL = "Email không đúng định dạng (vd: example@gmail.com)";
	public static final String MESSAGE_VALIDATION_PASSWORD_20_FAIL = "Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 20 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.";
	public static final String MESSAGE_VALIDATION_PASSWORD_60_FAIL = "Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 60 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.";
	
	public static final String MESSAGE_VALIDATION_MAX_SIZE_INPUT_TEXT_50_FAIL = "Vui lòng nhập tối đa 50 kí tự";
	public static final String MESSAGE_VALIDATION_MAX_SIZE_INPUT_TEXT_255_FAIL = "Vui lòng nhập tối đa 255 kí tự";
	public static final String MESSAGE_VALIDATION_MAX_SIZE_INPUT_TEXT_64_FAIL = "Vui lòng nhập tối đa 64 kí tự";
	public static final String MESSAGE_VALIDATION_MIN_SIZE_INPUT_TEXT_3_FAIL = "Vui lòng nhập tối thiểu 03 kí tự";
	public static final String MESSAGE_VALIDATION_BLANK_INPUT_TEXT_FAIL = "Vui lòng không được để trống";
	
	public static final String MESSAGE_VALIDATION_UPLOAD_FILE_SIZE_1MB_FAIL = "Vui lòng chọn tệp có kích thước không vượt quá 1MB";
	
	public static final String PATH_USER_PHOTO_UPLOAD_DEFAULT = "user-photos";
	
	public static final String MESSAGE_VALIDATION_REPASSWORD_PASSWORD_USER_FAIL = "Mật khẩu và xác nhận mật khẩu không trùng khớp";
	public static final String MESSAGE_SUCCESS_INSERT_NEW_USER = "Thêm mới người dùng thành công";
	public static final String MESSAGE_FAIL_INSERT_NEW_USER = "Thêm mới người dùng thất bại";
	
	public static final String MESSAGE_VALIDATION_BINDING_RESULT_FAIL = "Xảy ra lỗi binding result form";
	
	public static final String MESSAGE_INTERNAL_SERVER_ERROR = "Đã xảy ra lỗi không xác định trong hệ thống. Vui lòng thử lại";
	public static final String MESSAGE_VALIDATION_DUPLICATE_EMAIL_USER = "Email đã được sử dụng. Vui lòng chọn email khác";
	public static final String MESSAGE_SUCCESS_EDIT_USER = "Cập nhật người dùng thành công";

}
