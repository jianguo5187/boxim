<template>
	<div class="login-view">
		<div class="login-content">
			<el-form class="login-form" :model="loginForm" status-icon :rules="rules" ref="loginForm" label-width="60px"
				@keyup.enter.native="submitForm('loginForm')" v-if="!autoLoginFlg">
				<div class="login-brand">登陆聊天IM</div>
				<el-form-item label="终端" prop="userName" v-show="false">
					<el-input type="terminal" v-model="loginForm.terminal" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="用户名" prop="userName">
					<el-input type="userName" v-model="loginForm.userName" autocomplete="off"
						placeholder="用户名"></el-input>

				</el-form-item>
				<el-form-item label="密码" prop="password">
					<el-input type="password" v-model="loginForm.password" autocomplete="off"
						placeholder="密码"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="submitForm('loginForm')">登陆</el-button>
					<el-button @click="resetForm('loginForm')">清空</el-button>
				</el-form-item>
<!--				<div class="register">-->
<!--					<router-link to="/register">没有账号,前往注册</router-link>-->
<!--				</div>-->
			</el-form>
      <div v-if="autoLoginFlg" style="font-size: 40px;font-weight: bold">
        <span class="login-title">登录中...</span>
      </div>
		</div>
<!--		<icp></icp>-->
	</div>

</template>

<script>
	import Icp from '../components/common/Icp.vue'
	export default {
		name: "login",
		components: {
			Icp
		},
		data() {
			var checkUsername = (rule, value, callback) => {
				if (!value) {
					return callback(new Error('请输入用户名'));
				}
				callback();
			};
			var checkPassword = (rule, value, callback) => {
				if (value === '') {
					callback(new Error('请输入密码'));
				}
				callback();

			};
			return {
				loginForm: {
					terminal: this.$enums.TERMINAL_TYPE.WEB,
					userName: '',
					password: ''
				},
				rules: {
					userName: [{
						validator: checkUsername,
						trigger: 'blur'
					}],
					password: [{
						validator: checkPassword,
						trigger: 'blur'
					}]
				},
        autoLoginFlg:false,
			};
		},
		methods: {
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.$http({
								url: "/login",
								method: 'post',
								data: this.loginForm
							})
							.then((data) => {
								// 保存密码到cookie(不安全)
								this.setCookie('username', this.loginForm.userName);
								this.setCookie('password', this.loginForm.password);
								// 保存token
								sessionStorage.setItem("accessToken", data.accessToken);
								sessionStorage.setItem("refreshToken", data.refreshToken);
								this.$message.success("登陆成功");
								this.$router.push("/home/chat");
							})

					}
				});
			},
			resetForm(formName) {
				this.$refs[formName].resetFields();
			},
			// 获取cookie、
			getCookie(name) {
				let reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
				let arr = document.cookie.match(reg)
				if (arr) {
					return unescape(arr[2]);
				}
				return '';
			},
			// 设置cookie,增加到vue实例方便全局调用
			setCookie(name, value, expiredays) {
				var exdate = new Date();
				exdate.setDate(exdate.getDate() + expiredays);
				document.cookie = name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate
					.toGMTString());
			},
			// 删除cookie
			delCookie(name) {
				var exp = new Date();
				exp.setTime(exp.getTime() - 1);
				var cval = this.getCookie(name);
				if (cval != null) {
					document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
				}
			},
      kefuAutoLogin(){
        this.loginForm.userName="kefu1";

        this.$http({
          url: "/kefuAutoThirdLogin",
          method: 'post',
          data: this.loginForm
        })
            .then((data) => {
              // 保存密码到cookie(不安全)
              this.setCookie('username', this.loginForm.userName);
              this.setCookie('password', this.loginForm.password);
              // 保存token
              sessionStorage.setItem("accessToken", data.accessToken);
              sessionStorage.setItem("refreshToken", data.refreshToken);
              this.$message.success("登陆成功");
              this.$router.push("/home/chat");
            })
      }
		},
    created() {
      if (window.location.href.includes('?') && window.location.href.includes('autoKefuImLogin=1')) {
        this.autoLoginFlg = true;
        this.kefuAutoLogin();
      }
      console.log("created");
    },
    mounted() {
      console.log("mounted");
			this.loginForm.userName = this.getCookie("username");
			// cookie存密码并不安全，暂时是为了方便
			this.loginForm.password = this.getCookie("password");

		}
	}
</script>

<style scoped lang="scss">
	.login-view {
		width: 100%;
		height: 100%;
		background: rgb(232, 242, 255);
		background-size: cover;
		box-sizing: border-box;


		.login-content {
			position: relative;
			display: flex;
			justify-content: space-around;
			align-items: center;
			padding: 10%;

			.login-intro {
				flex: 1;
				padding: 40px;
				max-width: 600px;

				.login-title {
					text-align: center;
					font-weight: 600;
					font-size: 30px;
				}

				.login-icons {
					display: flex;
					align-items: center;

					.login-icon {
						padding-left: 5px;
					}
				}
			}

			.login-form {
				height: 340px;
				width: 400px;
				padding: 30px;
				background: white;
				opacity: 0.9;
				box-shadow: 0px 0px 1px #ccc;
				border-radius: 3%;
				overflow: hidden;
				border: 1px solid #ccc;

				.login-brand {
					line-height: 50px;
					margin: 30px 0 40px 0;
					font-size: 22px;
					font-weight: 600;
					letter-spacing: 2px;
					text-transform: uppercase;
					text-align: center;
				}

				.register {
					display: flex;
					flex-direction: row-reverse;
					line-height: 40px;
					text-align: left;
					padding-left: 20px;
				}
			}
		}
	}
</style>