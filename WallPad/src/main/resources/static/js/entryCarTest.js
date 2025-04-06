

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