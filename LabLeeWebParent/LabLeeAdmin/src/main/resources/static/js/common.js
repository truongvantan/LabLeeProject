function showImageThumbnail(fileInput) {
	const file = fileInput.files[0];
	const reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	}
	reader.readAsDataURL(file);
}

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal('show');
}

function showErrorModal(message) {
	showModalDialog("Lỗi", message);
}

function showWarningModal(message) {
	showModalDialog("Cảnh báo", message);
}

function showConfirmModal(link, entityName, message) {
	entityId = link.attr("entityId");
	$("#confirmButton").attr("href", link.attr("href"));
	$("#confirmText").text(message + " " + entityName + " ID " + entityId + "?");
	$("#confirmModal").modal("show");
}