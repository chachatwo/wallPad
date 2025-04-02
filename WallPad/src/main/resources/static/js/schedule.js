let schedules = [];
let selectedMonth = new Date().getMonth();

const monthElement = document.getElementById("month");
const calendarBox = document.getElementById("calendar");
const monthSelect = document.getElementById("monthSelect");

const scheduleCache = {};

function fetchSchedules() {
	const cacheKey = `${new Date().getFullYear()}-${selectedMonth + 1}`;
	const cached = scheduleCache[cacheKey];

	const now = Date.now();
	const fiveMinutes = 5 * 60 * 1000;

	console.log(scheduleCache);

	if (cached && now - cached.cachedAt < fiveMinutes) {
		schedules = cached.data;
		renderMonth(selectedMonth);
		renderCalendar();
	} else {
		$.ajax({
			url: '/api/schedules',
			method: 'GET',
			contentType: 'application/json',
			success: function(response) {
				scheduleCache[cacheKey] = {
					data: response,
					cachedAt: now
				};
				schedules = response;
				renderMonth(selectedMonth);
				renderCalendar();
			},
			error: function(xhr, status, error) {
				console.error('요청에 실패하였습니다.', error);
			}
		});
	}
}

function onMonthChange() {
	selectedMonth = parseInt(monthSelect.value);
	renderMonth(selectedMonth);
	renderCalendar();
}

function renderMonth(month) {
	const monthNames = ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"];
	monthElement.innerHTML = `<h2>${monthNames[month]}</h2>`;
}

function renderCalendar() {
	const date = new Date();
	const year = date.getFullYear();
	const lastDate = new Date(year, selectedMonth + 1, 0).getDate();
	const firstDay = new Date(year, selectedMonth, 1).getDay(); // 0 = SUN

	let calendar = "<table><tr><th>SUN</th><th>MON</th><th>TUE</th><th>WED</th><th>THU</th><th>FRI</th><th>SAT</th></tr><tr>";
	let day = 1;

	for (let i = 0; i < firstDay; i++) {
		calendar += "<td></td>";
	}

	for (let i = 0; day <= lastDate; i++) {
		const dateString = formatDate(day, selectedMonth);
		const hasSchedule = schedules.some(schedule => schedule.maintenanceDate === dateString);

		calendar += `<td class="${hasSchedule ? 'has-schedule' : ''}">${day}</td>`;

		if ((i + firstDay + 1) % 7 === 0) {
			calendar += "</tr><tr>";
		}

		day++;
	}

	calendar += "</tr></table>";
	calendarBox.innerHTML = calendar;

	$("#calendar td").click(function() {
		showModal($(this).text());
	});
}


function formatDate(day, month) {
	return `${new Date().getFullYear()}-${(month + 1).toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
}

function showModal(day) {
	const modal = $('#scheduleModal');
	const modalText = $('#modalText');
	const formattedDate = formatDate(day, selectedMonth);

	const allSchedules = schedules.filter(sch => sch.maintenanceDate === formattedDate);

	if (allSchedules.length > 0) {
		let scheduleDetails = `날짜: ${formattedDate} <br><br>`;
		allSchedules.forEach(schedule => {
			scheduleDetails += `제목: ${schedule.title} <br>
                          상세: ${schedule.description} <br>
                          시작 시간: ${schedule.startTime} <br>
                          종료 시간: ${schedule.endTime} <br><br>`;
		});
		modalText.html(scheduleDetails);
	} else {
		modalText.html(`날짜: ${formattedDate} <br> 일정이 없습니다.`);
	}

	modal.show();
}

$('.close-btn').click(function() {
	$('#scheduleModal').hide();
});

window.onload = function() {
	const currentMonth = new Date().getMonth();
	const monthSelect = document.getElementById("monthSelect");

	monthSelect.value = currentMonth;

	fetchSchedules();
};
