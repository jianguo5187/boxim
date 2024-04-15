<template>
  <div class="login-view">
    <div class="login-content">
      跳转中
    </div>
  </div>

</template>

<script>
import Icp from '../components/common/Icp.vue'
export default {
  name: "thirdLogin",
  components: {
    Icp
  },
  data() {
    return {
      thirdLoginForm: {
        terminal: this.$enums.TERMINAL_TYPE.WEB,
        thirdUserId: null,
        userName: null,
        nickName: null,
        signature: null,
        kefuUserId: null,
      },
      rules: {}
    };
  },
  methods: {
    autoLogin(){
      // 获取URL中的查询字符串部分
      const queryParams = new URLSearchParams(window.location.search);
      console.log(queryParams);
      // 获取参数
      this.thirdLoginForm.thirdUserId = queryParams.get('userId');
      this.thirdLoginForm.userName = queryParams.get('userName');
      this.thirdLoginForm.nickName = queryParams.get('nickName');
      this.thirdLoginForm.signature = queryParams.get('signature');
      this.thirdLoginForm.kefuUserId = queryParams.get('kefuUserId');
      // this.thirdLoginForm.terminal = queryParams.get('terminal');

      // 使用参数
      this.$http({
        url: "/thirdLogin",
        method: 'post',
        data: this.thirdLoginForm
      }).then((data) => {
        // 保存密码到cookie(不安全)
        this.setCookie('username', data.userName);
        // this.setCookie('password', this.loginForm.password);
        // 保存token
        sessionStorage.setItem("accessToken", data.accessToken);
        sessionStorage.setItem("refreshToken", data.refreshToken);
        // this.$message.success("登陆成功");
        // this.$router.push("/home/chat");

        let chat = {
          type: 'PRIVATE',
          targetId: data.kefuUserInfo.id,
          showName: data.kefuUserInfo.nickName,
          headImage: data.kefuUserInfo.headImageThumb,
        };
        this.$store.commit("openChat", chat);
        this.$store.commit("activeChat", 0);
        this.$router.push("/home/chat");
      })
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
    }
  },
  mounted() {
    this.autoLogin();
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