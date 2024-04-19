<template>
	<div class="user-info-mask" @click="$emit('close')">
		<div class="user-info" :style="{left: pos.x+'px',top: pos.y+'px'}" @click.stop>
			<div class="user-info-box">
				<div class="avatar">
					<head-image :name="user.nickName" :url="user.headImageThumb" :size="60" :online="user.online"
						@click.native="showFullImage()"> </head-image>
				</div>
				<div>
					<el-descriptions :column="1" :title="user.userName" class="user-info-items">
						<el-descriptions-item label="昵称">{{ user.nickName }}
						</el-descriptions-item>

						<el-descriptions-item label="备注">{{ friendRemarkName }}
						</el-descriptions-item>
					</el-descriptions>
				</div>
			</div>
			<el-divider content-position="center"></el-divider>
			<div class="user-btn-group">
				<el-button v-show="isFriend" type="primary" @click="openUpdateRemarkName()">修改备注</el-button>
<!--				<el-button v-show="!isFriend" type="primary" @click="onAddFriend()">加为好友</el-button>-->
			</div>
		</div>

    <el-dialog title="修改备注" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item prop="remarkName" label="备注">
          <el-input v-model="form.remarkName" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateRemarkName">确认修改</el-button>
        <el-button @click="cancel">关闭</el-button>
      </div>
    </el-dialog>
	</div>
</template>

<script>
	import HeadImage from './HeadImage.vue'

	export default {
		name: "userInfo",
		components: {
			HeadImage
		},
		data() {
			return {
        friendRemarkName:null,
        // 是否显示弹出层
        open: false,
        // 表单参数
        form: {},
        // 表单校验
        rules: {},
			}
		},
		props: {
			user: {
				type: Object
			},
			pos: {
				type: Object
			}
		},
		methods: {
      // 表单重置
      reset() {
        this.form = {
          remarkName: null,
          friendId: null,
        };
      },
      updateRemarkName() {
        this.$http({
          url: "/friend/updateRemark",
          method: "put",
          data: this.form
        }).then((data) => {
          this.friendRemarkName = this.form.remarkName;
          var nickName = "";
          let friends = this.$store.state.friendStore.friends;
          for(var i=0;i<friends.length;i++){
            var objFriend = friends[i];
            if(objFriend.id ==this.user.id){
              nickName = objFriend.nickName;
              this.$store.commit("activeFriend", i);
              this.$store.commit("updateFriendRemark", this.form.remarkName);
              break;
            }
          }

          let chats = this.$store.state.chatStore.chats;
          for(var j=0;j<chats.length;j++){
            var objChat = chats[j];
            if(objChat.targetId ==this.user.id){
              this.$store.commit("activeChatIndex", j);
              if(this.form.remarkName == undefined || this.form.remarkName == ''){
                this.$store.commit("updateChatRemark", nickName);
              }else{
                this.$store.commit("updateChatRemark", this.form.remarkName);
              }
              break;
            }
          }
          this.cancel();
        })
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
			openUpdateRemarkName() {
        this.open = true;
        this.reset();
        this.form.remarkName = this.friendRemarkName;
        this.form.friendId = this.user.id;
			},
			onAddFriend() {
				this.$http({
					url: "/friend/add",
					method: "post",
					params: {
						friendId: this.user.id
					}
				}).then((data) => {
					this.$message.success("添加成功，对方已成为您的好友");
					let friend = {
						id: this.user.id,
						nickName: this.user.nickName,
						headImage: this.user.headImageThumb,
						online: this.user.online
					}
					this.$store.commit("addFriend", friend);
				})
			},
			showFullImage() {
				if (this.user.headImage) {
					this.$store.commit("showFullImageBox", this.user.headImage);
				}
			},
      setFriendRemark(friend){
        if(friend != undefined){
          this.friendRemarkName = friend.remarkName;
        }else{
          this.friendRemarkName = null;
        }
      }
		},
		computed: {
			isFriend() {
				let friends = this.$store.state.friendStore.friends;
				let friend = friends.find((f) => f.id == this.user.id);
        this.setFriendRemark(friend);
				return friend != undefined;
			}
		}
	}
</script>

<style lang="scss">
	.user-info-mask {
		background-color: rgba($color: #000000, $alpha: 0);
		position: absolute;
		width: 100%;
		height: 100%;
	}

	.user-info {
		position: absolute;
		width: 300px;
		background-color: white;
		border: #dddddd solid 1px;
		border-radius: 5px;
		padding: 15px;

		.user-info-box {
			display: flex;

			.user-info-items {
				margin-left: 10px;
				white-space: nowrap;
				overflow: hidden;

				.el-descriptions__header {
					margin-bottom: 5px;
				}

				.el-descriptions__title {
					font-size: 20px;
				}

				.el-descriptions-item__cell {
					padding-bottom: 1px;
				}
			}
		}

		.user-btn-group {
			text-align: center;
		}
	}
</style>