/*var browserVersion = window.navigator.userAgent.toUpperCase();
var isOpera = browserVersion.indexOf("OPERA") > -1 ? true : false;
var isFireFox = browserVersion.indexOf("FIREFOX") > -1 ? true : false;
var isChrome = browserVersion.indexOf("CHROME") > -1 ? true : false;
var isSafari = browserVersion.indexOf("SAFARI") > -1 ? true : false;
var isIE = (!!window.ActiveXObject || "ActiveXObject" in window);
var isIE9More = (!-[1, ] == false);

var count = 0;
var interval = null;

function startInit(iframeId, minHeight) {
    eval("window.IE9MoreRealHeight" + iframeId + "=0");
    interval = setInterval("reinitIframe('" + iframeId + "'," + minHeight + ")", 100);
}

function reinitIframe(iframeId, minHeight) {
	count += 100;
	if(count > 300){
		clearInterval(interval);
	}
	try {
		var iframe = window.parent.document.getElementById(iframeId);
		var bHeight = 0;
		var dHeight = 0;
		if(iframe){
			if(isChrome == false && isSafari == false) {
				try {
					bHeight = iframe.contentWindow.document.body.scrollHeight;
				} catch(ex) {
				}
			}
			
			if(isFireFox == true) {
				dHeight = iframe.contentWindow.document.documentElement.offsetHeight + 2; //如果火狐浏览器高度不断增加删除+2
			} else if(isIE == false && isOpera == false && iframe.contentWindow) {
				try {
					dHeight = iframe.contentWindow.document.body.scrollHeight;
				} catch(ex) {console.info(ex);}
			} else if(isIE == true && isIE9More) { //ie9+
				var heightDeviation = bHeight - eval("window.IE9MoreRealHeight" + iframeId);
				if(heightDeviation == 0) {
					bHeight += 3;
				} else if(heightDeviation != 3) {
					eval("window.IE9MoreRealHeight" + iframeId + "=" + bHeight);
					bHeight += 3;
				}
			} else {
				bHeight += 3;
			}
			
			var height = Math.max(bHeight, dHeight);
			if(height < minHeight) height = minHeight;
			iframe.style.height = height + "px";
		}
	} catch(ex) {
	}
}

startInit('deal_case', 200);*/

//console.info($(window.parent.document).find("#deal_case"));
//console.info($(".tab-con"));
$(window.parent.document).find("#deal_case").height($(".tab-con").height() + 150);