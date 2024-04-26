<template>
	<view class="login-form" v-if="!this.autoLoginFlg">
		<view class="login-title">欢迎登录</view>
		<uni-forms style="margin-top: 100px;" :modelValue="loginForm" :rules="rules" validate-trigger="bind">
			<uni-forms-item name="userName">
				<uni-easyinput type="text" v-model="loginForm.userName" prefix-icon="person" focus placeholder="用户名" />
			</uni-forms-item>
			<uni-forms-item name="password">
				<uni-easyinput type="password" v-model="loginForm.password" prefix-icon="locked" placeholder="密码" />
			</uni-forms-item>
			<button @click="submit" type="primary">登录</button>
		</uni-forms>
	</view>
	<view class="login-form" v-if="this.autoLoginFlg">
		<view class="login-title">登录中</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				info:'',
				loginForm: {
					terminal: 1, // APP终端
					userName: '',
					password: ''
				},
				autoLoginFlg:false,
				autoLoginForm: {
					terminal: 1, // APP终端
					thirdUserId: '',
					userName: null,
					nickName: null,
					signature: null,
					kefuUserId: null,
				},
				rules: {
					userName: {
						rules: [{
							required: true,
							errorMessage: '请输入用户名',
						}]
					},
					password: {
						rules: [{
							required: true,
							errorMessage: '请输入密码',
						}]
					}
				}
			}
		},
		methods: {
			submit() {
				this.$http({
					url: '/login',
					data: this.loginForm,
					method: 'POST'
				}).then(data => {
					console.log("登录成功,自动跳转到聊天页面...")
					uni.setStorageSync("userName", this.loginForm.userName);
					uni.setStorageSync("password", this.loginForm.password);
					uni.setStorageSync("loginInfo", data);
					uni.removeStorageSync("autoOpenChat");
					// 调用App.vue的初始化方法
					getApp().init()
					// 跳转到聊天页面   
					uni.switchTab({
						url: "/pages/chat/chat"
					})
				})
			}
		},
		onLoad(e) {
			console.log('loging');
			this.loginForm.userName = uni.getStorageSync("userName");
			this.loginForm.password = uni.getStorageSync("password");
			let loginInfo = uni.getStorageSync("loginInfo");
			if (loginInfo) {
				uni.removeStorageSync("autoOpenChat");
				// 跳转到聊天页面
				uni.switchTab({
					url: "/pages/chat/chat"
				})
			}else{
				if(e.item != undefined){
					this.autoLoginFlg = true;
					this.info = JSON.parse(decodeURIComponent(e.item));
					if(this.info != undefined){
						// console.log('获取的参数',this.info);
						setTimeout(() => {
							// 获取URL中的查询字符串部分
							this.autoLoginForm.thirdUserId = typeof this.info.userId == 'undefined'?'':this.info.userId
							this.autoLoginForm.userName = typeof this.info.userName == 'undefined'?'':this.info.userName
							this.autoLoginForm.nickName = typeof this.info.nickName == 'undefined'?'':this.info.nickName
							this.autoLoginForm.signature = typeof this.info.signature == 'undefined'?'':this.info.signature
							this.autoLoginForm.kefuUserId = typeof this.info.kefuUserId == 'undefined'?'':this.info.kefuUserId
							this.$http({
								url: '/thirdLogin',
								data: this.autoLoginForm,
								method: 'POST'
							}).then(data => {
								console.log("登录成功,自动跳转到聊天页面...")
								uni.setStorageSync("userName", data.userName);
								uni.setStorageSync("loginInfo", data);
								uni.removeStorageSync("chats-" + data.userId);
								uni.removeStorageSync("chats-undefined");
								// 调用App.vue的初始化方法
								getApp().init()
								// 跳转到聊天页面
								
								let chat = {
									type: 'PRIVATE',
									targetId: data.kefuUserInfo.id,
									showName: data.kefuUserInfo.nickName,
									headImage: data.kefuUserInfo.headImage,
								};
								this.$store.commit("openChat", chat);
								uni.setStorageSync("autoOpenChat", "1");
								uni.switchTab({
									// url:"/pages/chat/chat-box?chatIdx=0"
					url: "/pages/chat/chat"
								})
							})
						}, 500)
					}
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.login-form {
		margin: 50rpx;

		.login-title {
			margin-top: 100rpx;
			margin-bottom: 100rpx;
			color: royalblue;
			text-align: center;
			font-size: 60rpx;
			font-weight: 600;
		}
	}
</style>