<template>
	<el-container class="home-page">
		<el-aside width="80px" class="navi-bar">
			<div class="user-head-image">
				<head-image :name="$store.state.userStore.userInfo.nickName"
					:url="$store.state.userStore.userInfo.headImageThumb" :size="60"
					@click.native="showSettingDialog = true">
				</head-image>
			</div>
			<el-menu background-color="#333333" text-color="#ddd" style="margin-top: 30px;">
				<el-menu-item title="聊天">
					<router-link v-bind:to="'/home/chat'">
						<span class="el-icon-chat-dot-round"></span>
						<div v-show="unreadCount > 0" class="unread-text">{{ unreadCount }}</div>
					</router-link>
				</el-menu-item>
				<el-menu-item title="好友">
					<router-link v-bind:to="'/home/friend'">
						<span class="el-icon-user"></span>
					</router-link>
				</el-menu-item>
<!--				<el-menu-item title="群聊" v-if="this.groupChatVisible">-->
<!--					<router-link v-bind:to="'/home/group'">-->
<!--						<span class="icon iconfont icon-group_fill"></span>-->
<!--					</router-link>-->
<!--				</el-menu-item>-->
        <el-menu-item title="修改密码" @click="showChangePassword()">
          <span class="el-icon-edit"></span>
        </el-menu-item>
        <el-menu-item title="清空聊天数据" @click="showClearRecord()">
          <span class="el-icon-delete"></span>
        </el-menu-item>
				<el-menu-item title="设置" @click="showAnswerMessageSetting()">
					<span class="el-icon-setting"></span>
				</el-menu-item>
			</el-menu>
			<div class="exit-box" @click="onExit()" title="退出">
				<span class="icon iconfont icon-exit"></span>
			</div>
		</el-aside>
		<el-main class="content-box">
			<router-view></router-view>
		</el-main>
		<setting :visible="showSettingDialog" @close="closeSetting()"></setting>
    <change-password :visible="showChangePasswordDialog" @close="closeChangePassword()"></change-password>
    <clear-chat-record :visible="showClearRecordDialog" @close="closeClearRecord()"/>
    <answer-message-setting :visible="showAnswerMessageSettingDialog" @close="closeAnswerMessageSetting()"/>
		<user-info v-show="uiStore.userInfo.show" :pos="uiStore.userInfo.pos" :user="uiStore.userInfo.user"
			@close="$store.commit('closeUserInfoBox')"></user-info>
		<full-image :visible="uiStore.fullImage.show" :url="uiStore.fullImage.url"
			@close="$store.commit('closeFullImageBox')"></full-image>
		<chat-private-video ref="privateVideo"></chat-private-video>
		<chat-video-acceptor ref="videoAcceptor"></chat-video-acceptor>
	</el-container>
</template>

<script>
	import HeadImage from '../components/common/HeadImage.vue';
  import Setting from '../components/setting/Setting.vue';
  import ChangePassword from '../components/setting/ChangePassword.vue';
	import UserInfo from '../components/common/UserInfo.vue';
	import FullImage from '../components/common/FullImage.vue';
	import ChatPrivateVideo from '../components/chat/ChatPrivateVideo.vue';
	import ChatVideoAcceptor from '../components/chat/ChatVideoAcceptor.vue';
  import ClearChatRecord from "@/components/setting/ClearChatRecord.vue";
  import AnswerMessageSetting from "../components/setting/AnswerMessageSetting.vue";

	export default {
		components: {
			HeadImage,
      Setting,
      ChangePassword,
			UserInfo,
			FullImage,
			ChatPrivateVideo,
			ChatVideoAcceptor,
      ClearChatRecord,
      AnswerMessageSetting,
		},
		data() {
			return {
				showSettingDialog: false,
        showChangePasswordDialog: false,
        showClearRecordDialog:false,
        showAnswerMessageSettingDialog: false,
				lastPlayAudioTime: new Date().getTime() - 1000,
			}
		},
		methods: {
      // loadLoginUserInfo() {
      //   this.$http({
      //     url: `/user/find/${this.$store.state.userStore.userInfo.id}`,
      //     method: 'get'
      //   }).then((user) => {
      //     if(user.type == 1){
      //       this.groupChatVisible = false;
      //     }else{
      //       this.groupChatVisible = true;
      //     }
      //   })
      // },
			init() {
				this.$store.dispatch("load").then(() => {
					// ws初始化
					this.$wsApi.connect(process.env.VUE_APP_WS_URL, sessionStorage.getItem("accessToken"));
					this.$wsApi.onConnect(() => {
						// 加载离线消息
						this.pullPrivateOfflineMessage(this.$store.state.chatStore.privateMsgMaxId);
						this.pullGroupOfflineMessage(this.$store.state.chatStore.groupMsgMaxId);
					});
					this.$wsApi.onMessage((cmd, msgInfo) => {
						if (cmd == 2) {
							// 关闭ws
							this.$wsApi.close(3000)
              window.close();
							// 异地登录，强制下线
							this.$alert("您已在其他地方登陆，将被强制下线", "强制下线通知", {
								confirmButtonText: '确定',
								callback: action => {
									location.href = "/";
								}
							});

						} else if (cmd == 3) {
							// 插入私聊消息
							this.handlePrivateMessage(msgInfo);
						} else if (cmd == 4) {
							// 插入群聊消息
							this.handleGroupMessage(msgInfo);
						}
					});
					this.$wsApi.onClose((e) => {
						console.log(e);
						if (e.code != 3000) {
							// 断线重连
							this.$message.error("连接断开，正在尝试重新连接...");
							this.$wsApi.reconnect(process.env.VUE_APP_WS_URL, sessionStorage.getItem(
								"accessToken"));
						}
					});
				}).catch((e) => {
					console.log("初始化失败", e);
				})
			},
			pullPrivateOfflineMessage(minId) {
				this.$http({
					url: "/message/private/pullOfflineMessage?minId=" + minId,
					method: 'get'
				});
			},
			pullGroupOfflineMessage(minId) {
				this.$http({
					url: "/message/group/pullOfflineMessage?minId=" + minId,
					method: 'get'
				});
			},
			handlePrivateMessage(msg) {
				// 消息加载标志
				if (msg.type == this.$enums.MESSAGE_TYPE.LOADDING) {
					this.$store.commit("loadingPrivateMsg", JSON.parse(msg.content))
					return;
				}
				// 消息已读处理，清空已读数量
				if (msg.type == this.$enums.MESSAGE_TYPE.READED) {
					this.$store.commit("resetUnreadCount", {
						type: 'PRIVATE',
						targetId: msg.recvId
					})
					return;
				}
				// 消息回执处理,改消息状态为已读
				if (msg.type == this.$enums.MESSAGE_TYPE.RECEIPT) {
					this.$store.commit("readedMessage", {
						friendId: msg.sendId
					})
					return;
				}
				// 标记这条消息是不是自己发的
				msg.selfSend = msg.sendId == this.$store.state.userStore.userInfo.id;
				// 好友id
				let friendId = msg.selfSend ? msg.recvId : msg.sendId;
				this.loadFriendInfo(friendId).then((friend) => {
					this.insertPrivateMessage(friend, msg);
				})
			},
			insertPrivateMessage(friend, msg) {
				// webrtc 信令
				if (msg.type >= this.$enums.MESSAGE_TYPE.RTC_CALL_VOICE &&
					msg.type <= this.$enums.MESSAGE_TYPE.RTC_CANDIDATE) {
					let rtcInfo = this.$store.state.userStore.rtcInfo;
					// 呼叫
					if (msg.type == this.$enums.MESSAGE_TYPE.RTC_CALL_VOICE ||
						msg.type == this.$enums.MESSAGE_TYPE.RTC_CALL_VIDEO ||
						rtcInfo.state == this.$enums.RTC_STATE.FREE ||
						rtcInfo.state == this.$enums.RTC_STATE.WAIT_ACCEPT) {
						this.$refs.videoAcceptor.onRTCMessage(msg,friend)
					} else {
						this.$refs.privateVideo.onRTCMessage(msg)
					}
					return;
				}

				let chatInfo = {
					type: 'PRIVATE',
					targetId: friend.id,
					showName: friend.nickName,
					headImage: friend.headImage
				};
				// 打开会话
				this.$store.commit("openChat", chatInfo);
				// 插入消息
				this.$store.commit("insertMessage", msg);
				// 播放提示音
				if (!msg.selfSend && msg.status != this.$enums.MESSAGE_STATUS.READED) {
					this.playAudioTip();
				}
			},
			handleGroupMessage(msg) {
				// 消息加载标志
				if (msg.type == this.$enums.MESSAGE_TYPE.LOADDING) {
					this.$store.commit("loadingGroupMsg", JSON.parse(msg.content))
					return;
				}
				// 消息已读处理
				if (msg.type == this.$enums.MESSAGE_TYPE.READED) {
					// 我已读对方的消息，清空已读数量
					let chatInfo = {
						type: 'GROUP',
						targetId: msg.groupId
					}
					this.$store.commit("resetUnreadCount", chatInfo)
					return;
				}
				// 消息回执处理
				if (msg.type == this.$enums.MESSAGE_TYPE.RECEIPT) {
					// 更新消息已读人数
					let msgInfo = {
						id: msg.id,
						groupId: msg.groupId,
						readedCount: msg.readedCount,
						receiptOk: msg.receiptOk
					};
					this.$store.commit("updateMessage", msgInfo)
					return;
				}
				// 标记这条消息是不是自己发的
				msg.selfSend = msg.sendId == this.$store.state.userStore.userInfo.id;
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
				this.$store.commit("openChat", chatInfo);
				// 插入消息
				this.$store.commit("insertMessage", msg);
				// 播放提示音
				if (!msg.selfSend && msg.status != this.$enums.MESSAGE_STATUS.READED) {
					this.playAudioTip();
				}
			},
			onExit() {
				this.$wsApi.close(3000);
				sessionStorage.removeItem("accessToken");
				location.href = "/";
			},
      getNoReadCnt() {
        console.log("getNoReadCnt");
        console.log(this.unreadCount);
        if(this.unreadCount > 0){
          this.playAudioTip();
        }
        this.$http({
          url: `/message/private/noReadCnt`,
          method: 'get'
        }).then((noReadCnt) => {
          console.log('noReadCnt：' + noReadCnt);
          if(noReadCnt != this.unreadCount){
            this.pullPrivateOfflineMessage(0);
          }
        });
      },
			playAudioTip() {
				if (new Date().getTime() - this.lastPlayAudioTime > 1000) {
          console.log("playAudioTip");
					this.lastPlayAudioTime = new Date().getTime();
					let audio = new Audio();
					let url = require(`@/assets/audio/tip.wav`);
					audio.src = url;
					audio.play().then(() => {
            // 播放成功
            console.log('音频播放成功');
          }).catch(error => {
            // 播放失败，这里处理错误
            console.error('音频播放失败:', error);
            this.$router.push("/home/chat");
            // 强制刷新页面
            location.reload();
          });
				}

			},
			showSetting() {
				this.showSettingDialog = true;
			},
			closeSetting() {
				this.showSettingDialog = false;
			},
      showChangePassword() {
        this.showChangePasswordDialog = true;
      },
      closeChangePassword() {
        this.showChangePasswordDialog = false;
      },
      showClearRecord() {
        this.showClearRecordDialog = true;
      },
      closeClearRecord() {
        this.showClearRecordDialog = false;
      },
      showAnswerMessageSetting() {
        this.showAnswerMessageSettingDialog = true;
      },
      closeAnswerMessageSetting() {
        this.showAnswerMessageSettingDialog = false;
      },
			loadFriendInfo(id) {
				return new Promise((resolve, reject) => {
					let friend = this.$store.state.friendStore.friends.find((f) => f.id == id);
					if (friend) {
						resolve(friend);
					} else {
						this.$http({
							url: `/friend/find/${id}`,
							method: 'get'
						}).then((friend) => {
							this.$store.commit("addFriend", friend);
							resolve(friend)
						})
					}
				});
			},
			loadGroupInfo(id) {
				return new Promise((resolve, reject) => {
					let group = this.$store.state.groupStore.groups.find((g) => g.id == id);
					if (group) {
						resolve(group);
					} else {
						this.$http({
							url: `/group/find/${id}`,
							method: 'get'
						}).then((group) => {
							resolve(group)
							this.$store.commit("addGroup", group);
						})
					}
				});
			}
		},
		computed: {
			uiStore() {
				return this.$store.state.uiStore;
			},
			unreadCount() {
				let unreadCount = 0;
				let chats = this.$store.state.chatStore.chats;
				chats.forEach((chat) => {
					unreadCount += chat.unreadCount
				});
				return unreadCount;
			}
		},
		watch: {
			unreadCount: {
				handler(newCount, oldCount) {
					let tip = newCount > 0 ? `${newCount}条未读` : "";
					this.$elm.setTitleTip(tip);
				},
				immediate: true
			}
		},
		mounted() {
			this.init();
      setInterval(this.getNoReadCnt,15000);//每20s获取一次
		},
		unmounted() {
			this.$wsApi.close();
		}
	}
</script>

<style scoped lang="scss">
	.navi-bar {
		background: #333333;
		padding: 10px;
		padding-top: 50px;

		.el-menu {
			border: none;
			flex: 1;

			.el-menu-item {
				margin: 25px 0;

				.router-link-exact-active span {
					color: white !important;
				}



				span {
					font-size: 24px !important;
					color: #aaaaaa;

					&:hover {
						color: white !important;
					}
				}

				.unread-text {
					position: absolute;
					line-height: 20px;
					background-color: #f56c6c;
					left: 36px;
					top: 7px;
					color: white;
					border-radius: 30px;
					padding: 0 5px;
					font-size: 10px;
					text-align: center;
					white-space: nowrap;
					border: 1px solid #f1e5e5;
				}
			}
		}



		.exit-box {
			position: absolute;
			width: 60px;
			bottom: 40px;
			color: #aaaaaa;
			text-align: center;
			cursor: pointer;

			.icon {
				font-size: 28px;
			}
			&:hover {
				color: white !important;
			}
		}
	}

	.content-box {
		padding: 0;
		background-color: #E9EEF3;
		color: #333;
		text-align: center;

	}
</style>