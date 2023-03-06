<template>
  <Layout>
    <Title title="年终奖管理"></Title>
    <div style="margin-top: 10px">
      <el-table
          :data="employeeList"
          stripe
          style="width: 100%"
          :header-cell-style="{'text-align':'center'}"
          :cell-style="{'text-align':'center'}">
        <el-table-column
            prop="employeeId"
            label="员工id"
            width="200">
        </el-table-column>
        <el-table-column
            prop="name"
            label="姓名"
            width="300">
        </el-table-column>
        <el-table-column
            prop="yearSalarySum"
            label="1-11月工资"
            width="250">
        </el-table-column>
        <el-table-column
            prop="finalBonus"
            label="年终奖金额"
            width="250">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="changeBonus(scope.row.employeeId)"
                type="text"
                size="small">
              修改
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
        title="修改年终奖"
        :visible.sync="dialogVisible"
        width="40%">
      <div style="width: 90%; margin: 0 auto">
        <label>请输入年终奖金额:</label><br>
        <el-input type="text"  v-model.lazy="finalBonus"></el-input><br>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">提交修改</el-button>
      </span>
    </el-dialog>
  </Layout>
</template>

<script>
import Layout from "../../components/content/Layout";
import Title from "../../components/content/Title";
import {changeBonus, getAllBonus} from "../../network/generalManager";

export default {
  name: 'Bonus',
  components: {
    Layout,
    Title
  },
  data() {
    return {
      employeeList: [],
      id:'',
      finalBonus:'',
      dialogVisible: false,
    }
  },
  mounted() {
    getAllBonus().then(res => {
      this.employeeList=res.result
    })
  },
  methods: {
    changeBonus(id){
      this.id=id
      this.dialogVisible=true
    },
    submit(){
      let config = {
        params: {
          id: this.id,
          bonus: Number(this.finalBonus)
        }
      }
      console.log(config)
      if(this.finalBonus<0){
        alert("年终奖不能为负！")
      }
      else {
        changeBonus(config).then(res => {
          this.$message({
            type: "success",
            message: "修改成功!"
          });
          this.dialogVisible = false;
          getAllBonus().then(res => {
            this.employeeList=res.result
          })
        })
      }
    },
  }
}
</script>

<style>

</style>
