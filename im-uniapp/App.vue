<script>
	import store from './store';
	import http from './common/request';
	import * as enums from './common/enums';
	import * as wsApi from './common/wssocket';
	import UNI_APP from '@/.env.js'

	export default {
		data() {
			return {
				audioTip: null
			}
		},
		methods: {
			init() {
				// 加载数据
				store.dispatch("load").then(() => {
					// 审核
					// this.initAudit();
					// 初始化websocket
					this.initWebSocket();
				}).catch((e) => {
					console.log(e);
					this.exit();
				})
			},
			initWebSocket() {
				let loginInfo = uni.getStorageSync("loginInfo")
				wsApi.init();
				wsApi.connect(UNI_APP.WS_URL, loginInfo.accessToken);
				wsApi.onConnect(() => {
					// 加载离线消息
					this.pullPrivateOfflineMessage(store.state.chatStore.privateMsgMaxId);
					this.pullGroupOfflineMessage(store.state.chatStore.groupMsgMaxId);
				});
				wsApi.onMessage((cmd, msgInfo) => {
					if (cmd == 2) {
						// 异地登录，强制下线
						uni.showModal({
							content: '登陆超时，将被强制下线',
							showCancel: false,
						})
						console.log("登陆超时，将被强制下线");
						wsApi.close(1000);
						uni.removeStorageSync("loginInfo");
						uni.reLaunch({
							url: "/pages/login/blank"
						})
						store.dispatch("unload");
					} else if (cmd == 3) {
						// 私聊消息
						this.handlePrivateMessage(msgInfo);
					} else if (cmd == 4) {
						// 群聊消息
						this.handleGroupMessage(msgInfo);
					}
				});
				wsApi.onClose((res) => {
					// 1000是客户端正常主动关闭
					if (res.code != 1000) {
						// 重新连接
						uni.showToast({
							title: '连接已断开，尝试重新连接...',
							icon: 'none',
						})
						let loginInfo = uni.getStorageSync("loginInfo")
						wsApi.reconnect(UNI_APP.WS_URL, loginInfo.accessToken);
					}
				})
			},
			pullPrivateOfflineMessage(minId) {
				http({
					url: "/message/private/pullOfflineMessage?minId=" + minId,
					method: 'get'
				});
			},
			pullGroupOfflineMessage(minId) {
				http({
					url: "/message/group/pullOfflineMessage?minId=" + minId,
					method: 'get'
				});
			},
			handlePrivateMessage(msg) {
				// 消息加载标志
				if (msg.type == enums.MESSAGE_TYPE.LOADDING) {
					store.commit("loadingPrivateMsg", JSON.parse(msg.content))
					return;
				}
				// 消息已读处理，清空已读数量
				if (msg.type == enums.MESSAGE_TYPE.READED) {
					store.commit("resetUnreadCount", {
						type: 'PRIVATE',
						targetId: msg.recvId
					})
					return;
				}
				// 消息回执处理,改消息状态为已读
				if (msg.type == enums.MESSAGE_TYPE.RECEIPT) {
					store.commit("readedMessage", {
						friendId: msg.sendId
					})
					return;
				}
				// 标记这条消息是不是自己发的
				msg.selfSend = msg.sendId == store.state.userStore.userInfo.id;
				// 好友id
				let friendId = msg.selfSend ? msg.recvId : msg.sendId;
				this.loadFriendInfo(friendId).then((friend) => {
					this.insertPrivateMessage(friend, msg);
				})

			},
			insertPrivateMessage(friend, msg) {
				// webrtc 信令
				if (msg.type >= enums.MESSAGE_TYPE.RTC_CALL_VOICE &&
					msg.type <= enums.MESSAGE_TYPE.RTC_CANDIDATE) {
					// #ifdef MP-WEIXIN
					// 小程序不支持音视频
					return;
					// #endif
					// 被呼叫，弹出视频页面
					if (msg.type == enums.MESSAGE_TYPE.RTC_CALL_VOICE ||
						msg.type == enums.MESSAGE_TYPE.RTC_CALL_VIDEO) {
						let mode = msg.type == enums.MESSAGE_TYPE.RTC_CALL_VIDEO ? "video" : "voice";
						let pages = getCurrentPages();
						let curPage = pages[pages.length - 1].route;
						if (curPage != "pages/chat/chat-video") {
							const friendInfo = encodeURIComponent(JSON.stringify(friend));
							uni.navigateTo({
								url: `/pages/chat/chat-video?mode=${mode}&friend=${friendInfo}&isHost=false`
							})
						}
					}
					setTimeout(() => {
						uni.$emit('WS_RTC', msg);
					}, 500)
					return;
				}

				let chatInfo = {
					type: 'PRIVATE',
					targetId: friend.id,
					showName: friend.nickName,
					headImage: friend.headImage
				};
				// 打开会话
				store.commit("openChat", chatInfo);
				// 插入消息
				store.commit("insertMessage", msg);
				// 播放提示音
				!msg.selfSend && this.playAudioTip();

			},
			handleGroupMessage(msg) {
				// 消息加载标志
				if (msg.type == enums.MESSAGE_TYPE.LOADDING) {
					store.commit("loadingGroupMsg", JSON.parse(msg.content))
					return;
				}
				// 消息已读处理
				if (msg.type == enums.MESSAGE_TYPE.READED) {
					// 我已读对方的消息，清空已读数量
					let chatInfo = {
						type: 'GROUP',
						targetId: msg.groupId
					}
					store.commit("resetUnreadCount", chatInfo)
					return;
				}
				// 消息回执处理
				if (msg.type == enums.MESSAGE_TYPE.RECEIPT) {
					// 更新消息已读人数
					let msgInfo = {
						id: msg.id,
						groupId: msg.groupId,
						readedCount: msg.readedCount,
						receiptOk: msg.receiptOk
					};
					store.commit("updateMessage", msgInfo)
					return;
				}
				// 标记这条消息是不是自己发的
				msg.selfSend = msg.sendId == store.state.userStore.userInfo.id;
				this.loadGroupInfo(msg.groupId).then((group) => {
					// 插入群聊消息
					this.insertGroupMessage(group, msg);
				})

			},
			insertGroupMessage(group, msg) {
				let chatInfo = {
					type: 'GROUP',
					targetId: group.id,
					showName: group.remark,
					headImage: group.headImageThumb
				};
				// 打开会话
				store.commit("openChat", chatInfo);
				// 插入消息
				store.commit("insertMessage", msg);
				// 播放提示音
				!msg.selfSend && this.playAudioTip();
			},
			loadFriendInfo(id) {
				return new Promise((resolve, reject) => {
					let friend = store.state.friendStore.friends.find((f) => f.id == id);
					if (friend) {
						resolve(friend);
					} else {
						http({
							url: `/friend/find/${id}`,
							method: 'get'
						}).then((friend) => {
							store.commit("addFriend", friend);
							resolve(friend)
						})
					}
				});
			},
			loadGroupInfo(id) {
				return new Promise((resolve, reject) => {
					let group = store.state.groupStore.groups.find((g) => g.id == id);
					if (group) {
						resolve(group);
					} else {
						http({
							url: `/group/find/${id}`,
							method: 'get'
						}).then((group) => {
							resolve(group)
							store.commit("addGroup", group);
						})
					}
				});
			},
			exit() {
				console.log("exit");
				wsApi.close(1000);
				uni.removeStorageSync("loginInfo");
				uni.reLaunch({
					url: "/pages/login/login"
				})
				store.dispatch("unload");
			},
			playAudioTip() {
				// 音频播放无法成功
				// this.audioTip = uni.createInnerAudioContext();
				// this.audioTip.src =  "/static/audio/tip.wav";
				// this.audioTip.play();
			},
			initAudit() {
				if (store.state.userStore.userInfo.type == 1) {
					// 显示群组功能
					uni.setTabBarItem({
						index: 2,
						text: "群聊"
					})
				} else {
					// 隐藏群组功能
					uni.setTabBarItem({
						index: 2,
						text: "搜索"
					})
				}
			}
		},
		onLaunch(e) {
			if (window.location.href.includes('?') && window.location.href.includes('terminal=1')) {
				console.log('terminal');
				if(window.location.href.includes('#/pages/chat/chat-box')){
					window.location.href = window.location.href.substr(0,window.location.href.indexOf('#'));
				}
				
				uni.setStorageSync("initWelcomeMsgFlg", "1");
				uni.removeStorageSync("loginInfo")
				uni.removeStorageSync("autoLogin")
				const params = new URLSearchParams(window.location.search);
				var paramStr = '{';
				for (const [key, value] of params) {
					paramStr += '"' + key + '":"' + value + '",';
				}
				paramStr = paramStr.substr(0,paramStr.length-1) + '}';
				uni.navigateTo({
					url: "/pages/login/login?item="+ encodeURIComponent(paramStr)
				})
				return
			}else{
				// 登录状态校验
				if (uni.getStorageSync("loginInfo")) {
					// 初始化
					this.init()
				} else {
					// 跳转到登录页
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}
			}

		}
	}
</script>

<style lang="scss">
	@import url('./static/icon/iconfont.css');

	.tab-page {
		// #ifdef H5
		height: calc(100vh - 46px - 50px); // h5平台100vh是包含了顶部和底部，需要减去
		// #endif
		// #ifndef H5
		height: calc(100vh);
		// #endif
		background-color: #f8f8f8;
	}

	.page {
		// #ifdef H5
		height: calc(100vh - 45px); // h5平台100vh是包含了顶部，需要减去
		// #endif
		// #ifndef H5
		height: calc(100vh);
		// #endif
		background-color: #f8f8f8;
	}
</style>