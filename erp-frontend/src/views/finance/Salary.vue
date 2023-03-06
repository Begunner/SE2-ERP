<template>
  <Layout>
    <Title title="工资单"></Title>
    <div class="body">
      <el-tabs v-model="activeName" :stretch="true">
        <el-tab-pane label="待审批" name="PENDING">
          <div v-if="pendingList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <salary-card :list="pendingList" :type="1" @refresh="getSalary()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="审批完成" name="SUCCESS">
          <div v-if="successList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <salary-card :list="successList" :type="2"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="审批失败" name="FAILURE">
          <div v-if="failureList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <salary-card :list="failureList" :type="3"/>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import SalaryCard from "./components/SalaryCard";
import { getAllSalary } from '../../network/finance'


export default {
  name: 'SalaryView',
  components: {
    SalaryCard,
    Layout,
    Title
  },
  data() {
    return {
      activeName: 'PENDING',
      salaryList: [],
      pendingList: [],
      successList: [],
      failureList: [],
    }
  },
  mounted() {
    this.getSalary()
  },
  methods: {
    getSalary() {
      getAllSalary({}).then(_res => {
        this.salaryList = _res.result
        this.pendingList = this.salaryList.filter(item => item.state === '待审批')
        this.successList = this.salaryList.filter(item => item.state === '审批完成')
        this.failureList = this.salaryList.filter(item => item.state === '审批失败')
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            this.resetForm()
            done();
          })
          .catch(_ => {});
    },
  }
}
</script>

<style lang="scss" scoped>
.body {
  margin: 0 auto;
  margin-top: 10px;
  height: 80vh;
  overflow-y: auto;
  width: 100%;
  background: rgba($color: #fff, $alpha: 0.5);
}
</style>
