<template>
	<view class="tab-page">

		<view v-if="loading" class="chat-loading" >
			<loading  :size="50" :mask="false">
				<view>消息接收中...</view>
			</loading>
		</view>
		<view class="chat-tip" v-if="!loading && chatStore.chats.length==0">
			温馨提示：您现在还没有任何聊天消息，快跟您的好友发起聊天吧~
		</view>
		<scroll-view class="scroll-bar" v-else scroll-with-animation="true" scroll-y="true">
			<view v-for="(chat,index) in chatStore.chats" :key="index">
				<chat-item :chat="chat" :index="index" @longpress.native="onShowMenu($event,index)"></chat-item>
			</view>
		</scroll-view>

		<pop-menu v-show="menu.show" :menu-style="menu.style" :items="menu.items" @close="menu.show=false"
			@select="onSelectMenu"></pop-menu>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				menu: {
					show: false,
					style: "",
					chatIdx: -1,
					items: [{
							key: 'DELETE',
							name: '删除',
							icon: 'trash'
						},
						{
							key: 'TOP',
							name: '置顶',
							icon: 'arrow-up'
						}
					]
				}
			}
		},
		methods: {
			onSelectMenu(item) {
				switch (item.key) {
					case 'DELETE':
						this.removeChat(this.menu.chatIdx);
						break;
					case 'TOP':
						this.moveToTop(this.menu.chatIdx);
						break;
					default:
						break;
				}
				this.menu.show = false;
			},
			onShowMenu(e, chatIdx) {
				uni.getSystemInfo({
					success: (res) => {
						let touches = e.touches[0];
						let style = "";
						/* 因 非H5端不兼容 style 属性绑定 Object ，所以拼接字符 */
						if (touches.clientY > (res.windowHeight / 2)) {
							style = `bottom:${res.windowHeight-touches.clientY}px;`;
						} else {
							style = `top:${touches.clientY}px;`;
						}
						if (touches.clientX > (res.windowWidth / 2)) {
							style += `right:${res.windowWidth-touches.clientX}px;`;
						} else {
							style += `left:${touches.clientX}px;`;
						}
						this.menu.style = style;
						this.menu.chatIdx = chatIdx;
						// 
						this.$nextTick(() => {
							this.menu.show = true;
						});
					}
				})
			},
			removeChat(chatIdx) {
				this.$store.commit("removeChat", chatIdx);
			},
			moveToTop(chatIdx) {
				this.$store.commit("moveTop", chatIdx);
			},
			refreshUnreadBadge() {
				if (this.unreadCount > 0) {
					uni.setTabBarBadge({
						index: 0,
						text: this.unreadCount + ""
					})
				} else {
					uni.removeTabBarBadge({
						index: 0,
						complete: () => {}
					})

				}
			}
		},
		mounted() {
		 //  setTimeout(() => {
			// if(this.chatStore.chats.length == 0){
			// 	// 跳转到好友页面   
			// 	uni.switchTab({
			// 		url: "/pages/friend/friend"
			// 	})
			// }
		 //  }, 500);
		 console.log('chat.vue');
		  if(uni.getStorageSync("autoOpenChat") && uni.getStorageSync("autoOpenChat") == "1"){
			  var index = 0;
			  var chatIndex = 0;
			  var autoOpenChatUserId = uni.getStorageSync("autoOpenChatUserId");
			  var chats = this.chatStore.chats;
			  for(var i=0;i<chats.length;i++){
				  var chatObj = chats[i];
				  if(chats[i].targetId == autoOpenChatUserId){
					  chatIndex = i;
				  }
			  }
			 //  this.chatStore.chats.forEach(chat => {
				//   index++;
			 //  	if(chat.targetId == autoOpenChatUserId){
				// 	chatIndex = index;
				// }
			 //  })
			  	uni.navigateTo({
			  		url: "/pages/chat/chat-box?chatIdx=" + chatIndex
			  	})
		  }
		},
		computed: {
			chatStore() {
				return this.$store.state.chatStore;
			},
			unreadCount() {
				let count = 0;
				this.chatStore.chats.forEach(chat => {
					count += chat.unreadCount;
				})
				return count;
			},
			loading() {
				return this.chatStore.loadingGroupMsg || this.chatStore.loadingPrivateMsg
			}
		},
		watch: {
			unreadCount(newCount, oldCount) {
				this.refreshUnreadBadge();
			}
		},
		onShow() {
			this.refreshUnreadBadge();
		}
	}
</script>

<style scoped lang="scss">
	.chat-tip {
		position: absolute;
		top: 400rpx;
		padding: 50rpx;
		line-height: 50rpx;
		text-align: left;
		color: darkblue;
		font-size: 30rpx;
	}

	.chat-loading {
		display: block;
		height: 100rpx;
		background: white;
		position: relative;
		color: blue;
	}
</style>