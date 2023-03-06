<template>
  <Layout>
    <Title title="员工管理"></Title>
    <el-button type="primary" size="medium" @click="addEmployee">新增员工</el-button>
    <div style="margin-top: 10px">
      <el-table
          :data="employeeList"
          stripe
          style="width: 100%"
          :header-cell-style="{'text-align':'center'}"
          :cell-style="{'text-align':'center'}">
        <el-table-column
            prop="id"
            label="id"
            width="100">
        </el-table-column>
        <el-table-column
            prop="name"
            label="姓名"
            width="150">
        </el-table-column>
        <el-table-column
            prop="gender"
            label="性别"
            width="100">
        </el-table-column>
        <el-table-column
            width="120"
            label="生日">
          <template slot-scope="scope">
            {{ formatDate(scope.row.birthday) }}
          </template>
        </el-table-column>
        <el-table-column
            prop="role"
            label="工作岗位"
            width="200"
            :filters="[{ text: '库存管理', value: 'INVENTORY_MANAGER' }, { text: '销售', value: 'SALE_STAFF' }, { text: '销售经理', value: 'SALE_MANAGER' }, { text: '财务', value: 'FINANCIAL_STAFF' }, { text: '人力资源', value: 'HR' }, { text: '总经理', value: 'GM' }]"
            :filter-method="filterTag"
            filter-placement="bottom-end">
        </el-table-column>
        <el-table-column
            prop="basicSalary"
            label="基本工资（/月）"
            width="150">
        </el-table-column>
        <el-table-column
            prop="postSalary"
            label="岗位工资（/月）"
            width="150">
        </el-table-column>
        <el-table-column
            prop="salaryComputeStrategy"
            label="薪酬规则"
            width="120">
        </el-table-column>
        <el-table-column
            prop="salaryPaymentStrategy"
            label="工资发放方式"
            width="120">
        </el-table-column>
        <el-table-column
            prop="account"
            label="工资卡账户"
            width="150">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="changeEmployee(scope.row)"
                type="text"
                size="small">
              修改
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
        title="新增员工"
        :visible.sync="dialogVisibleAdd"
        width="40%">
      <div style="width: 90%; margin: 0 auto">
        <label class="dialogItem">姓名: </label><br>
        <el-input class="inputItem" type="text"  v-model.lazy="employeeInfo.name"></el-input><br><br>
        <label class="dialogItem">性别: </label><br>
        <el-select class="inputItem" name="gender" v-model="employeeInfo.gender">
          <el-option value="男">男</el-option>
          <el-option value="女">女</el-option>
        </el-select><br><br>
        <label class="dialogItem">生日: </label><br>
        <el-date-picker v-model="employeeInfo.birthday" type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
        </el-date-picker><br><br>
        <label class="dialogItem">电话号码: </label><br>
        <el-input class="inputItem" type="text"  v-model.lazy="employeeInfo.phone"></el-input><br><br>
        <label class="dialogItem">工作岗位: </label><br>
        <el-select class="inputItem" name="role" v-model="employeeInfo.role">
          <el-option value="INVENTORY_MANAGER">库存管理人员</el-option>
          <el-option value="SALE_STAFF">进货销售人员</el-option>
          <el-option value="FINANCIAL_STAFF">财务人员</el-option>
          <el-option value="SALE_MANAGER">销售经理</el-option>
          <el-option value="HR">人力资源人员</el-option>
          <el-option value="GM">总经理</el-option>
        </el-select><br><br>
        <label class="dialogItem">基本工资: </label><br>
        <el-input class="inputItem" type="text" v-model.lazy="employeeInfo.basicSalary"></el-input><br><br>
        <label class="dialogItem">薪资计算策略: </label><br>
        <el-select class="inputItem" name="salaryComputeStrategy" v-model="employeeInfo.salaryComputeStrategy"><br>
          <el-option value="按月薪计算">按月薪计算</el-option>
          <el-option value="按月薪和提成计算">按月薪和提成计算</el-option>
          <el-option value="按年薪计算">按年薪计算</el-option>
        </el-select><br><br>
        <label class="dialogItem">薪资发放策略: </label><br>
        <el-select class="inputItem" name="salaryPaymentStrategy" v-model="employeeInfo.salaryPaymentStrategy">
          <el-option value="按月薪发放">按月薪发放</el-option>
          <el-option value="按年薪发放">按年薪发放</el-option>
        </el-select><br><br>
        <label class="dialogItem">工资卡账户: </label><br>
        <el-input class="inputItem" type="text"  v-model.lazy="employeeInfo.account"></el-input><br><br>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">提交新建</el-button>
      </span>
    </el-dialog>
    <el-dialog
        title="修改员工"
        :visible.sync="dialogVisibleUp"
        width="40%">
      <div style="width: 90%; margin: 0 auto">
        <label class="dialogItem"><strong>姓名: {{employeeInfoUp.name}}</strong></label><br><br>
        <label class="dialogItem">性别: </label><br>
        <el-select class="inputItem" name="gender" v-model="employeeInfoUp.gender">
          <el-option value="男">男</el-option>
          <el-option value="女">女</el-option>
        </el-select><br><br>
        <label class="dialogItem">生日: </label><br>
        <el-date-picker v-model="employeeInfoUp.birthday" type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
        </el-date-picker><br><br>
        <label class="dialogItem">电话号码: </label><br>
        <el-input class="inputItem" type="text"  v-model.lazy="employeeInfoUp.phone"></el-input><br><br>
        <label class="dialogItem">工作岗位: </label><br>
        <el-select class="inputItem" name="role" v-model="employeeInfoUp.role">
          <el-option value="INVENTORY_MANAGER">库存管理人员</el-option>
          <el-option value="SALE_STAFF">进货销售人员</el-option>
          <el-option value="FINANCIAL_STAFF">财务人员</el-option>
          <el-option value="SALE_MANAGER">销售经理</el-option>
          <el-option value="HR">人力资源人员</el-option>
          <el-option value="GM">总经理</el-option>
        </el-select><br><br>
        <label class="dialogItem">基本工资: </label><br>
        <el-input class="inputItem" type="text" v-model.lazy="employeeInfoUp.basicSalary"></el-input><br><br>
        <label class="dialogItem">薪资计算策略: </label><br>
        <el-select class="inputItem" name="salaryComputeStrategy" v-model="employeeInfoUp.salaryComputeStrategy"><br>
          <el-option value="按月薪计算">按月薪计算</el-option>
          <el-option value="按月薪和提成计算">按月薪和提成计算</el-option>
          <el-option value="按年薪计算">按年薪计算</el-option>
        </el-select><br><br>
        <label class="dialogItem">薪资发放策略: </label><br>
        <el-select class="inputItem" name="salaryPaymentStrategy" v-model="employeeInfoUp.salaryPaymentStrategy">
          <el-option value="按月薪发放">按月薪发放</el-option>
          <el-option value="按年薪发放">按年薪发放</el-option>
        </el-select><br><br>
        <label class="dialogItem">工资卡账户: </label><br>
        <el-input class="inputItem" type="text"  v-model.lazy="employeeInfoUp.account"></el-input><br><br>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUp">提交修改</el-button>
      </span>
    </el-dialog>
  </Layout>
</template>

<script>
import Layout from "../../components/content/Layout";
import Title from "../../components/content/Title";
import { getAllEmployee } from "../../network/humanResources";
import { addEmployee,updateEmployee } from "../../network/humanResources";

export default {
  name: 'EmployeeView',
  components: {
    Layout,
    Title
  },
  data() {
    return {
      employeeList: [],
      employeeInfo:{
        id:null,
        name:null,
        gender:null,
        birthday:null,
        phone:null,
        role:null,
        basicSalary:null,
        postSalary:null,
        salaryComputeStrategy:null,
        salaryPaymentStrategy:null,
        account:null,
      },

      dialogVisibleAdd: false,
      employeeInfoUp:{
        id:null,
        name:null,
        gender:null,
        birthday:null,
        phone:null,
        role:null,
        basicSalary:null,
        postSalary:null,
        salaryComputeStrategy:null,
        salaryPaymentStrategy:null,
        account:null,
      },
      dialogVisibleUp: false,
    }
  },
  async mounted() {
    await getAllEmployee().then(_res => {
      this.employeeList =_res.result
    })
  },
  methods: {
    filterTag(value, row) {
      return row.role === value
    },
    addEmployee() {
      this.dialogVisibleAdd = true
    },
    changeEmployee(info) {
      this.employeeInfoUp=JSON.parse(JSON.stringify(info));
      this.dialogVisibleUp=true;
    },
    submit(){
      this.dialogVisibleAdd = false
      addEmployee(this.employeeInfo).then(res=>{
        alert("添加成功！")
        this.employeeInfo = {};
      })
      window.location.reload()
    },
    submitUp() {
      this.dialogVisibleUp = false
      updateEmployee(this.employeeInfoUp).then(res=>{
        alert("修改成功！")
        this.employeeInfoUp = {};
      })
      window.location.reload()
    },
    formatDate(gmt) {
      let date = new Date(gmt)
      let m = (date.getMonth() + 1), mm = '-' + (m < 10 ? '0' + m : m)
      let d = date.getDate(), dd = '-' + (d < 10 ? '0' + d : d)
      return date.getFullYear() + mm + dd
    },
  }
}
</script>

<style>
  .dialogItem {

  }
  .inputItem {
    width: 100%;
  }
</style>
