window.onload = function() {
	
	var cookieData = document.cookie;
	
// 	alert(cookieData);
	
	const getCookie = (name) => (
			  document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)')?.pop() || ''
			)
	
	if(getCookie("id") != "") {
		document.getElementById("id").value = getCookie("id");
		document.getElementById("rememberId").checked = true;
	}		
}