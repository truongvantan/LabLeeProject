function uploadImageTinyMCE(urlUploadImage) {
	const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
	const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

	tinymce.init({
		selector: 'textarea.tinymce-editor',
		height: 750,
		plugins: 'image link media table code lists',
		toolbar: 'undo redo | bold italic underline | link image media | code | bullist numlist',
		menubar: true,
		automatic_uploads: true,
		relative_urls: false,
		remove_script_host: true,
		convert_urls: true,
		images_upload_handler: function(blobInfo) {
			const file = blobInfo.blob();
			const maxSize = 1 * 1024 * 1024; // 1MB

			if (file.size > maxSize) {
				return Promise.reject('Image file size must be less than 1MB!');
			}

			const formData = new FormData();
			formData.append('upload', file);

			return fetch(urlUploadImage, {
				method: 'POST',
				body: formData,
				headers: { [csrfHeader]: csrfToken }
			})
				.then(response => response.json())
				.then(data => {
					if (data.url) {
						return data.url;
					} else {
						return Promise.reject(data.error || 'Upload fail');
					}
				})
				.catch(err => Promise.reject('Upload fail: ' + err));
		},

		file_picker_types: 'image',
		file_picker_callback: function(callback, value, meta) {
			if (meta.filetype === 'image') {
				const url = prompt('Enter image URL:');
				if (url) {
					callback(url, { alt: '' });
				}
			}
		},

		images_reuse_filename: false
	});
}