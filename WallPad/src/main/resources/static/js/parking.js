
$(document).ready(function() {
	$('#entryForm').submit(function(event) {
		event.preventDefault();

		var carNumber = $('#carNumber').val();
		var entryTime = $('#entryTime').val().replace(" ", "T");

		$.ajax({
			url: '/api/register/car',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({
				carNumber: carNumber,
				entryTime: entryTime
			}),
			success: function(response) {
				if (response) {
					console.log("입차가 완료되었습니다.");
				} else {
					console.log("입차할 수 없는 차량입니다.");
				}
			},
			error: function(xhr, status, error) {
				console.error("입차 등록 중 오류가 발생했습니다.");
			}
		});
	});
});



function openModal() {
	document.getElementById("reserveModal").style.display = "block";
}

function closeModal() {
	document.getElementById("reserveModal").style.display = "none";
}


window.onload = function() {
	const messageElement = document.getElementById('hiddenMessage');
	if (messageElement && messageElement.innerText.trim() !== '') {
		const message = messageElement.innerText;
		document.getElementById('modalMessage').innerText = message;
		document.getElementById('messageModal').style.display = 'block';
	}
};

function closeModal2() {
	document.getElementById("messageModal").style.display = "none";
}

function openModal2() {
	document.getElementById("statusModal").style.display = "block";
}

function closeModal3() {
	document.getElementById("statusModal").style.display = "none";
}


$("#statusButton").on("click", function() {
	$.ajax({
		url: "/api/parking/reserve/states",
		method: "POST",
		contentType: 'application/json',
		success: function(response) {
			var tableBody = $("#reserveStatusTableBody");
			tableBody.empty();

			if (response.length === 0) {
				var row = "<tr>";
				row += "<td class='text-center'>현재 차량 예약이 없습니다</td>";
				row += "<td class='text-center'>미정</td>";
				row += "</tr>";
				tableBody.append(row);
			} else {
				for (var i = 0; i < response.length; i++) {
					var reservation = response[i];
					var formattedDate = formatDate(reservation.allowedPeriod);

					var row = "<tr>";
					row += "<td>" + reservation.carNumber + "</td>";
					row += "<td>" + formattedDate + "</td>";
					row += "</tr>";
					tableBody.append(row);
				}
			}
		},
		error: function(response) {
		}
	});
});


function formatDate(dateString) {
	const date = new Date(dateString);
	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');
	const hours = String(date.getHours()).padStart(2, '0');
	const minutes = String(date.getMinutes()).padStart(2, '0');
	const seconds = String(date.getSeconds()).padStart(2, '0');

	return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}


window.onclick = function(event) {
	var reserveModal = document.getElementById("reserveModal");
	if (event.target === reserveModal) {
		closeModal();
	}

	var messageModal = document.getElementById("messageModal");
	if (event.target === messageModal) {
		closeModal2();
	}

	var statusModal = document.getElementById("statusModal");
	if (event.target === statusModal) {
		closeModal3();
	}

};


