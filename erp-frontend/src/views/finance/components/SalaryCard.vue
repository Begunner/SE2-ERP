<template>
  <div class="card">
    <el-card v-for="(item, index) in list" :index="item.index" :key="item.id" shadow="hover">
      <template #header>
        <el-row>
          <el-col :span="18">
            <span><strong>id: </strong>{{item.id}}</span>
            <el-button v-if="authorization() === 1" style="margin-left: 10px"
                       type="success" icon="el-icon-check" circle size="mini" @click="approval(item.id)"></el-button>
            <el-button v-if="authorization() === 1"
                       type="danger" icon="el-icon-close" circle size="mini" @click="deny(item.id)"></el-button>

            <span style="margin-left: 10px">
              <el-tag v-if="type === 2" effect="dark" type='success'>审核通过</el-tag>
              <el-tag v-if="type === 3" effect="dark" type='danger'>审核未通过</el-tag>
            </span>
          </el-col>
<!--          <el-col :span="6">-->
<!--            <el-button class="button" type="text"-->
<!--                       v-if="authorization() === 3"-->
<!--                       @click="writeBack(index)">-->
<!--              红冲-->
<!--            </el-button>-->
<!--          </el-col>-->
        </el-row>
      </template>
      <div>
        <el-row>
          <el-col :span="8">
            <span><strong>起始时间: </strong>{{formatDate(item.beginDate)}}</span>
          </el-col>
          <el-col :span="8">
            <span><strong>截止时间: </strong>{{formatDate(item.endDate)}}</span>
          </el-col>
          <el-col :span="8">
            <span><strong>创建时间:</strong>{{formatDate(item.createTime)}}</span>
          </el-col>
        </el-row>

        <el-row style="margin-top: 15px">
          <el-col :span="4">
            <span><strong>员工编号: </strong>{{item.employeeId}}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>员工姓名: </strong>{{item.name}}</span>
          </el-col>
          <el-col :span="8">
            <span><strong>银行账户: </strong>{{item.account}}</span>
          </el-col>
        </el-row>

        <el-row style="margin-top: 15px">
          <el-col :span="6">
            <span><strong>出勤天数: </strong>{{item.attendanceDays}}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>缺勤天数: </strong>{{item.absenceDays}}</span>
          </el-col>
        </el-row>

        <el-row style="margin-top: 15px">
          <el-col :span="6">
            <span><strong>应发工资: </strong>{{item.expectedSalary}}</span>
          </el-col>
          <el-col :span="4">
            <span><strong>基本工资: </strong>{{item.basicSalary}}</span>
          </el-col>
          <el-col :span="4">
            <span><strong>岗位工资: </strong>{{item.postSalary}}</span>
          </el-col>
          <el-col :span="4">
            <span><strong>提成工资: </strong>{{item.commission}}</span>
          </el-col>
          <el-col :span="4">
            <span><strong>应扣工资: </strong>{{item.deductSalary}}</span>
          </el-col>
        </el-row>


        <el-row style="margin-top: 15px">
          <el-col :span="6">
            <span><strong>应扣税款总计: </strong>{{item.totalTax}}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>扣除个人所得税: </strong>{{item.personalIncomeTax}}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>扣除失业保险: </strong>{{item.unemploymentInsurance}}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>扣除住房公积金: </strong>{{item.housingProvidentFund}}</span>
          </el-col>
        </el-row>
        <el-row>
          <el-col style="margin-top: 15px">
            <span><strong>实发工资: </strong>{{item.finalSalary}}</span>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>

import {approvalSalary} from "../../../network/finance";
import { writeBackSalary } from "../../../network/finance";

export default {
  name: 'SalaryCard',
  props: {
    list: Array,
    type: Number
  },
  data() {
    return {
      showAll: [],
    }
  },
  mounted() {
    this.showAll = new Array(this.list.length).fill(false)
  },
  methods: {
    changeState(index) {
      this.$set(this.showAll, index, !this.showAll[index])
    },
    authorization() {
      if (this.type === 1 && sessionStorage.getItem('role') === 'GM') {
        return 1
      } else if (this.type === 2 && (sessionStorage.getItem('role') === 'FINANCIAL_STAFF' || sessionStorage.getItem('role') === 'ADMIN')) {
        return 3
      }
    },
    approval(id) {
      let config = {
        params: {
          salarySheetId: id,
          state: 'SUCCESS'
        }
      }
      if (this.type === 1) {
        approvalSalary(config).then(res => {
          this.$emit("refresh")
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        })
      }
    },
    deny(id) {
      let config = {
        params: {
          salarySheetId: id,
          state: 'FAILURE'
        }
      }
      if (this.type === 1) {
        approvalSalary(config).then(res => {
          this.$emit("refresh")
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        })
      }
    },
    formatDate(gmt) {
      let date = new Date(gmt)
      let m = (date.getMonth() + 1), mm = '-' + (m < 10 ? '0' + m : m)
      let d = date.getDate(), dd = '-' + (d < 10 ? '0' + d : d)
      let h = date.getHours(), hh = ' ' +(h < 10 ? '0' + h : h)
      let i = date.getMinutes(), ii = ':' +(i < 10 ? '0' + i : i)
      let s = date.getSeconds(), ss = ':' +(s < 10 ? '0' + s : s)
      return date.getFullYear() + mm + dd + hh + ii + ss
    },
    writeBack(index){
      let config=JSON.parse(JSON.stringify(this.list[index]));
      config.expectedSalary=-config.expectedSalary
      config.basicSalary=-config.basicSalary
      config.postSalary=-config.postSalary
      config.commission=-config.commission
      config.deductSalary=-config.deductSalary
      config.totalTax=-config.totalTax
      config.personalIncomeTax=-config.personalIncomeTax
      config.unemploymentInsurance=-config.unemploymentInsurance
      config.housingProvidentFund=-config.housingProvidentFund
      config.finalSalary=-config.finalSalary
      writeBackSalary(config).then(res=>{
        this.$emit("refresh")
        this.$message({
          message: '红冲成功!',
          type: 'success'
        })
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.card {
  width: 80%;
  margin: 0 auto;
  .button {
    float: right;
    padding: 3px 0
  }
}
.el-card {
  margin-bottom: 20px;
  background: #EEF7F2;
}
</style>
