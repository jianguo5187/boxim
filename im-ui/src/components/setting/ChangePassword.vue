<template>
  <el-dialog class="setting" title="设置" :visible.sync="visible"  width="500px" :before-close="onClose">
    <el-form :model="changePasswordForm" label-width="80px" :rules="rules" ref="changePasswordForm">
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input type="password" v-model="changePasswordForm.oldPassword" autocomplete="off" placeholder="旧密码" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input :type="newPwType" v-model="changePasswordForm.newPassword" autocomplete="off" placeholder="新密码" style="width: 300px">
        </el-input>
        <i :class="newPwIcon" @click="Show" style="margin-left: 10px;"></i>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input :type="confirPwType" v-model="changePasswordForm.confirmPassword" autocomplete="off" placeholder="确认密码" style="width: 300px"></el-input>
        <i :class="confirmPwIcon" @click="confirmPwShow" style="margin-left: 10px;"></i>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
			<el-button @click="onClose()">取 消</el-button>
			<el-button type="primary" @click="onSubmit()">确 定</el-button>
		</span>
  </el-dialog>
</template>

<script>
import FileUpload from "../common/FileUpload.vue";

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
    }
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
