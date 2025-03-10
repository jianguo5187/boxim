//设置环境(打包前修改此变量)
const ENV = "DEV";
const UNI_APP = {}
if(ENV=="DEV"){
	UNI_APP.BASE_URL = "http://127.0.0.1:8888";
	UNI_APP.WS_URL = "ws://127.0.0.1:8878/im";
	// H5 走本地代理解决跨域问题
	// #ifdef H5
		UNI_APP.BASE_URL = "/api";
	// #endif
}
if(ENV=="PROD"){
	// UNI_APP.BASE_URL = "http://43.159.197.22:82/api";
	// UNI_APP.WS_URL = "ws://43.159.197.22:8878/im";
	// H5 走本地代理解决跨域问题
	// #ifdef H5
		UNI_APP.BASE_URL = "/api";
	// #endif
	// UNI_APP.BASE_URL = "http://43.129.82.76:81/api";
	UNI_APP.WS_URL = "ws://8.212.123.221:8878/im";
	// UNI_APP.BASE_URL = "http://43.132.168.72:82/api";
	// UNI_APP.WS_URL = "ws://43.132.168.72:8878/im";
	// UNI_APP.WS_URL = "ws://47.57.188.97:8878/im";
	// UNI_APP.BASE_URL = "https://ngbt.qdcspy.shop/api";
	// UNI_APP.WS_URL = "wss://ngbt.qdcspy.shop/im";
}
export default UNI_APP