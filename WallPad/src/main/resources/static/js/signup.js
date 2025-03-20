function validateApartNum() {
	const apartNum = document.getElementById('apartmentNumber').value;
	const errorMessage = document.getElementById('errorMessage');

	// 입력값이 비어 있을 때 에러 메시지 제거
	if (apartNum.trim() === "") {
		errorMessage.textContent = "";
		return;
	}

	const apartNumPattern = /^\d{3}동\d{3,4}호$/;

	if (!apartNumPattern.test(apartNum)) {
		errorMessage.textContent = "동호수 입력 형식이 올바르지 않습니다. 예: 000동000호 또는 000동0000호";
	} else {
		errorMessage.textContent = ""; 
	}
}

$(document).ready(function() {
	let isUsernameAvailable = true;
	let isEmailAvailable = true;

	// 아이디 입력 시 실시간으로 중복 확인
	$("#username").on("blur", function() {
		const username = $(this).val();

		// 아이디 값이 비어있으면 메시지 지우기
		if (username === "") {
			$("#idStatus").text("");
			isUsernameAvailable = true;
			enableSignupButton();
			return;
		}

		$.ajax({
			url: "/check-username", // 서버의 아이디 중복 확인 
			method: "GET",
			data: { username: username },
			success: function(response) {
				$("#idStatus").text(response);
				if (response.includes("사용 가능")) {
					isUsernameAvailable = true;
				} else {
					isUsernameAvailable = false;
				}
				enableSignupButton();
			},
			error: function() {
				alert("서버 오류로 중복 확인을 할 수 없습니다.");
			}
		});
	});

	// 이메일 중복 확인
	$("#email").on("blur", function() {
		const email = $(this).val();

		if (!validateEmailFormat(email)) {
			$("#emailStatus").text("유효하지 않은 이메일 형식입니다.");
			isEmailAvailable = false;
			enableSignupButton();
			return;
		}


		// 이메일 값이 비어있으면 메시지 지우기
		if (email === "") {
			$("#emailStatus").text("");
			isEmailAvailable = true;
			enableSignupButton();
			return;
		}

		$.ajax({
			url: "/check-email", // 서버의 이메일 중복 확인 
			method: "GET",
			data: { email: email },
			success: function(response) {
				$("#emailStatus").text(response);
				if (response.includes("사용 가능")) {
					isEmailAvailable = true;
				} else {
					isEmailAvailable = false;
				}
				enableSignupButton();
			},
			error: function() {
				alert("서버 오류로 중복 확인을 할 수 없습니다.");
			}
		});
	});

	// 회원가입 버튼 활성화/비활성화
	function enableSignupButton() {
		if (isUsernameAvailable && isEmailAvailable) {
			$(".btn-signup").prop("disabled", false); // 활성화
		} else {
			$(".btn-signup").prop("disabled", true); // 비활성화
		}
	}


	// 전화번호 입력 후 자동으로 '-' 추가
	document.querySelector("input[name='phone_num']").addEventListener("input", function() {
		let phone = this.value.replace(/[^0-9]/g, '');

		// 최대 11자리
		if (phone.length > 11) {
			phone = phone.slice(0, 11);
		}

		// 10자리 전화번호
		if (phone.length === 10) {
			this.value = phone.slice(0, 3) + '-' + phone.slice(3, 6) + '-' + phone.slice(6); // 3-3-4
		}
		// 11자리 전화번호
		else if (phone.length === 11) {
			this.value = phone.slice(0, 3) + '-' + phone.slice(3, 7) + '-' + phone.slice(7); // 3-4-4
		}
		else {
			this.value = phone;
		}
	});

	document.getElementById("signupForm").addEventListener("submit", function(event) {
		let phone = document.querySelector("input[name='phone_num']").value.replace(/[^0-9]/g, ''); // '-' 제거
		if (phone.length !== 10 && phone.length !== 11) {
			alert("전화번호는 10자리 또는 11자리로 입력해야 합니다.");
			event.preventDefault();
		}

		if (!isUsernameAvailable || !isEmailAvailable) {
			alert("아이디 또는 이메일이 중복됩니다. 확인 후 다시 시도해 주세요.");
			event.preventDefault();
		}
	});
});

function validateEmailFormat(email) {
	const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;  // 이메일 정규식
	return regex.test(email);
}
