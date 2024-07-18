<template>
  <el-dialog class="setting" title="清除聊天记录" :visible.sync="visible"  width="500px" :before-close="onClose">
<!--    <el-form :model="changePasswordForm" label-width="80px" :rules="rules" ref="changePasswordForm">-->
<!--      <el-form-item label="旧密码" prop="oldPassword">-->
<!--        <el-input type="password" v-model="changePasswordForm.oldPassword" autocomplete="off" placeholder="旧密码" style="width: 300px"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="新密码" prop="newPassword">-->
<!--        <el-input :type="newPwType" v-model="changePasswordForm.newPassword" autocomplete="off" placeholder="新密码" style="width: 300px">-->
<!--        </el-input>-->
<!--        <i :class="newPwIcon" @click="Show" style="margin-left: 10px;"></i>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="确认密码" prop="confirmPassword">-->
<!--        <el-input :type="confirPwType" v-model="changePasswordForm.confirmPassword" autocomplete="off" placeholder="确认密码" style="width: 300px"></el-input>-->
<!--        <i :class="confirmPwIcon" @click="confirmPwShow" style="margin-left: 10px;"></i>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--    <span slot="footer" class="dialog-footer">-->
<!--			<el-button @click="onClose()">取 消</el-button>-->
    <el-button type="primary" @click="onDeleteOneDayBeforeChatRecord()">删除1天前聊天数据</el-button>
    <el-button type="primary" @click="onDeleteThreeDayBeforeChatRecord()">删除3天前聊天数据</el-button>
<!--		</span>-->
  </el-dialog>
</template>

<script>
import FileUpload from "../common/FileUpload.vue";
import userStore from "@/store/userStore";

export default {
  name: "setting",
  components: {
    FileUpload
  },
  data() {
    var checkPassword = (rule, value, callback) => {
      if (value === '') {
        return callback(new Error('请输入密码'));
      }
      callback();
    };

    var checkConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        return callback(new Error('请输入密码'));
      }
      if (value != this.changePasswordForm.newPassword) {
        return callback(new Error('两次密码输入不一致'));
      }
      callback();
    };

    return {
      // 密码的可见不可见
      newPwType:"password",
      newPwIcon:"el-icon-view",
      confirPwType:"password",
      confirmPwIcon:"el-icon-view",
      changePasswordForm: {
        oldPassword:null,
        newPassword:null,
        confirmPassword:null,
      },
      rules: {
        oldPassword: [{
          validator: checkPassword,
          trigger: 'blur'
        }],
        newPassword: [{
          validator: checkPassword,
          trigger: 'blur'
        }],
        confirmPassword: [{
          validator: checkConfirmPassword,
          trigger: 'blur'
        }]
      }
    }
  },
  methods: {
    // 密码的可见和不可见
    Show() {
      // 点击改变input框的展示方式
      this.newPwType = this.newPwType === "password" ? "text" : "password";
      this.newPwIcon = this.newPwIcon ==="el-icon-view"?"el-icon-watermelon":"el-icon-view";
    },
    // 密码的可见和不可见
    confirmPwShow() {
      // 点击改变input框的展示方式
      this.confirPwType = this.confirPwType === "password" ? "text" : "password";
      this.confirmPwIcon = this.confirmPwIcon ==="el-icon-view"?"el-icon-watermelon":"el-icon-view";
    },
    onClose() {
      this.$emit("close");
    },
    onSubmit() {
      this.$refs['changePasswordForm'].validate((valid) => {
        if (!valid) {
          return false;
        }
        this.$http({
          url: "/modifyPwd",
          method: "put",
          data: this.changePasswordForm
        }).then(()=>{
          this.$emit("close");
          this.$message.success("修改成功");
        })
      });
    },
    initForm(){
      this.changePasswordForm = {};
    },
    onDeleteOneDayBeforeChatRecord(){
      this.$http({
        url: "/message/private/deleteOneDayBeforeMessage",
        method: "delete",
      }).then(()=>{
        this.updateChatStorage(1);
        this.$emit("close");
        this.$message.success("删除成功");
      })
    },
    onDeleteThreeDayBeforeChatRecord(){
      this.$http({
        url: "/message/private/deleteThreeDayBeforeMessage",
        method: "delete",
      }).then(()=>{
        console.log("onDeleteThreeDayBeforeChatRecord")
        this.updateChatStorage(3);
        this.$emit("close");
        this.$message.success("删除成功");
      })
    },
    updateChatStorage(day){
      let userId = userStore.state.userInfo.id;
      let key = "chats-" + userId;
      let item = localStorage.getItem(key)
      if(item){
        let chatsData = JSON.parse(item);
        const compareTime = new Date().getTime() - 24 * 60 * 60 * 1000 * day;
        //todo
        // const compareTime = new Date().getTime() - 1000 * day;

        for(let i=0;i<chatsData.chats.length;i++){
          let chat = chatsData.chats[i];
          let newChat = chat;
          let newMessages = [];
          let newMessageIndex = 0;
          for(let y=0;y<chat.messages.length;y++){
            let message = chat.messages[y];
            if(message.type == '0'){
              if(compareTime - message.sendTime < 0){
                newMessages[newMessageIndex] = message;
                newMessageIndex++;
              }
            }else{
              newMessages[newMessageIndex] = message;
              newMessageIndex++;
            }
          }

          let lastNewMessages = [];
          let lastNewMessageIndex = 0;
          for(let j=0;j<newMessageIndex;j++){
            if(j == newMessageIndex - 1){
              lastNewMessages[lastNewMessageIndex] = newMessages[j];
              lastNewMessageIndex++;
              break;
            }
            if(newMessages[j].type == '20' && newMessages[j+1].type == '20'){
              continue;
            }else{
              lastNewMessages[lastNewMessageIndex] = newMessages[j];
              lastNewMessageIndex++;
            }
          }

          if(lastNewMessages.length == 1 && lastNewMessages[0].type == '20'){
            newChat.lastSendTime=lastNewMessages[0].sendTime;
            newChat.lastTimeTip=lastNewMessages[0].sendTime;
            lastNewMessages = [];
            newChat.lastContent="";
          }else if(lastNewMessages.length == 0){
            newChat.lastContent="";
          }
          newChat.messages = lastNewMessages;
          chatsData.chats[i] = newChat;
        }
        this.$store.commit("initChats", chatsData);
        this.$store.commit("saveToStorage");
      }
    },
  },
  props: {
    visible: {
      type: Boolean
    }
  },
  computed:{
  },
  watch: {
    visible: function(newData, oldData) {
      this.initForm();
    }
  }
}
</script>

<style lang="scss" >
.setting {
  display: flex;
  align-items: center; /* 垂直居中 */
}

.el-input__inner {
  flex: 1; /* 让输入框占据可用空间 */
}

.icon {
  margin-left: 10px; /* 图标与输入框之间的间距 */
  /* 你可以添加更多的样式来定制图标，比如字体、颜色等 */
}
</style>
