<template>
	<view class="login-form">
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
					thirdUserId: '',
					userName: null,
					nickName: null,
					signature: null,
				},
				rules: {
				}
			}
		},
		methods: {
		},
		onLoad(e) {
			this.info = JSON.parse(decodeURIComponent(e.item));
			console.log('获取的参数',this.info);
			setTimeout(() => {
				// 获取URL中的查询字符串部分
				this.loginForm.thirdUserId = this.info.userId
				this.loginForm.userName = this.info.userName
				this.loginForm.nickName = this.info.nickName
				this.loginForm.signature = this.info.signature
				this.$http({
					url: '/thirdLogin',
					data: this.loginForm,
					method: 'POST'
				}).then(data => {
					console.log("登录成功,自动跳转到聊天页面...")
					uni.setStorageSync("userName", data.userName);
					// uni.setStorageSync("password", this.loginForm.password);
					uni.setStorageSync("loginInfo", data);
					// 调用App.vue的初始化方法
					getApp().init()
					// 跳转到聊天页面   
					uni.switchTab({
						url: "/pages/chat/chat"
					})
				})
			}, 0)
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