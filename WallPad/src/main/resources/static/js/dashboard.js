// 날짜 업데이트
function updateClock() {
	const now = new Date();
	const daysOfWeek = ["일", "월", "화", "수", "목", "금", "토"];
	const dayOfWeek = daysOfWeek[now.getDay()];
	const year = now.getFullYear();
	const month = (now.getMonth() + 1).toString().padStart(2, '0');
	const day = now.getDate().toString().padStart(2, '0');
	const hours = now.getHours();
	const minutes = now.getMinutes().toString().padStart(2, '0');
	const seconds = now.getSeconds().toString().padStart(2, '0');

	let ampm = 'AM';
	let displayHours = hours;

	if (hours >= 12) {
		ampm = 'PM';
		displayHours = hours % 12;    // 나머지 값 가져오기
		if (displayHours === 0) {
			displayHours = 12;
		}
	}

	const timeString = `${year}-${month}-${day} (${dayOfWeek}) ${displayHours}:${minutes}:${seconds} ${ampm}`;
	$("#currentDate").text(timeString);
}

// 위치정보제공 거부 또는 오류 시 기본 위치 (의왕) 설정
const defaultLat = 37.3496;
const defaultLon = 126.9882;

// 위치 정보 가져오기
function getLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(updateWeatherAndAirQuality, handleError);
	} else {
		console.error("Geolocation is not supported by this browser.");
		alert("위치 정보를 가져올 수 없습니다.");
		updateWeatherAndAirQuality({ coords: { latitude: defaultLat, longitude: defaultLon } }); // 기본 위치로 날씨 및 미세먼지 정보 업데이트
	}
}


const cityNameTranslations = {
	"Seoul": "서울",
	"Busan": "부산",
	"Incheon": "인천",
	"Daegu": "대구",
	"Daejeon": "대전",
	"Gwangju": "광주",
	"Jeonju": "전주",
	"Suwon": "수원",
	"Ulsan": "울산",
	"Changwon": "창원",
	"Cheonan": "천안",
	"Cheongju": "청주",
	"Gimpo": "김포",
	"Goyang": "고양",
	"Seongnam": "성남",
	"Anyang": "안양",
	"Pyeongtaek": "평택",
	"Wonju": "원주",
	"Jinju": "진주",
	"Pohang": "포항",
	"Gwangmyeong": "광명",
	"Gimhae": "김해",
	"Jeju": "제주",
	"Iksan": "익산",
	"Bucheon": "부천",
	"Yangju": "양주",
	"Ansan": "안산",
	"Seongju": "성주",
	"Asan": "아산",
	"Yeosu": "여수",
	"Sokcho": "속초",
	"Gangneung": "강릉",
	"Chuncheon": "춘천",
	"Mokpo": "목포",
	"Kimje": "김제",
	"Gunsan": "군산",
	"Jeongeup": "정읍",
	"Namwon": "남원",
	"Yeongju": "영주",
	"Andong": "안동",
	"Gimcheon": "김천",
	"Jangseong": "장성",
	"Hwasun": "화순",
	"Sunchang": "순창",
	"Gochang": "고창",
	"Boseong": "보성",
	"Naju": "나주",
	"Miryang": "밀양",
	"Tongyeong": "통영",
	"Sacheon": "사천",
	"Boryeong": "보령",
	"Gyeongju": "경주",
	"Suwon": "수원",
	"Chuncheon": "춘천",
	"Jindo": "진도",
	"Uiryeong": "의령",
	"Ganghwa": "강화",
	"Sangju": "상주",
	"Gyeongsan": "경산",
	"Dalseo": "달서",
	"Yeongdeok": "영덕",
	"Jeongseon": "정선",
	"Incheon": "인천",
	"Seosan": "서산",
	"Jeongseon": "정선",
	"Hongseong": "홍성",
	"Gunpo": "군포"
	// 여기에 원하는 도시 이름을 추가하세요.
};

// 날씨 정보 업데이트
function updateWeatherAndAirQuality(position) {
	const lat = position.coords.latitude;
	const lon = position.coords.longitude;

	const weatherApiKey = 'fca26292966a38faa203666df43bd6fc';
	const weatherApiUrl = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${weatherApiKey}&units=metric&lang=kr`;

	fetch(weatherApiUrl)
		.then(response => response.json())
		.then(data => {
			let cityName = data.name;
			const weatherDescription = data.weather[0].description;
			const temperature = data.main.temp;
			let weatherIcon = '';

			// 도시명이 cityNameTranslations에 존재하면 변환, 아니면 그대로 사용
			if (cityNameTranslations[cityName]) {
				cityName = cityNameTranslations[cityName];
			}

			// 날씨에 맞는 아이콘 설정
			if (weatherDescription.includes('clear') || weatherDescription.includes('맑음')) {
				weatherIcon = '🌞'; // 맑은 날씨 아이콘
			} else if (weatherDescription.includes('cloud') || weatherDescription.includes('구름')) {
				weatherIcon = '🌥️'; // 구름 낀 날씨 아이콘
			} else if (weatherDescription.includes('rain') || weatherDescription.includes('비')) {
				weatherIcon = '🌧️'; // 비 아이콘
			} else if (weatherDescription.includes('snow') || weatherDescription.includes('눈')) {
				weatherIcon = '❄️'; // 눈 아이콘
			} else if (weatherDescription.includes('fog') || weatherDescription.includes('안개') || weatherDescription.includes('박무') || weatherDescription.includes('흐림') || weatherDescription.includes('온흐림') || weatherDescription.includes('mist')) {
				weatherIcon = '🌫️'; // 안개 아이콘
			} else if (weatherDescription.includes('thunderstorm') || weatherDescription.includes('천둥')) {
				weatherIcon = '🌩️'; // 천둥 아이콘
			} else if (weatherDescription.includes('dust') || weatherDescription.includes('sand') || weatherDescription.includes('황사')) {
				weatherIcon = '🌪️'; // 황사 아이콘
			} else if (weatherDescription.includes('hail') || weatherDescription.includes('우박')) {
				weatherIcon = '⛈️'; // 우박 아이콘
			}

			document.getElementById("cityName").textContent = `${cityName}`;
			document.getElementById("weather").innerHTML = `${weatherIcon} ${weatherDescription}`;
			document.getElementById("temperature").textContent = `${temperature}°C`;
		})
		.catch(error => {
			console.error("날씨 정보 오류:", error);
			document.getElementById("weather").textContent = "날씨 정보를 불러오는 데 실패했습니다.";
			document.getElementById("temperature").textContent = "";
		});


	// 미세먼지 정보 업데이트
	const airQualityApiKey = '13b12ee6-4aa4-47f8-9cf2-af383105b182';
	const airQualityApiUrl = `https://api.airvisual.com/v2/nearest_city?lat=${lat}&lon=${lon}&key=${airQualityApiKey}`;

	fetch(airQualityApiUrl)
		.then(response => response.json())
		.then(data => {
			const airQuality = data.data.current.pollution.aqius;
			let airQualityText = '';
			let airQualityIcon = ''; // 아이콘을 저장할 변수

			// 미세먼지 상태에 따라 텍스트와 아이콘 설정
			if (airQuality <= 50) {
				airQualityText = '좋음';
				airQualityIcon = '🌿';  // 식물 아이콘
			} else if (airQuality <= 100) {
				airQualityText = '보통';
				airQualityIcon = '😐';  // 무표정 아이콘
			} else if (airQuality <= 150) {
				airQualityText = '나쁨';
				airQualityIcon = '😷';  // 마스크 아이콘
			} else {
				airQualityText = '매우 나쁨';
				airQualityIcon = '⚠️';  // 경고 아이콘
			}

			// 미세먼지 상태와 아이콘을 화면에 표시
			document.getElementById("airQuality").innerHTML = `${airQualityIcon} ${airQualityText}`;
		})
		.catch(error => {
			console.error("미세먼지 정보 가져오기 오류:", error);
			document.getElementById("airQuality").textContent = "미세먼지 정보를 불러오는 데 실패했습니다.";
		});
}

// API 오류 처리
function handleError(error) {
	switch (error.code) {
		case error.PERMISSION_DENIED:
			alert("위치 정보 제공이 거부되었습니다. 기본 위치로 설정됩니다.");
			updateWeatherAndAirQuality({ coords: { latitude: defaultLat, longitude: defaultLon } }); // 기본 위치로 날씨 및 미세먼지 정보 업데이트
			break;
		case error.POSITION_UNAVAILABLE:
			alert("위치 정보를 사용할 수 없습니다. 기본 위치로 설정됩니다.");
			updateWeatherAndAirQuality({ coords: { latitude: defaultLat, longitude: defaultLon } }); // 기본 위치로 날씨 및 미세먼지 정보 업데이트
			break;
		case error.TIMEOUT:
			alert("위치 정보 가져오기 요청 시간이 초과되었습니다. 기본 위치로 설정됩니다.");
			updateWeatherAndAirQuality({ coords: { latitude: defaultLat, longitude: defaultLon } }); // 기본 위치로 날씨 및 미세먼지 정보 업데이트
			break;
		case error.UNKNOWN_ERROR:
			alert("알 수 없는 오류가 발생했습니다. 기본 위치로 설정됩니다.");
			updateWeatherAndAirQuality({ coords: { latitude: defaultLat, longitude: defaultLon } }); // 기본 위치로 날씨 및 미세먼지 정보 업데이트
			break;
	}
}


document.querySelectorAll('.notices-widget li').forEach(function(noticeItem) {
	noticeItem.addEventListener('click', function() {
		var fullContent = noticeItem.querySelector('.content').innerText; // 공지사항의 내용을 가져옴
		document.getElementById('modalContent').innerText = fullContent; // 모달 내용에 삽입

		var myModal = new bootstrap.Modal(document.getElementById('contentModal')); // 모달 객체 생성
		myModal.show(); // 모달 표시
	});
});


window.onload = function() {
	updateClock();
	setInterval(updateClock, 1000);
	getLocation();
}
