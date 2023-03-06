<template>
  <Layout>
    <Title title="经营情况表"></Title>
    <div>
      <el-date-picker
          v-model="date"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="getData()">
      </el-date-picker>
      <el-button v-if="isFinding" @click="returnToAll" style="margin-left: 30px">返回所有列表</el-button>
      <el-button @click="excel" style="float: right">导出为EXCEL</el-button>
      <el-row style="margin-top: 20px">
        <el-col :span="6"><strong>总收入:</strong> {{businessSituationTable.totalIncome}}</el-col>
        <el-col :span="6"><strong>总支出:</strong> {{businessSituationTable.totalPay}}</el-col>
        <el-col :span="6"><strong>利润:</strong> {{businessSituationTable.profit}}</el-col>
      </el-row>
      <el-tabs active-name="INCOME" :stretch="true">
        <el-tab-pane label="收入类" name="INCOME">
          <div v-if="incomeList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <el-table
                :data="incomeList"
                stripe
                style="width: 100%"
                :header-cell-style="{'text-align':'center'}"
                :cell-style="{'text-align':'center'}"
                class="mt15">
              <el-table-column
                  prop="description"
                  label="类型"
                  width="250">
              </el-table-column>
              <el-table-column
                  width="400"
                  label="日期">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.date) }}
                </template>
              </el-table-column>
              <el-table-column
                  prop="amount"
                  label="金额"
                  width="350">
              </el-table-column>
              <el-table-column
                  label="折让比例">
                <template slot-scope="scope">
                  {{scope.row.description==="销售收入"?scope.row.discount:"无"}}
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="支出类" name="PAYMENT">
          <div v-if="paymentList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <el-table
                :data="paymentList"
                stripe
                style="width: 100%"
                :header-cell-style="{'text-align':'center'}"
                :cell-style="{'text-align':'center'}"
                class="mt15">
              <el-table-column
                  prop="description"
                  label="类型"
                  width="250">
              </el-table-column>
              <el-table-column
                  width="400"
                  label="日期">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.date) }}
                </template>
              </el-table-column>
              <el-table-column
                  prop="amount"
                  label="金额"
                  width="350">
              </el-table-column>
              <el-table-column
                  label="折让比例">
                无
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>

    </div>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import { getBusinessSituation } from "../../network/finance"

export default {
  components: {
    Layout,
    Title
  },
  data() {
    return {
      date:[],
      businessSituationTable: {},
      incomeList:[],
      paymentList:[],
      isFinding: false,
    }
  },
  computed: {
    beginDate: function () {
      return this.date === '' ? '' : this.formatDate(this.date[0])
    },
    endDate: function () {
      return this.date === '' ? '' : this.formatDate(this.date[1])
    }
  },
  async mounted() {
    await getBusinessSituation().then(res => {
      this.businessSituationTable=res.result
      this.incomeList=this.businessSituationTable.items.filter(item => item.type==="收入")
      this.paymentList=this.businessSituationTable.items.filter(item => item.type==="支出")
    })
  },
  methods: {
    getData() {
      const config = {
        params: {
          beginDateStr: this.beginDate,
          endDateStr: this.endDate
        }
      }
      getBusinessSituation(config).then(res => {
        this.businessSituationTable=res.result
        this.incomeList=this.businessSituationTable.items.filter(item => item.type==="收入")
        this.paymentList=this.businessSituationTable.items.filter(item => item.type==="支出")
        this.$message.success('查询成功!')
      })
      this.isFinding = true
    },
    returnToAll() {
      location.reload()
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
    excel(){
      excelBusinessSituation().then(res=>{

      })
    }
  }
};
</script>

<style scoped lang="scss">
.mt15 {
  margin-top: 15px;
}
</style>
