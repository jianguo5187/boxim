<template>
	<el-dialog class="setting" title="设置自动回复内容" :visible.sync="visible"  width="1200px"  :before-close="onClose">
		
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
            >新增</el-button>
          </el-col>
		</el-row>

		<el-table
		  :data="tableData"
		  ref="table"
		  style="width: 100%"
		  :height="500"
		  border
		>
			<el-table-column prop="id" label="ID" width="180">
				<template slot-scope="scope">
					<p v-show="scope.row.id == 0"></p>
					<p v-show="scope.row.id != 0">{{ scope.row.id }}</p>
				</template>
			</el-table-column>
			<el-table-column prop="content" label="问题">
				<template slot-scope="scope">
					<el-input v-model="scope.row.content" clearable v-show="scope.row.id == cellIndex" />
					<p v-show="scope.row.id != cellIndex" v-html="$emo.transform(scope.row.content)"></p>
				</template>
			</el-table-column>
			<el-table-column prop="answerContent" label="回答文本">
				<template slot-scope="scope">
					<el-input v-model="scope.row.answerContent" :disabled="scope.row.imgContent != undefined && scope.row.imgContent != ''" clearable v-show="scope.row.id == cellIndex" />
					<p v-show="scope.row.id != cellIndex" v-html="$emo.transform(scope.row.answerContent)"></p>
				</template>
			</el-table-column>

      <el-table-column label="回答图片" align="center" prop="answerImgContent" width="120">
        <template slot-scope="scope">
          <file-upload  class="autoAnswer-uploader"
                        :disabled="scope.row.id != cellIndex"
                        :action="imageAction"
                        :showLoading="true"
                        :maxSize="maxSize"
                        @success="onAnswerUploadSuccess"
                        @before="onBeforeUpload(scope.row)"
                        @fail="onUploadFail"
                        :fileTypes="['image/jpeg', 'image/png', 'image/jpg','image/webp']">
            <img v-if="scope.row.answerImgContent != undefined && scope.row.answerImgContent != ''" :src="scope.row.answerImgContent" class="avatar">
            <i v-else class="el-icon-plus autoAnswer-uploader-icon"></i>
          </file-upload>
        </template>
      </el-table-column>

			<el-table-column prop="welcomeShowFlag" label="状态" width="80">
				<template slot-scope="scope">
					<el-switch
					v-model="scope.row.welcomeShowFlag"
					active-color="#13ce66"
					inactive-color="#D9D9D9"
					active-value="1"
					inactive-value="0"
					:disabled="scope.row.id != cellIndex"
					>
					</el-switch>
			</template>
			</el-table-column>
			<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope">
				<el-button
					size="mini"
					type="text"
					icon="el-icon-edit"
					@click="handleSave(scope.row)"
					v-show="scope.row.id == cellIndex"
				>保存</el-button>
				<el-button
					size="mini"
					type="text"
					icon="el-icon-edit"
					@click="handleUpdate(scope.row)"
					v-show="scope.row.id != cellIndex"
				>修改</el-button>
				<el-button
					size="mini"
					type="text"
					icon="el-icon-delete"
					@click="handleDelete(scope.row)"
					v-show="scope.row.id != 0"
				>删除</el-button>
        <el-button
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleCancle(scope.row)"
            v-show="scope.row.id == cellIndex"
        >取消</el-button>
				</template>
			</el-table-column>
		</el-table>

		<span slot="footer" class="dialog-footer">
			<el-button @click="onClose()">关 闭</el-button>
		</span>
	</el-dialog>
</template>

<script>
	import FileUpload from "../../../../../sharkpayImBox/im-ui/src/components/common/FileUpload.vue";
	
	export default {
		name: "setting",
		components: {
			FileUpload
		},
		data() {
			return {
				userInfo: {
				},
				tableData: [
				],
				cellIndex:undefined,
        maxSize: 5*1024*1024,
        uploadFileRowId:undefined,
			}
		},
		methods: {
			onClose() {
				this.$emit("close");
			},
			onFindDefaultMessage(){
				this.$http({
					url: `/defaultMessage/loadAllDefaultMessage`,
					method: "get"
				}).then((defaultMessages) => {
					console.log(defaultMessages);
					this.tableData = defaultMessages;
				})
			},
			handleAdd(){
        console.log('handleAdd');
        // this.tableData.push({id:0});
				this.tableData.push({id:0,welcomeShowFlag:"1",content:'',answerContent:'',answerImgContent:''});
				this.cellIndex = 0;
				this.$nextTick(() => {
					// 滚动到新增行的位置
					const table = this.$refs.table;
					if (table) {
						table.$el.querySelector('.el-table__body-wrapper').scrollTop = table.$el.querySelector('.el-table__body-wrapper').scrollHeight - 140;
					}
				});
			},
			handleSave(row){
				console.log(row);

        if((row.answerImgContent == undefined || row.answerImgContent == "") && (row.answerContent == undefined || row.answerContent == "")){
          this.$message.error(`发送图片或发送内容必须输入一个`);
          return;
        }

				this.$http({
					url: `/defaultMessage/saveDefaultMessage`,
					method: "post",
					data: row
				}).then(() => {
					if(row.id == 0){
						this.$message.success("添加成功");
					}else{
						this.$message.success("修改成功");
					}
					this.cellIndex = 0;
					this.tableData = [];
					this.onFindDefaultMessage();
				})
			},
			handleUpdate(row) {
				this.cellIndex = row.id;
			},
			handleDelete(row) {
				this.$confirm(`确定要删除这行数据吗？`, '确认解除?', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: `/defaultMessage/deleteDefaultMessage/${row.id}`,
						method: 'delete'
					}).then((data) => {
						this.$message.success("删除成功");
						this.cellIndex = 0;
						this.tableData = [];
						this.onFindDefaultMessage();
					})
				})
			},
      handleCancle(row){
        this.cellIndex = 0;
        this.onFindDefaultMessage();
      },
      onBeforeUpload(row){
        this.uploadFileRowId = row.id;
      },
      onUploadFail(error, file){
        console.log('onUploadSuccess');
        console.log(error);
      },
      onAnswerUploadSuccess(data, file) {
        console.log('onUploadSuccess');

        this.tableData.forEach((rowData) => {
          if(rowData.id == this.cellIndex){
            rowData.answerImgContent = data.originUrl;
          }
        })
        this.$forceUpdate();
      },
		},
		props: {
			visible: {
				type: Boolean
			}
		},
		computed:{
			imageAction(){
				return `/image/upload`;
			}
		},
		watch: {
			visible: function(newData, oldData) {
				// 深拷贝
				let mine = this.$store.state.userStore.userInfo;
				this.userInfo = JSON.parse(JSON.stringify(mine));
				if(newData){
					this.cellIndex = 0;
					this.tableData = [];
					this.onFindDefaultMessage();
				}
			}
		}
	}
</script>

<style lang="scss" >
.setting {
  .autoAnswer-uploader {

    .el-upload {
      border: 1px dashed #d9d9d9 !important;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }

    .el-upload:hover {
      border-color: #409EFF;
    }

    .autoAnswer-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 60px;
      height: 60px;
      line-height: 60px;
      text-align: center;
    }

    .avatar {
      width: 60px;
      height: 60px;
      display: block;
    }
  }
}
</style>
