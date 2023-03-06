<template>
  <Layout>
    <Title title="账户管理"></Title>
    <div>
      <el-button type="primary" size="medium" @click="addAccount">新增账户</el-button>
        <el-input v-model="vagueName" style="width: 30%;right: 150px;position: absolute"></el-input>
        <el-button type="primary" size="medium" style="right: 70px;position: absolute" @click="findAccount">查询</el-button>
    </div>

    <div style="margin-top: 10px" v-if="wholeListVisible">
      <el-table
        :data="accountList"
        stripe
        style="width: 100%"
        :header-cell-style="{'text-align':'center'}"
        :cell-style="{'text-align':'center'}">
        <el-table-column
            prop="name"
            label="账户名称"
            width="350">
        </el-table-column>
        <el-table-column
            prop="amount"
            label="账户余额"
            width="350">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="delAccount(scope.row.id)"
                type="text"
                size="small">
              删除
            </el-button>
            <el-button
                @click.native.prevent="updateAccount(scope.row.id,scope.row.amount)"
                type="text"
                size="small">
              修改
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="margin-top: 10px" v-if="findListVisible">
      <el-table
          :data="findAccountList"
          stripe
          style="width: 100%"
          :header-cell-style="{'text-align':'center'}"
          :cell-style="{'text-align':'center'}">
        <el-table-column
            prop="name"
            label="账户名称"
            width="350">
        </el-table-column>
        <el-table-column
            prop="amount"
            label="账户余额"
            width="350">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="delAccount(scope.row.id)"
                type="text"
                size="small">
              删除
            </el-button>
            <el-button
                @click.native.prevent="updateAccount(scope.row.id,scope.row.amount)"
                type="text"
                size="small">
              修改
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button @click="returnToWhole">返回</el-button>
    </div>
    <el-dialog
        title="新增账户"
        :visible.sync="dialogVisibleAdd"
        width="40%">
      <div style="width: 90%; margin: 0 auto">
        <label>账户名称:</label><br>
        <el-input type="text"  v-model="accountInfo.name"></el-input><br>
        <label>余额:</label><br>
        <el-input type="number"  v-model="accountInfo.amount"></el-input><br>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAdd">提交新增</el-button>
      </span>
    </el-dialog>
    <el-dialog
        title="修改账户"
        :visible.sync="dialogVisibleUp"
        width="40%">
      <div style="width: 90%; margin: 0 auto">
        <label>账户名称:</label><br>
        <el-input type="text"  v-model="nameUp"></el-input><br>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUpdate()">提交修改</el-button>
      </span>
    </el-dialog>
  </Layout>
</template>

<script>
import Layout from "../../components/content/Layout";
import {addAccount, deleteAccount, findAccount, getAllAccounts, updateAccount} from "../../network/finance";
export default {
  name: "AccountManagement",
  components: {Layout},
  data() {
    return {
      wholeListVisible: true,
      findListVisible: false,
      dialogVisibleAdd: false,
      dialogVisibleUp:false,
      accountInfo: {
        name: "",
        amount: 0,
        id: null
      },
      accountList: [],
      findAccountList: [],
      nameUp: "",
      idUp: 0,
      amountUp: 0,
      vagueName: ""
    }
  },
  async mounted() {
    await getAllAccounts().then(_res => {
      this.accountList = this.accountList.concat(_res.result)
    })
  },
  methods: {
    addAccount() {
      this.dialogVisibleAdd = true
    },
    submitAdd() {
      this.dialogVisibleAdd = false
      console.log(this.accountInfo)
      addAccount(this.accountInfo).then(_res => {
        if (_res.code === "E0001") {
          this.$message({
            type: "error",
            message: "账户已存在！"
          });
          alert("账户已存在！");
        } else {
          this.$emit("refresh");
          this.$message({
            type: "success",
            message: "添加成功"
          });
          alert("添加成功！");
        }
      })
      this.accountInfo = {}
      window.location.reload()
    },
    delAccount(id) {
      let config = {
        params: {
          id: id
        }
      };
      this.$confirm('是否要删除该账户？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAccount(config)
        this.$emit("refresh");
        this.$message({
          type: "success",
          message: "删除成功"
        });
        alert("删除成功！");
        window.location.reload()
      })
    },
    updateAccount(id,amount) {
      this.dialogVisibleUp = true
      this.idUp = id
      this.amountUp = amount
    },
    submitUpdate(){
      this.dialogVisibleUp = false
      let config = {
          id: this.idUp,
          name: this.nameUp,
          amount: this.amountUp
      };
      console.log(config)
      updateAccount(config).then(_res => {
        if (_res.code === "E0001") {
          this.$message({
            type: "error",
            message: "账户已存在！"
          });
          alert("账户已存在！");
        } else if(this.nameUp === ""){
          this.$message({
            type: "error",
            message: "账户名称不能为空！"
          });
          alert("账户名称不能为空！");
        } else{
          this.$emit("refresh");
          this.$message({
            type: "success",
            message: "添加成功"
          });
          alert("修改成功！");
        }
      })
      window.location.reload()
      this.nameUp=""
      this.amountUp=0
      this.idUp=0
    },
    findAccount(){
      this.dialogVisibleUp = false
      let config = {
        params: {
          name: this.vagueName,
        }
      };
      findAccount(config).then(_res =>{
        this.findAccountList=_res.result
      })
      this.wholeListVisible = false;
      this.findListVisible = true;
      if(this.findAccountList == null){
        alert("没有相关结果。")
      }
      this.accountInfo = {}
    },
    returnToWhole(){
      this.wholeListVisible = true;
      this.findListVisible = false;
    }

  }
}
</script>

<style scoped>

</style>
