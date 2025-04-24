window.addEventListener("DOMContentLoaded", function() {
	const socket = new SockJS("/ws");
	const stompClient = Stomp.over(socket);

	stompClient.connect({}, function(frame) {
		stompClient.subscribe("/user/queue/alerts", function(notification) {
		    const data = JSON.parse(notification.body);
		    addNotification(data.message);
		    loadLatestNotifications();
		});
	});

	loadUnreadCount();
	loadLatestNotifications();
});

function loadUnreadCount() {
	$.ajax({
		url: "/api/notifications/unread-count",
		type: "GET",
		success: function(count) {
			$("#notificationCount").text(count);
		}
	});
}

function loadLatestNotifications() {
	$.ajax({
		url: "/api/notifications/latest",
		type: "GET",
		success: function(data) {
			const latestList = document.getElementById("latestNotificationList");
			if (!latestList) return;
			latestList.innerHTML = "";
			data.forEach(item => {
				const li = document.createElement("li");
				li.classList.add("list-group-item");
				li.textContent = item.message;
				latestList.appendChild(li);
			});
		}
	});
}

function toggleNotificationList() {
	const list = document.getElementById("notificationList");
	const items = document.getElementById("notificationItems");
	const count = document.getElementById("notificationCount");

	if (list.style.display === "none") {
		items.innerHTML = "";

		$.ajax({
			url: "/api/notifications/unread",
			type: "GET",
			success: function(data) {
				if (data.length === 0) {
					const li = document.createElement("li");
					li.classList.add("list-group-item", "text-muted", "text-center");
					li.textContent = "모든 알림을 확인했습니다 😊";
					items.appendChild(li);
				} else {
					data.forEach((item) => {
						const li = document.createElement("li");
						li.classList.add("list-group-item");
						li.textContent = item.message;
						items.appendChild(li);
					});
				}

				list.style.display = "block";
				count.textContent = "0";

				$.ajax({
					url: "/api/notifications/read",
					type: "POST"
				});
			}
		});
	} else {
		list.style.display = "none";
	}
}

function addNotification(message) {
	const list = document.getElementById("notificationItems");
	const count = document.getElementById("notificationCount");

	const item = document.createElement("li");
	item.classList.add("list-group-item");
	item.textContent = message;
	list.prepend(item);

	const current = parseInt(count.textContent) || 0;
	count.textContent = current + 1;
}


document.addEventListener("click", function (event) {
	const list = document.getElementById("notificationList");
	const icon = document.getElementById("notificationIcon");

	if (list.style.display === "block" && !icon.contains(event.target) && !list.contains(event.target)) {
		list.style.display = "none";
	}
});
