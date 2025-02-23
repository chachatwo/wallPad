$(document).ready(function() {
	var table = $('#dataTable').DataTable({
		"pageLength": 10,
		"lengthChange": true,
		"pagingType": "full_numbers",
		"language": {
			"paginate": {
				"first": "첫 페이지",
				"previous": "이전",
				"next": "다음",
				"last": "마지막"
			},
			"info": "페이지 _PAGE_ / _PAGES_",
			"infoEmpty": "데이터가 없습니다.",
			"zeroRecords": "검색 결과가 없습니다."
		},
		"searching": true,
		"order": [[3, 'desc']],
//페이지 번호 매김 함수
		"rowCallback": function(row, data, index) {
			var pageInfo = this.api().page.info();
			var rowNumber = pageInfo.page * pageInfo.length + index + 1;

			$(row).children().first().html(rowNumber);
		}
	});

	$('.dataTables_filter').hide();

	$('input[type="text"][aria-label="Search"]').on('keyup', function() {
		table.search(this.value).draw();
	});
});

//셀 클릭 시 모든 내용 가져오기
document.querySelectorAll('.table td:nth-child(3)').forEach(function(cell) {
	cell.addEventListener('click', function() {
		var fullContent = cell.innerText;
		document.getElementById('modalContent').innerText = fullContent;

		var myModal = new bootstrap.Modal(document.getElementById('contentModal'));
		myModal.show();
	});
});
