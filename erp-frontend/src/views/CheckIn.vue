<template>
  <Layout>
    <Title title="打卡记录"></Title>
    <div>
      <el-date-picker
          v-model="date"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="getData()">
      </el-date-picker>
      <el-button v-if="isFinding" @click="returnToAll" style="margin-left: 30px">返回所有列表</el-button>
      <div class="mt15" v-if="!isFinding">
        <el-table
            :data="checkList"
            stripe
            style="width: 100%"
            :header-cell-style="{'text-align':'center'}"
            :cell-style="{'text-align':'center'}"
            class="mt15">
          <el-table-column
              width="500"
              label="日期">
            <template slot-scope="scope">
              {{ formatDate(scope.row.date) }}
            </template>
          </el-table-column>
          <el-table-column
              label="打卡记录">
            <template slot-scope="scope">
              {{scope.row.hasClockIn?"已打卡":"未打卡"}}
            </template>
          </el-table-column>
          <el-table-column
              width="500"
              label="时间">
            <template slot-scope="scope">
              {{scope.row.hasClockIn?(formatTime(scope.row.time)):"无" }}
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="mt15" v-if="isFinding">
        <el-table
            :data="findCheckList"
            stripe
            style="width: 100%"
            :header-cell-style="{'text-align':'center'}"
            :cell-style="{'text-align':'center'}"
            class="mt15">
          <el-table-column
              width="500"
              label="日期">
            <template slot-scope="scope">
              {{ formatDate(scope.row.date) }}
            </template>
          </el-table-column>
          <el-table-column
              label="打卡记录">
            <template slot-scope="scope">
              {{scope.row.hasClockIn?"已打卡":"未打卡"}}
            </template>
          </el-table-column>
          <el-table-column
              width="500"
              label="时间">
            <template slot-scope="scope">
              {{scope.row.hasClockIn?(formatTime(scope.row.time)):"无" }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import { getCheckByTime, getAllCheck } from "../network/checkIn"

export default {
  components: {
    Layout,
    Title
  },
  data() {
    return {
      date: [],
      checkList: [],
      findCheckList: [],
      isFinding: false,
    }
  },
  computed: {
    beginDate: function(){
      return this.date === '' ? '' : this.formatDate(this.date[0])
    },
    endDate: function() {
      return this.date === '' ? '' : this.formatDate(this.date[1])
    }
  },
  async mounted() {
    await getAllCheck().then(_res => {
      this.checkList =_res.result
      console.log(this.checkList)
    })
  },
  methods: {
    getData() {
      this.findCheckList=[]
      const config = {
        params: {
          beginDateStr: this.beginDate,
          endDateStr: this.endDate
        }
      }
      getCheckByTime(config).then(_res => {
        this.findCheckList = _res.result
        this.$message.success('查询成功!')
      })
      this.isFinding = true
    },
    returnToAll() {
      this.isFinding = false
      getAllCheck().then(_res => {
        this.checkList =_res.result
      })
    },
    formatDate(gmt) {
      let date = new Date(gmt)
      let m = (date.getMonth() + 1), mm = '-' + (m < 10 ? '0' + m : m)
      let d = date.getDate(), dd = '-' + (d < 10 ? '0' + d : d)
      return date.getFullYear() + mm + dd
    },
    formatTime(gmt) {
      let date = new Date(gmt)
      let h = date.getHours(), hh = ' ' +(h < 10 ? '0' + h : h)
      let i = date.getMinutes(), ii = ':' +(i < 10 ? '0' + i : i)
      let s = date.getSeconds(), ss = ':' +(s < 10 ? '0' + s : s)
      return hh + ii + ss
    },

  }
};
</script>

<style scoped lang="scss">
.mt15 {
  margin-top: 15px;
}
</style>
