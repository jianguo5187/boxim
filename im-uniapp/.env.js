//设置环境(打包前修改此变量)
const ENV = "PROD";
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
	UNI_APP.BASE_URL = "http://43.132.126.240/api";
	UNI_APP.WS_URL = "ws://43.132.126.240:8878/im";
}
export default UNI_APP